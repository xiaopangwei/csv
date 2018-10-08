package job;

import common.TradeReplayRuntimeException;
import operator.KafkaFlinkProducer;
import operator.PartitionerOfJobSet;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.util.StringUtils;

import java.util.Enumeration;
import java.util.Properties;


/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/6
 * @time 11:03 AM
 */
public abstract class AbstractTradeReplayJob<T, E> implements KeySelector<T, E> {

    protected      Properties                 properties    = null;
    private static int                        indexOfJobSet = 0;
    protected      StreamExecutionEnvironment env           = null;

    private RichMapFunction<T, T> richMapFunction;
    private RichSourceFunction<T> richSourceFunction;

    public AbstractTradeReplayJob() {
        Properties properties = loadFromConfigCenter(env);
        validateConfig();
        this.richSourceFunction = getRichSourceFunction(properties);
        this.richMapFunction = getRichMapFunction(properties);
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        indexOfJobSet++;
    }

    public abstract RichSourceFunction<T> getRichSourceFunction(Properties properties);

    public abstract RichMapFunction<T, T> getRichMapFunction(Properties properties);

    protected abstract Properties loadFromConfigCenter(StreamExecutionEnvironment env);

    protected abstract Properties loadFromNativeConfig(StreamExecutionEnvironment env);


    protected void validateConfig() {
        Enumeration names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String key   = (String) names.nextElement();
            String value = properties.getProperty(key);
            if (StringUtils.isNullOrWhitespaceOnly(value)) {
                throw new TradeReplayRuntimeException();
            }

        }
    }

    public JobExecutionResult execute() throws Exception {
        PartitionerOfJobSet   partitionerOfJobSet = new PartitionerOfJobSet(indexOfJobSet);
        FlinkKafkaProducer010 kafkaFlinkProducer  = new KafkaFlinkProducer(null, null, partitionerOfJobSet);
        env
                .addSource(richSourceFunction)
                .keyBy(this)
                .map(richMapFunction)
                .addSink(kafkaFlinkProducer);
        String jobName = properties.getProperty("jobName");
        return env.execute(jobName);

    }


}
