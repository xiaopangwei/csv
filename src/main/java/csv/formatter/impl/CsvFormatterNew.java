package csv.formatter.impl;

import common.CsvProcessorRuntimeException;
import csv.formatter.AbstractCsvFormatter;
import csv.utility.Copyable;
import csv.utility.CsvPropertyCache;
import model.CsvMetaDetails;
import org.apache.commons.collections4.map.MultiValueMap;

import java.util.List;
import java.util.Map;

import static csv.formatter.DataFormatter.*;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/4
 * @time 1 :37 AM
 */
public class CsvFormatterNew extends AbstractCsvFormatter implements Copyable<CsvMetaDetails> {
    private static final int             INDEX_OF_START_POS   = 1;
    private static final int             INDEX_OF_COPY_LENGTH = 3;
    private static final CsvFormatterNew csvFormatter         = new CsvFormatterNew();
    private CsvFormatterNew() {

    }

    /**
     * Gets csv formatter.
     *
     * @return the csv formatter
     */
    public static CsvFormatterNew getCsvFormatter() {
        return csvFormatter;

    }

    /**
     * 查找字段映射表中连续的序列，通过这种方式可以获得批量复制连续字段,获得一个更加好的性能
     * <p>Eg:
     * <p>假设对应该CSV对应的CsvDetails中的mappingField
     * <p>如下:
     * <p> targetFieldIndex(KEY) | srcFieldIndex(VALUE)
     * <p> 0 | 1
     * <p> 1 | 2
     * <p> 2 | 3
     * <p> 3 | 0
     * <p>未优化前: <strong>分为4步,分别拷贝源CSV的1,2,3,0字段</strong>
     * <p> 优化后: <strong>分为2步,分别拷贝源CSV的（1,2,3）字段跟（0）字段</strong>
     * <p>原因是连续的目标字段对应的源CSV的字段也是连续的
     * <p>i.e:
     * <p> 如下是mappinFieldMap的结构
     * <p> 0,1
     * <p> 1,0
     * <p> 2,2
     * <p> 3,3
     * <p> 4,4
     * <p> 5,6
     * <p> 6,5
     * <p> 运行结果:
     * <ol>
     * <li> getMaxCopyableSequence0(csvMetaDetails,0) return 1 </li>
     * <li> getMaxCopyableSequence0(csvMetaDetails,1) return 1 </li>
     * <li> getMaxCopyableSequence0(csvMetaDetails,2) return 3 </li>
     * <li> getMaxCopyableSequence0(csvMetaDetails,3) return 2 </li>
     * <li> getMaxCopyableSequence0(csvMetaDetails,4) return 1 </li>
     * <li> getMaxCopyableSequence0(csvMetaDetails,5) return 1 </li>
     * <li> getMaxCopyableSequence0(csvMetaDetails,6) return 1 </li>
     * </ol>
     *
     * @param csvMetaDetails 输入CSV的元数据
     * @param kthTargetField 需要计算的目标CSV的索引序号
     * @return 从targetFieldIndex开始可以批量复制的最多的字段字数
     * @throws CsvProcessorRuntimeException 操作CSV时异常
     */
    @Override
    public int getMaxCopyableStep0(CsvMetaDetails csvMetaDetails, int kthTargetField) throws CsvProcessorRuntimeException {
        int     offset             = 0;
        boolean firstFlag          = false;
        int     copyFieldNum       = 1;
        int     previousFieldIndex = kthTargetField - 1;
        for (Map.Entry<Integer, Integer> entry : csvMetaDetails.getMappingField().entrySet()) {
            if (entry.getKey() < kthTargetField) {
                //跳过比目标索引小的KEY
                continue;
            } else {

                int actualFieldOffset = entry.getKey() - entry.getValue();
                if (!firstFlag) {
                    offset = actualFieldOffset;
                    firstFlag = true;
                } else {
                    if (offset != actualFieldOffset) {
                        return copyFieldNum;
                    } else {
                        {
                            if (previousFieldIndex + 1 != entry.getKey()) {
                                return Math.max(1, copyFieldNum);
                            } else {
                                copyFieldNum++;
                            }
                        }
                    }
                }
            }
            previousFieldIndex = entry.getKey();

        }
        return copyFieldNum;
    }

