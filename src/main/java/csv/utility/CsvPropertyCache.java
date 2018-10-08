package csv.utility;

import model.BiKeyOfStep;
import model.CsvMetaDetails;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/4
 * @time 10 :55 AM
 */
public class CsvPropertyCache {

    private static CsvPropertyCache csvPropertyCache;
    private static final Map<BiKeyOfStep, Integer> CSV_COPY_STEP_CACHE = new ConcurrentHashMap<>();

    private CsvPropertyCache() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static CsvPropertyCache getInstance() {
        return csvPropertyCache = new CsvPropertyCache();
    }


    /**
     * 缓存对应格式CSV的拷贝步长
     * <p>KEY是组合结构:(CsvSourceType,fieldIndex)
     * <p>VALUE:对于KEY的下一次的拷贝步长
     *
     * @param csvMetaDetails 源数据的元信息
     * @param fieldIndex     目标CSV的列序号
     * @param copyable       获取拷贝步长的接口
     * @return 满足条件的拷贝步长 and put
     */
    public Integer getAndPut(CsvMetaDetails csvMetaDetails, int fieldIndex, Copyable<CsvMetaDetails> copyable) {
        BiKeyOfStep key = new BiKeyOfStep(csvMetaDetails.getCsvSourceType(), fieldIndex);
        if (CSV_COPY_STEP_CACHE.containsKey(key)) {
            return CSV_COPY_STEP_CACHE.get(key);
        } else {
            int result = copyable.getMaxCopyableStep0(csvMetaDetails, fieldIndex);
            CSV_COPY_STEP_CACHE.put(key, result);
            return result;
        }

    }


}
