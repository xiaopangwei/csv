package observer.util;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 10:27 PM
 */
public class MapUtils {

    public static <K, V> MultiMap<K, V> toMultiValueMap(Map<K, List<V>> listMap) {
        MultiMap<K, V> multiMap = new MultiValueMap<>();
        for (Map.Entry<K, List<V>> item : listMap.entrySet()) {
            K       key   = item.getKey();
            List<V> value = item.getValue();
            for (V v : value) {
                multiMap.put(key, v);
            }
        }
        return multiMap;
    }
}
