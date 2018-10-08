package csv.formatter;

import common.CsvProcessorRuntimeException;
import common.DataFormatRuntimeException;
import model.CsvMetaDetails;
import org.apache.commons.collections4.map.MultiValueMap;
import utils.ArraysUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static csv.formatter.DataFormatter.*;


/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/4
 * @time 1 :03 AM
 */
public abstract class AbstractCsvFormatter implements DataFormatter<byte[], byte[], CsvMetaDetails> {


    /**
     * <p>获取下一步复制要用到的参照表，是一个(key,(value1,value2,value3...))的结构
     * <p>存储使用的是MultiValueMap结构,具体使用方法参见{@link MultiValueMap}
     * <p>KEY:目标CSV中第K个字段
     * <p>VALUE:
     * <p>value0:M,跟目标CSV中第K个字段对应的第M个字段
     * <p>value1:跟目标CSV中第K个字段对应的第M个字段在源CSV中的起点,该位置上可能是一个分隔符
     * <p>value2:跟目标CSV中第K个字段对应的第M个字段在源CSV中的终点
     * <p>value3:跟目标CSV中第K个字段对应的第M个字段,起点与终点之间的长度
     * <p>i.e (1->(4,7,10,3))
     * <p>1->表示的是目标CSV的第2个字段
     * <p>4->对应的是源CSV的第5个字段
     * <p>7->源CSV的第5个字段起始位置是7
     * <p>10->终点位置是10
     * <p>3->需要复制3个字节
     *
     * @param srcByteArray   源CSV数据
     * @param csvMetaDetails CSV的元信息
     * @return 复制对应字段时借助的中间表结构 field reference table
     */
    protected MultiValueMap<Integer, Integer> getFieldReferenceTable(byte[] srcByteArray, CsvMetaDetails csvMetaDetails) {
        MultiValueMap<Integer, Integer> multiFieldMap    = new MultiValueMap<>();
        List<Integer>                   separatorPosList = getSeparatorList(srcByteArray);
        for (Map.Entry<Integer, Integer> item : csvMetaDetails.getMappingField().entrySet()) {
            int targetFieldIndex = item.getKey();
            int srcFieldIndex    = item.getValue();
            multiFieldMap.put(targetFieldIndex, srcFieldIndex);
            int srcKthFieldStartPos = srcFieldIndex < 1 ? 0 : separatorPosList.get(srcFieldIndex - 1);
            int srcKthFieldEndPos   = separatorPosList.get(srcFieldIndex);
            multiFieldMap.put(targetFieldIndex, srcKthFieldStartPos);
            multiFieldMap.put(targetFieldIndex, srcKthFieldEndPos);
            multiFieldMap.put(targetFieldIndex, srcKthFieldEndPos - srcKthFieldStartPos);
        }
        return multiFieldMap;
    }

    /**
     * 获得CSV行中每个分隔符的具体位置
     *
     * @param srcByteArray 源CSV数据
     * @return 关于分隔符的列表  <p>i.e <p>(第0个分隔符位置,第1个分隔符位置,第2个分隔符位置...) <p>为了保持返回的列表数目与字段数目相同，将最后一个位置的分隔符设置到行尾位置上
     */
    protected List<Integer> getSeparatorList(byte[] srcByteArray) {
        List<Integer> separatorPosList = new ArrayList<>();
        int           fieldNum         = 0;
        for (int i = 0; i < srcByteArray.length; i++) {
            if (isSeparator(srcByteArray, i)) {
                fieldNum++;
                separatorPosList.add(i);
            }
        }
        separatorPosList.add(srcByteArray.length);
        return separatorPosList;

    }


    /**
     * 添加新CSV中添加新列,并填充对应的内容
     * <p>i.e:
     * <p><strong>注意的是下一次插入时索引比上次多1</strong>
     * <p><strong>单点插入</strong>
     * <p> 以下实例中:第一个参数是CSV源数据,第二个参数是CSV中总字段数目，第三个参数是插入的字段字典（插入点索引,插入的内容）
     * <p>addNewElement("0,1,23,456".getBytes(),4,{<-1,78910>}) return  78910,0,1,23,456
     * <p>addNewElement("0,1,23,456".getBytes(),4,{<0,78910>})  return  0,78910,1,23,456
     * <p>addNewElement("0,1,23,456".getBytes(),4,{<1,78910>})  return  0,1,78910,23,456
     * <p>addNewElement("0,1,23,456".getBytes(),4,{<2,78910>})  return  0,1,23,78910,456
     * <p>addNewElement("0,1,23,456".getBytes(),4,{<3,78910>})  return  0,1,23,456,78910
     * <p>addNewElement("0,1,23,456".getBytes(),5,{<4,78910>})  return  0,1,23,456,78910
     * <p><strong>多点插入</strong>
     * <p>addNewElement("0,1,23,456".getBytes(),4,{<0,78910>,<1,1112>}) return  0,78910,1112,1,23,456
     * <p>addNewElement("0,1,23,456".getBytes(),4,{<0,78910>,<3,1112>}) return  0,78910,1,23,1112,456
     *
     * @param srcByteArray 源CSV对应的数据
     * @param csvMetaDetails   源CSV中的元信息
     * @return 更新后的CSV数据
     * @throws CsvProcessorRuntimeException CSV处理异常
     */
    @Override
    public byte[] addNewElement(byte[] srcByteArray, CsvMetaDetails csvMetaDetails) throws CsvProcessorRuntimeException {
        int    addedCount  = 0;
        byte[] iteratedCsv = null;
        for (Map.Entry<Integer, String> entry : csvMetaDetails.getAddedElements().entrySet()) {
            int    index    = entry.getKey();
            String addedStr = entry.getValue().toString();
            iteratedCsv = addNewElement(srcByteArray, csvMetaDetails.getFieldNum() + addedCount, index, addedStr);
            srcByteArray = iteratedCsv;
            addedCount++;

        }
        return iteratedCsv;
    }