    /**
     * <p>因为同一种源CSV格式（CsvMetaDetails）相同,
     * <p>不需要对每个字段重复调用{@link #getMaxCopyableStep0(CsvMetaDetails, int)}重复计算,
     * <p>所以缓存对应关系，缓存的数据结构是:
     * <p>KEY:CsvMetaDetails,字段对应的序号
     * <p>VALUE:可以连续拷贝的最大字段数目
     *
     * @param csvMetaDetails 源CSV元数据
     * @param kthTargetField 目标字段列号
     * @return 可以连续拷贝的最大字段数目 max copyable step
     * @throws CsvProcessorRuntimeException 操作CSV时异常
     */
    public int getMaxCopyableStep(CsvMetaDetails csvMetaDetails, int kthTargetField) throws CsvProcessorRuntimeException {
        return CsvPropertyCache.getInstance().getAndPut(csvMetaDetails, kthTargetField, this);
    }


    @Override
    protected byte[] copyFields(byte[] srcByteArray, CsvMetaDetails csvMetaDetails, List<Integer> separatorPosList, MultiValueMap<Integer, Integer> multiFieldMap) {

        byte[] targetByteArray = new byte[srcByteArray.length];
        int    targetOffset    = 0;
        int    fieldNum        = separatorPosList.size() - 1;
        for (int i = 0; i < fieldNum + 1; ) {
            int leftPos = i;
            int step    = getMaxCopyableStep(csvMetaDetails, i);
            i += step;
            int rightPos      = i;
            int fieldStartPos = multiFieldMap.getCollection(leftPos).stream().skip(INDEX_OF_START_POS).findFirst().get();
            int totalSize     = 0;
            for (int j = leftPos; j < rightPos; j++) {
                int copyLength = multiFieldMap.getCollection(j).stream().skip(INDEX_OF_COPY_LENGTH).findFirst().get();
                totalSize += copyLength;
            }
            //
            if (targetOffset == 0) {
                //待拷贝的数据第一个元素不是分隔符
                if (!isSeparator(srcByteArray, fieldStartPos)) {
                    copyArray(srcByteArray, fieldStartPos, targetByteArray, targetOffset, totalSize);
                    targetOffset += totalSize;
                } else {
                    copyArray(srcByteArray, fieldStartPos + 1, targetByteArray, targetOffset, totalSize);
                    targetOffset += (totalSize - 1);
                }
            } else {
                //待拷贝的数据第一个元素不是分隔符
                if (!isSeparator(srcByteArray, fieldStartPos)) {
                    //目标数组游标指向的是分隔符

                    if (isSeparator(targetByteArray, targetOffset - 1)) {
                        copyArray(srcByteArray, fieldStartPos, targetByteArray, targetOffset, totalSize);
                        targetOffset += (totalSize);
                    }
                    //目标数组游标指向的不是分隔符
                    else {
                        fillSeparator(targetByteArray, targetOffset);
                        copyArray(srcByteArray, fieldStartPos, targetByteArray, targetOffset + 1, totalSize);
                        targetOffset += (totalSize + 1);
                    }
                }
                //待拷贝的数据第一个元素是分隔符
                else if (isSeparator(srcByteArray, fieldStartPos)) {
                    //目标数组游标指向的是分隔符
                    if (isSeparator(targetByteArray, targetOffset - 1)) {
                        copyArray(srcByteArray, fieldStartPos, targetByteArray, targetOffset - 1, totalSize);
                        targetOffset += (totalSize - 1);
                    }
                    //目标数组游标指向的不是分隔符
                    else {
                        copyArray(srcByteArray, fieldStartPos, targetByteArray, targetOffset, totalSize);
                        targetOffset += (totalSize);
                    }
                }

            }//end


        }
        return targetByteArray;
    }


}
