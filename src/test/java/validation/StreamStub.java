package validation;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/3
 * @time 3:28 PM
 */
public class StreamStub {

    public static final StreamExecutionEnvironment environment = StreamExecutionEnvironment.createLocalEnvironment();

    static {

        environment.setParallelism(2);
        environment.getConfig().disableSysoutLogging();
        environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    }

    public static final DataStreamSource<Long> s1 = environment.generateSequence(100, 199).setParallelism(1);
    public static final DataStreamSource<Long> s2 = environment.generateSequence(200, 299).setParallelism(1);
    public static final DataStreamSource<Long> s3 = environment.generateSequence(300, 399).setParallelism(1);
    public static final DataStreamSource<Long> s4 = environment.generateSequence(400, 499).setParallelism(1);
}
