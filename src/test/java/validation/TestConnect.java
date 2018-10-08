package validation;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.junit.Test;

import static validation.StreamStub.environment;
import static validation.StreamStub.s1;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/3
 * @time 3:26 PM
 */
public class TestConnect {

    @Test
    public void testShuffle() throws Exception {
        environment.getConfig().disableSysoutLogging();
        s1.rebalance().map(new CustomMapFunction()).setParallelism(3).addSink(new DiscardingSink<>());
        environment.execute();
    }

    @Test
    public void testPartition() throws Exception {
        environment.getConfig().disableSysoutLogging();
        s1.partitionCustom(new Partitioner<Long>() {
            @Override
            public int partition(Long aLong, int numOfPartitions) {
                return ((int) (aLong % 10)) % numOfPartitions;
            }
        }, new KeySelector<Long, Long>() {
            @Override
            public Long getKey(Long aLong) throws Exception {
                return aLong;
            }
        }).map(new CustomMapFunction()).setParallelism(5).addSink(new DiscardingSink<>());
        environment.execute();
    }

    @Test
    public void testGroupBy() throws Exception {
        s1.keyBy(new KeySelector<Long, Long>() {
            @Override
            public Long getKey(Long value) throws Exception {
                System.err.println("getKey:" + Thread.currentThread().getName() + "  " + value + " <--> " + value % 3);
                return value % 3;
            }
        }).map(new CustomMapFunction()).setParallelism(3).addSink(new DiscardingSink<>());
        environment.execute();
    }

    @Test
    public void testConnect() {

    }
}
