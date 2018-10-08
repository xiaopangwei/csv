package csv.formatter;

import common.DataFormatRuntimeException;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @param <IN>  the type parameter
 * @param <OUT> the type parameter
 * @param <T>   the type parameter
 * @author weihuang
 * @date 2018 /10/4
 * @time 12 :53 AM
 */
public interface DataFormatter<IN, OUT, T> {

    /**
     * CSV行中添加列
     *
     * @param srcByteArray 源数据
     * @param csvDetails   源数据的元数据
     * @return 目标CSV out
     * @throws DataFormatRuntimeException 数据格式化异常
     */
    OUT addNewElement(IN srcByteArray, T csvDetails) throws DataFormatRuntimeException;

    /**
     * CSV行调整列的顺序
     *
     * @param srcByteArray 源数据
     * @param csvDetails   源数据的元数据
     * @return 目标CSV out
     * @throws DataFormatRuntimeException 数据格式化异常
     */
    OUT exchangeOrder(IN srcByteArray, T csvDetails) throws DataFormatRuntimeException;

    /**
     * 通过添加列与调整列的顺序,使得CSV文件字段顺序与对应的监察消息格式消息体对应
     *
     * @param srcByteArray 源数据
     * @param csvDetails   源数据的元数据
     * @return 目标CSV内容 out
     * @throws DataFormatRuntimeException 数据格式化异常
     */
    OUT format(IN srcByteArray, T csvDetails) throws DataFormatRuntimeException;


    /**
     * 判断数组指定位置上是否是分隔符
     *
     * @param array 输入字节数组
     * @param index 索引
     * @return true表示当前位置是分隔符 boolean
     */
    static boolean isSeparator(byte[] array, int index) {

        return (array != null && index >= 0 && index < array.length) ? array[index] == ',' : false;
    }

    /**
     * 指定位置上填充字符
     *
     * @param array 输入字节数组
     * @param index 索引
     */
    static void fillSeparator(byte[] array, int index) {
        if (array != null && index >= 0 && index < array.length) {
            array[index] = ',';
        }
    }

    /**
     * 字节数组之间的相互拷贝
     * <p>
     * <p>注意:这里调整一下，当拷贝的数组长度出现越界时，此时不抛出异常，而是提前判断，使得拷贝长度比合法的长度小
     *
     * @param srcByteArray    源数据
     * @param srcOffset       源数组索引
     * @param targetByteArray 目标数据
     * @param targetOffset    目标数组索引
     * @param length          复制字节的长度
     */
    static void copyArray(byte[] srcByteArray, int srcOffset, byte[] targetByteArray, int targetOffset, int length) {
        int newSrcLength    = srcByteArray.length - srcOffset;
        int newTargetLength = targetByteArray.length - targetOffset;
        if (srcOffset + length > srcByteArray.length || targetOffset + length > targetByteArray.length) {
            System.arraycopy(srcByteArray, srcOffset, targetByteArray, targetOffset, Math.min(newSrcLength, newTargetLength));
        } else {
            System.arraycopy(srcByteArray, srcOffset, targetByteArray, targetOffset, length);
        }
    }
}
