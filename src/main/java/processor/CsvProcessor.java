package processor;

import common.CsvProcessorRuntimeException;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @param <T> the type parameter
 * @author weihuang
 * @date 2018 /10/2
 * @time 11 :50 AM
 */
public interface CsvProcessor<T> {
    /**
     * 处理源CSV行,返回的是传给下游算子的结果
     *
     * @param csvRow 输入的CSV行
     * @return 传给下游算子的结果
     * @throws CsvProcessorRuntimeException CSV处理异常
     */
    byte[] process(byte[] csvRow) throws CsvProcessorRuntimeException;

    /**
     * 使得输入的CSV结构与对应消息结构的顺序匹配
     *
     * @param csvRow 输入的CSV行
     * @return 对应消息体的二进制序列
     * @throws CsvProcessorRuntimeException CSV处理异常
     */
    byte[] fit(byte[] csvRow) throws CsvProcessorRuntimeException;

    /**
     * 构造监察消息对象
     *
     * @param csvRow 监察消息的消息体对应的二进制，一般是调用#{@link CsvProcessor#fit(byte[])}的返回值
     * @return 监察消息对象
     * @throws CsvProcessorRuntimeException CSV处理异常
     */
    T buildRecord(byte[] csvRow) throws CsvProcessorRuntimeException;

    /**
     * 获取落地的二进制序列
     *
     * @param record 调用#{@link CsvProcessor#buildRecord(byte[])}的返回值
     * @return 经过加工处理传递到下游的二进制序列
     * @throws CsvProcessorRuntimeException CSV处理异常
     */
    byte[] getSinkResult(T record) throws CsvProcessorRuntimeException;

    /**
     * 判断当前处理的行与CSV结构是否匹配
     *
     * @param tableHeadRow  表头
     * @param fieldNumInRow CSV每行中的列数
     * @return true表示合理，否则非法
     */
    default boolean validate(String tableHeadRow, int fieldNumInRow) {
        return tableHeadRow == null ? true : tableHeadRow.split(",", -1).length == fieldNumInRow;
    }


}
