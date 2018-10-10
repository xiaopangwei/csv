package others;

import observer.impl.RangeStrategy;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 8:17 PM
 */
public class TestRuntimeException {
    @Test
    public void testException() throws Exception {
        try {
            throw new Exception("huangwei");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("after1");

    }

    @Test
    public void testRunException() {

        throw new RuntimeException("huangwei");

    }

    @Test
    public void testMap() {
        File             file1 = new File("/aaa");
        File             file2 = new File("/aaa");
        File             file3 = new File("/bbb");
        File             file4 = new File("/bbb");
        Collection<File> list1 = Arrays.asList(file1, file2, file3, file4);
        int              index = 2;
        System.out.println(list1);
        MultiMap<Integer, File> multiMap = new MultiValueMap<>();
        Map<Integer, List<File>> listMap =
                list1.stream().collect(Collectors.groupingBy(i -> i.hashCode() % index));
        for (Map.Entry<Integer, List<File>> item : listMap.entrySet()) {
            Integer    key   = item.getKey();
            List<File> value = item.getValue();
            for (File v : value) {
                multiMap.put(key, v);
            }
        }
        System.out.println(multiMap);

    }

    @Test
    public void testSubList() {
        List<List<Integer>> result = RangeStrategy.subList(Arrays.asList(1, 2, 3, 4, 5, 6), 3);
        System.out.println(result);
    }
}
