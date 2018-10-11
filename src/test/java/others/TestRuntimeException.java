package others;

import observer.util.PatternUtils;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
//        List<List<Integer>> result = RangeStrategy.subList(Arrays.asList(1, 2, 3, 4, 5, 6), 3);
//        System.out.println(result);
    }

    @Test
    public void testPattern() {
        String  pattern = "^([a-z]+)[_]([a-z]+)(\\d{1,3})[_](\\d{1,3})(.csv)$";
        Pattern r       = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher("queue_equitymsg178_128.csv");
        if (m.find()) {
            System.out.println(m.group(0));
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            System.out.println(m.group(3));
            System.out.println(m.group(4));
            System.out.println(m.group(5));
        }
    }

    @Test
    public void testPattern2() {
        String sourceName = "quot_equitymsg1_4.csv";
        System.out.println(PatternUtils.getDxsPort(sourceName));
        System.out.println(PatternUtils.getProcessId(sourceName));
        System.out.println(PatternUtils.getSourceType(sourceName));

    }
}
