package processor;


import common.CsvProcessorRuntimeException;
import csv.formatter.impl.CsvFormatterNew;
import model.CsvMetaDetails;
import model.CsvSourceType;
import processor.impl.QueueOfAS400Processor;
import processor.impl.QueueOfUnixProcessor;
import processor.impl.QuotationOfAS400Processor;
import processor.impl.QuotationOfUnixProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/2
 * @time 11 :49 AM
 */
public abstract class AbstractProcessor implements CsvProcessor<byte[]> {


    private static final Map<CsvSourceType, AbstractProcessor> PROCESSOR_CACHED_MAP = new ConcurrentHashMap<>();

    private static CsvMetaDetails csvMetaDetails;

    static {
        csvMetaDetails = new CsvMetaDetails();
    }

    /**
     * 为了快速的找到合适的处理器，通过Map缓存快速存取Processor
     *
     * @param csvSourceType CSV类型
     * @return 处理对应CSV的合适的处理器
     */
    public static AbstractProcessor getProcessorByType(CsvSourceType csvSourceType) {
        AbstractProcessor processor = null;
        if (csvSourceType == null) {
            throw new CsvProcessorRuntimeException();
        }
        //没有出现过这样类型
        if (!PROCESSOR_CACHED_MAP.containsKey(csvSourceType)) {
            processor = getProcessor(csvSourceType);
            PROCESSOR_CACHED_MAP.put(csvSourceType, processor);
            return processor;
        } else {

            return PROCESSOR_CACHED_MAP.get(csvSourceType);
        }
    }


    /**
     * 通过CSV类型选择合适的处理器
     *
     * @param csvSourceType CSV类型
     * @return 处理对应CSV的合适的处理器
     */
    private static AbstractProcessor getProcessor(CsvSourceType csvSourceType) {

        AbstractProcessor processor = null;
        switch (csvSourceType) {
            case QUEUE_OF_UNIX:
                processor = QueueOfUnixProcessor.getProcessor();
                break;
            case QUEUE_OF_AS400:
                processor = QueueOfAS400Processor.getProcessor();
                break;
            case QUOTATION_OF_UNIX:
                processor = QuotationOfUnixProcessor.getProcessor();
                break;
            case QUOTATION_OF_AS400:
                processor = QuotationOfAS400Processor.getProcessor();
                break;
            default:
                throw new CsvProcessorRuntimeException();
        }
        processor = processor
                .bindMappingField()
                .bindAddedElements()
                .bindFieldNum()
                .bindTableHead();
        return processor;

    }

    /**
     * 计算、输出FlatMap的处理结果，传输给下面的Sink算子
     *
     * @param csvRow CSV行数据
     * @return 数据落地的指定格式
     * @throws CsvProcessorRuntimeException CSV处理异常
     */
    @Override
    public byte[] process(byte[] csvRow) throws CsvProcessorRuntimeException {


        byte[] resultByteArray = fit(csvRow);

        resultByteArray = buildRecord(resultByteArray);

        byte[] sinkResultByteArray = getSinkResult(resultByteArray);

        System.out.println(new String(sinkResultByteArray));

        return sinkResultByteArray;


    }


    /**
     * 通过调整CSV格式，通过交换顺序与插入列两种操作与元数据rdconfig文件对应
     *
     * @param csvRow CSV行数据
     * @return 监察消息对应的二进制
     * @throws CsvProcessorRuntimeException CSV处理异常
     */
    @Override
    public byte[] fit(byte[] csvRow) throws CsvProcessorRuntimeException {
        return CsvFormatterNew.getCsvFormatter().format(csvRow, csvMetaDetails);

    }

    /**
     * <p>返回目标字段（rdconfig文件规定的顺序）与源CSV字段（CSV文件给定的顺序）对应的映射表
     * <p>i.e.
     * <code>
     * <p>Map<Integer,Integer> mappingFields=new HashMap<>();
     * <p>mappingFields.put(0,1);
     * <p>mappingFields.put(1,2);
     * <p>mappingFields.put(2,3);
     * <p>mappingFields.put(3,4);
     * <p>mappingFields.put(4,0);
     * <p>return mappingFields;
     * </code>
     * <ol>假设有5个字段，返回的Map是以上结构:
     * <li>rdconfig文件的第0个字段对应的是CSV文件的第1列</li>
     * <li>rdconfig文件的第1个字段对应的是CSV文件的第2列</li>
     * <li>rdconfig文件的第2个字段对应的是CSV文件的第3列</li>
     * <li>rdconfig文件的第3个字段对应的是CSV文件的第4列</li>
     * <li>rdconfig文件的第4个字段对应的是CSV文件的第0列</li>
     * </ol>
     *
     * @return 字段映射表 mapping field map
     */
    protected Map<Integer, Integer> getMappingFieldMap() {
        return null;
    }