    /**
     * 在指定的CSV列中插入内容
     * <p>i.e:
     * <p>addNewElement("0,1,23,456".getBytes(),4,-1,78910}) return  78910,0,1,23,456
     * <p>addNewElement("0,1,23,456".getBytes(),4,0,78910})  return  0,78910,1,23,456
     * <p>addNewElement("0,1,23,456".getBytes(),4,1,78910})  return  0,1,78910,23,456
     * <p>addNewElement("0,1,23,456".getBytes(),4,2,78910})  return  0,1,23,78910,456
     * <p>addNewElement("0,1,23,456".getBytes(),4,3,78910})  return  0,1,23,456,78910
     *
     * @param srcByteArray           源CSV对应的数据
     * @param numOfSrcByteArrayField 源CSV中含有字段的数目
     * @param insertedIndex          需要插入的列序号
     * @param addedElement           插入列上的内容
     * @return 插入新列之后的CSV结构
     * @throws CsvProcessorRuntimeException CSV加工异常
     */

    private byte[] addNewElement(byte[] srcByteArray, int numOfSrcByteArrayField, int insertedIndex, String addedElement) throws CsvProcessorRuntimeException {

        int    newLength       = srcByteArray.length + addedElement.toString().length() + 1;
        byte[] targetByteArray = new byte[newLength];
        int    separatorCount  = 0;
        int    nthSeperatorPos = 0;
        int    i               = 0;
        while (i < srcByteArray.length) {
            if (ArraysUtils.isSeparator(srcByteArray, i)) {
                separatorCount++;
            }
            if (separatorCount == insertedIndex + 1) {
                nthSeperatorPos = i;
                break;
            }
            i++;
        }
        //System.out.println("kthSeperatorPos" + nthSeperatorPos + " separatorCount" + separatorCount);
        byte[] addedBytes        = addedElement.toString().getBytes();
        int    addedLengthInByte = addedBytes.length;
        //第一列前添加
        if (insertedIndex < 0) {

            copyArray(addedBytes, 0, targetByteArray, 0, addedLengthInByte);
            fillSeparator(targetByteArray, addedLengthInByte);
            copyArray(srcByteArray, 0, targetByteArray, addedLengthInByte + 1, srcByteArray.length);
        }
        //最后一列追加
        else if (insertedIndex + 1 >= numOfSrcByteArrayField) {
            fillSeparator(targetByteArray, srcByteArray.length);
            copyArray(addedBytes, 0, targetByteArray, srcByteArray.length + 1, addedLengthInByte);
            copyArray(srcByteArray, 0, targetByteArray, 0, srcByteArray.length);
        } else {
            int targetOffset = 0;
            //复制0-separatorPosList[insertedElement]
            copyArray(srcByteArray, 0, targetByteArray, targetOffset, nthSeperatorPos + 1);
            targetOffset += (nthSeperatorPos + 1);

            //insert (addedElement,)
            copyArray(addedBytes, 0, targetByteArray, targetOffset, addedBytes.length);
            targetOffset += (addedLengthInByte);
            fillSeparator(targetByteArray, targetOffset);
            targetOffset++;

            //复制separatorPosList[insertedElement]-length
            copyArray(srcByteArray, nthSeperatorPos + 1, targetByteArray, targetOffset, srcByteArray.length - (nthSeperatorPos + 1));

        }
        return targetByteArray;
    }


    /**
     * 高效的拷贝对应位置上的字段的方法
     *
     * @param srcByteArray     源CSV对应的数据
     * @param csvMetaDetails   源CSV的元数据
     * @param separatorPosList 分隔符位置的列表
     * @param multiFieldMap    拷贝需要用到的参照表
     * @return 目标CSV对应的字节数组
     */
    protected abstract byte[] copyFields(byte[] srcByteArray,
                                         CsvMetaDetails csvMetaDetails,
                                         List<Integer> separatorPosList,
                                         MultiValueMap<Integer, Integer> multiFieldMap);

    @Override
    public byte[] exchangeOrder(byte[] src, CsvMetaDetails csvMetaDetails) throws DataFormatRuntimeException {
        List<Integer>                   separatorPosList = getSeparatorList(src);
        MultiValueMap<Integer, Integer> multiFieldMap    = getFieldReferenceTable(src, csvMetaDetails);
        return copyFields(src, csvMetaDetails, separatorPosList, multiFieldMap);
    }

    @Override
    public byte[] format(byte[] src, CsvMetaDetails csvMetaDetails) throws DataFormatRuntimeException {
        Map<Integer, Integer> mappingFields      = csvMetaDetails.getMappingField();
        Map<Integer, String>  addedElements      = csvMetaDetails.getAddedElements();
        byte[]                exchangedByteArray = null;
        byte[]                resultByteArray    = null;
        if (mappingFields != null && !mappingFields.isEmpty()) {
            exchangedByteArray = exchangeOrder(src, csvMetaDetails);
        } else {
            exchangedByteArray = src;
        }
        if (addedElements != null && !addedElements.isEmpty()) {
            resultByteArray = addNewElement(exchangedByteArray, csvMetaDetails);
        } else {
            resultByteArray = src;
        }
        return resultByteArray;

    }


}
