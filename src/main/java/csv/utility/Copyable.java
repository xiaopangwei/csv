package csv.utility;

import common.CsvProcessorRuntimeException;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @param <T> the type parameter
 * @author weihuang
 * @date 2018 /10/4
 * @time 2 :42 PM
 */
public interface Copyable<T> {

    /**
     * 获取从指定字段上可以连续拷贝的最多字段数目
     * 方法的用途参见{@link csv.formatter.impl.CsvFormatterNew#getMaxCopyableStep0(Object, int)}
     *
     * @param src        源数据
     * @param fieldIndex 目标CSV的字段顺序
     * @return 连续拷贝的字段数目 max copyable step 0
     * @throws CsvProcessorRuntimeException 操作CSV异常
     */
    int getMaxCopyableStep0(T src, int fieldIndex) throws CsvProcessorRuntimeException;
}
