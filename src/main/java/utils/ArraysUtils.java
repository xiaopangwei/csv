package utils;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/2
 * @time 11 :24 PM
 */
public class ArraysUtils {
    /**
     * 判断数组指定位置上是否是分隔符
     *
     * @param array 输入字节数组
     * @param index 索引
     * @return true表示当前位置是分隔符 boolean
     */
    public static boolean isSeparator(byte[] array, int index) {

        return (array != null && index >= 0 && index < array.length) ? array[index] == ',' : false;
    }

    /**
     * 指定位置上填充字符
     *
     * @param array 输入字节数组
     * @param index 索引
     */
    public static void fillSeparator(byte[] array, int index) {
        if (array != null && index >= 0 && index < array.length) {
            array[index] = ',';
        }
    }

    /**
     * 字节数组之间的相互拷贝
     *
     * @param srcByteArray    源数据
     * @param srcOffset       源数组索引
     * @param targetByteArray 目标数据
     * @param targetOffset    目标数组索引
     * @param length          复制字节的长度
     */
    public static void copyArray(byte[] srcByteArray, int srcOffset, byte[] targetByteArray, int targetOffset, int length) {
        int newSrcLength    = srcByteArray.length - srcOffset;
        int newTargetLength = targetByteArray.length - targetOffset;
        if (srcOffset + length > srcByteArray.length || targetOffset + length > targetByteArray.length) {
            System.arraycopy(srcByteArray, srcOffset, targetByteArray, targetOffset, Math.min(newSrcLength,newTargetLength));
        }else {
            System.arraycopy(srcByteArray, srcOffset, targetByteArray, targetOffset, length);
        }


    }

}