    /**
     * 返回新增加的元素的位置及内容
     * <p>
     * KEY表示插入的位置,VALUE表示的是插入的内容
     * <p>i.e.
     * <code>
     * <p>Map<Integer, String> addedElements=new HashMap<>();
     * <p>addedElements.put(-1,"first");
     * <p>addedElements.put(1,"second");
     * <p>addedElements.put(8,'last');
     * <p>return addedElements;
     * </code>
     * <ol>假设有需要插入3个字段，返回的Map是以上结构:
     * <li>在目标数据的第-1个字段后面插入first</li>
     * <li>在目标数据的第1个字段后面插入second</li>
     * <li>在目标数据的第8个字段后面插入last</li>
     * </ol>
     *
     * @return 含有添加元素的信息 added elements map
     */
    protected Map<Integer, String> getAddedElementsMap() {
        return null;
    }

    /**
     * 返回CSV文件中的数据的列数
     * <p>i.e.
     * <p>假设CSV数据的结构是：
     * <p>0,1,23,456,678
     * <p>返回5
     *
     * @return CSV文件中的数据列数 num of field
     */
    protected abstract int getNumOfField();


    /**
     * 返回CSV文件中的列头行，用于校验CSV数据行
     * <p>i.e.
     * <p>假设CSV数据的表头(第一行)是：
     * <p>A,B,C,D,E
     * <p>返回"A,B,C,D,E"
     *
     * @return CSV文件中的数头 table head
     */
    protected abstract String getTableHead();

    @Override
    public byte[] buildRecord(byte[] csvRow) throws CsvProcessorRuntimeException {
        return csvRow;
    }

    @Override
    public byte[] getSinkResult(byte[] record) throws CsvProcessorRuntimeException {
        return record;
    }

    private AbstractProcessor bindMappingField() {
        csvMetaDetails.setMappingField(getMappingFieldMap());
        return this;
    }

    private AbstractProcessor bindAddedElements() {
        csvMetaDetails.setAddedElements(getAddedElementsMap());
        return this;
    }

    private AbstractProcessor bindFieldNum() {
        csvMetaDetails.setFieldNum(getNumOfField());
        return this;
    }

    private AbstractProcessor bindTableHead() {
        String tableHead = getTableHead();
        int    fieldNum  = csvMetaDetails.getFieldNum();
        if (validate(tableHead, fieldNum) && csvMetaDetails.getMappingField().keySet().size() == fieldNum) {
            csvMetaDetails.setTableHead(tableHead);
            return this;
        } else {
            //TODO:数目不一致
            throw new CsvProcessorRuntimeException();
        }

    }

    /**
     * 根据类型绑定处理器
     *
     * @param <T>       the type parameter
     * @param clazzName the clazz name
     */
    protected <T extends AbstractProcessor> void bindSourceType(Class<T> clazzName) {

        if (clazzName == QueueOfUnixProcessor.class) {
            csvMetaDetails.setCsvSourceType(CsvSourceType.QUEUE_OF_UNIX);
        } else if (clazzName == QueueOfAS400Processor.class) {
            csvMetaDetails.setCsvSourceType(CsvSourceType.QUEUE_OF_AS400);
        } else if (clazzName == QuotationOfUnixProcessor.class) {
            csvMetaDetails.setCsvSourceType(CsvSourceType.QUOTATION_OF_UNIX);
        } else if (clazzName == QuotationOfAS400Processor.class) {
            csvMetaDetails.setCsvSourceType(CsvSourceType.QUOTATION_OF_AS400);
        }
    }

    public CsvMetaDetails getCsvMetaDetails() {
        return csvMetaDetails;
    }
}
