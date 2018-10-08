package others;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.flink.shaded.com.google.common.collect.ArrayListMultimap;
import org.apache.flink.shaded.com.google.common.collect.ListMultimap;
import org.apache.flink.shaded.com.google.common.collect.Multimaps;
import org.junit.Test;

import java.util.Random;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/1
 * @time 7:59 PM
 */
public class TestMultiValueMap {



    @Test
    public void testMultiKeyedHashMap() {
        MultiKeyMap map = new MultiKeyMap();
        map.put(1, "123", "123", "v1");
        map.put(1, "234", "123", "v2");
        map.put(2, "456", 3L, "v3");
        map.put("123", 2, 3, "v4");
        System.out.println(map.get(1, "123", "123"));
        System.out.println(map.get("123", 2, 3));
    }

    @Test
    public void testMultiKeyMap() {
        MultiKeyMap map = new MultiKeyMap<>();
        map.put(0, 0, 0);
        map.put(1, 1, 1);
        map.put(2, 2, 2);
        map.put(3, 3, 3);
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Random  random  = new Random();
                    int     index   = random.nextInt(3);
                    Random  random2 = new Random();
                    boolean b       = random2.nextBoolean();
                    System.out.println(b + "  " + index + " " + Thread.currentThread().getName());
                    if (b) {
                        try {
                            Thread.sleep(new Random().nextInt(3) * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        map.put(index, index, index, System.currentTimeMillis());
                    } else {
                        map.get(index, index, index, System.currentTimeMillis());
                    }

                }
            }).start();
        }
        System.out.println(map.get(1, 1, 1));
        System.out.println(map.get(2, 2, 2));
        System.out.println(map.get(3, 3, 3));


    }

    @Test
    public void testMultiMaps() {
        ListMultimap listMultimap = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
        listMultimap.put(1, 2);
        listMultimap.put(2, 3);
        listMultimap.put(1, 50);
        listMultimap.put(2, 4);
        System.out.println(listMultimap.get(1));
        System.out.println(listMultimap.get(2));
    }

}
