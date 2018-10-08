package utils;

import common.CsvProcessorRuntimeException;
import org.apache.commons.collections4.map.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.ArraysUtils.*;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/2
 * @time 3 :19 PM
 */
public class CsvFormatter {


    /**
     * 查找字段映射表中连续的序列，通过这种方式可以获得批量复制可以得到一个更加好的性能
     * Eg:
     * targetFieldIndex | srcFieldIndex
     * 0 | 1
     * 1 | 2
     * 2 | 3
     * 3 | 0
     * 未优化前: <strong>分为4步分别拷贝源CSV的1，2，3，0字段</string>
     * 优化后:  <strong>分为2步 分别拷贝源CSV的（1，2，3）字段跟（0）字段
     * 原因是连续的目标字段对应的源CSV的字段也是连续的<strong>
     *
     * @param mappingField   字段映射表KEY:目标字段的索引，VALUE:目标字段的索引 <3,2>表示目标CSV的第二个字段跟源CSV的第一个字段对应
     * @param kthTargetField 需要计算的目标CSV的索引序号
     * @return 从targetFieldIndex开始可以批量复制的最多的字段字数  <p> i.e: 如下是mappinFieldMap的结构 0,1 1,0 2,2 3,3 4,4 5,6 6,5 运行结果 <ol> <li> getMaxCopyableSequence(mappinFieldMap,0) return 1 </li> <li> getMaxCopyableSequence(mappinFieldMap,1) return 1 </li> <li> getMaxCopyableSequence(mappinFieldMap,2) return 3 </li> <li> getMaxCopyableSequence(mappinFieldMap,3) return 2 </li> <li> getMaxCopyableSequence(mappinFieldMap,4) return 1 </li> <li> getMaxCopyableSequence(mappinFieldMap,5) return 1 </li> <li> getMaxCopyableSequence(mappinFieldMap,6) return 1 </li> </ol> <p>
     */
    public static int getMaxCopyableSequence(Map<Integer, Integer> mappingField, int kthTargetField) {
        int     offset             = 0;
        boolean firstFlag          = false;
        int     copyFieldNum       = 1;
        int     previousFieldIndex = kthTargetField - 1;
        for (Map.Entry<Integer, Integer> entry : mappingField.entrySet()) {
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
     * 添加新CSV中添加新列,并填充对应的内容
     *
     * @param srcByteArray           源CSV对应的数据
     * @param numOfSrcByteArrayField 源CSV中含有字段的数目
     * @param addedElement           添加到CSV的元素 KEY表示的是在目标CSV中的列序号,VALUE表示的是对应添加到CSV中的内容
     * @return 更新后的CSV数据 byte [ ]
     * @throws CsvProcessorRuntimeException CSV加工异常                                      i.e:                                      注意的是下一次插入时索引比上次多1                                      <strong>单点插入</strong>                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,{<-1,78910>}) return  78910,0,1,23,456                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,{<0,78910>})  return  0,78910,1,23,456                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,{<1,78910>})  return  0,1,78910,23,456                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,{<2,78910>})  return  0,1,23,78910,456                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,{<3,78910>})  return  0,1,23,456,78910                                      exchangeFieldOrder("0,1,23,456".getBytes(),5,{<4,78910>})  return  0,1,23,456,78910                                      <strong>多点插入</strong>                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,{<0,78910>,<1,1112>}) return  0,78910,1112,1,23,456                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,{<0,78910>,<3,1112>}) return  0,78910,1,23,1112,456
     */
    public static byte[] addNewElement(byte[] srcByteArray, int numOfSrcByteArrayField, Map<Integer, String> addedElement) throws CsvProcessorRuntimeException {
        int    addedCount  = 0;
        byte[] iteratedCsv = null;
        for (Map.Entry<Integer, String> entry : addedElement.entrySet()) {
            int    index    = entry.getKey();
            String addedStr = entry.getValue().toString();
            iteratedCsv = addNewElement(srcByteArray, numOfSrcByteArrayField + addedCount, index, addedStr);
            srcByteArray = iteratedCsv;
            addedCount++;

        }
        return iteratedCsv;
    }

    /**
     * 在指定的列中插入内容
     *
     * @param srcByteArray           源CSV对应的数据
     * @param numOfSrcByteArrayField 源CSV中含有字段的数目
     * @param insertedIndex          需要插入的列序号
     * @param addedElement           插入列上的内容
     * @return 插入新列之后的CSV结构 byte [ ]
     * @throws CsvProcessorRuntimeException CSV加工异常                                      i.e:                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,-1,78910}) return  78910,0,1,23,456                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,0,78910})  return  0,78910,1,23,456                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,1,78910})  return  0,1,78910,23,456                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,2,78910})  return  0,1,23,78910,456                                      exchangeFieldOrder("0,1,23,456".getBytes(),4,3,78910})  return  0,1,23,456,78910
     */
    public static byte[] addNewElement(byte[] srcByteArray, int numOfSrcByteArrayField, int insertedIndex, String addedElement) throws CsvProcessorRuntimeException {

        int    newLength       = srcByteArray.length + addedElement.toString().length() + 1;
        byte[] targetByteArray = new byte[newLength];
        int    separatorCount  = 0;
        int    nthSeperatorPos = 0;
        int    i               = 0;
        while (i < srcByteArray.length) {
            if (isSeparator(srcByteArray, i)) {
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
     * 根据字段顺序的映射关系获取新的结构的CSV数据
     *
     * @param srcByteArray 源CSV对应的数据
     * @param mappingField 字段顺序映射表
     * @return 目标CSV对应的内容 byte [ ]
     * @throws CsvProcessorRuntimeException CSV加工异常
     */
    public static byte[] exchangeFieldOrder(byte[] srcByteArray, final Map<Integer, Integer> mappingField) throws CsvProcessorRuntimeException {

        byte[]        targetByteArray  = new byte[srcByteArray.length];
        int           targetOffset     = 0;
        List<Integer> separatorPosList = new ArrayList<>();
        int           fieldNum         = 0;
        for (int i = 0; i < srcByteArray.length; i++) {
            if (isSeparator(srcByteArray, i)) {
                fieldNum++;
                separatorPosList.add(i);
            }
        }
        separatorPosList.add(srcByteArray.length);
        MultiValueMap<Integer, Integer> multiFieldMap = new MultiValueMap<>();
        for (Map.Entry<Integer, Integer> item : mappingField.entrySet()) {
            int targetFieldIndex = item.getKey();
            int srcFieldIndex    = item.getValue();
            multiFieldMap.put(targetFieldIndex, srcFieldIndex);
            int srcKthFieldStartPos = srcFieldIndex < 1 ? 0 : separatorPosList.get(srcFieldIndex - 1);
            int srcKthFieldEndPos   = separatorPosList.get(srcFieldIndex);
            multiFieldMap.put(targetFieldIndex, srcKthFieldStartPos);
            multiFieldMap.put(targetFieldIndex, srcKthFieldEndPos);
            multiFieldMap.put(targetFieldIndex, srcKthFieldEndPos - srcKthFieldStartPos);
        }


        for (int i = 0; i < fieldNum + 1; ) {
            int leftPos = i;
            int step    = getMaxCopyableSequence(mappingField, i);
            i += step;
            int rightPos      = i;
            int fieldStartPos = multiFieldMap.getCollection(leftPos).stream().skip(1).findFirst().get();
            int totalSize     = 0;
            for (int j = leftPos; j < rightPos; j++) {
                int copyLength = multiFieldMap.getCollection(j).stream().skip(3).findFirst().get();
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
