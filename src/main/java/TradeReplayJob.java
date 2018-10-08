
import job.AbstractTradeReplayJob;
import model.CsvSourceType;
import operator.TradeReplayMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Properties;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/6
 * @time 11:33 AM
 */
public class TradeReplayJob extends AbstractTradeReplayJob<byte[], String> {

    private String sourceType;

    public TradeReplayJob(String sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public RichSourceFunction<byte[]> getRichSourceFunction(Properties properties) {
        return null;
    }

    @Override
    public RichMapFunction<byte[], byte[]> getRichMapFunction(Properties properties) {
        return new TradeReplayMapFunction(CsvSourceType.getTypeByName(null));
    }

    @Override
    protected Properties loadFromConfigCenter(StreamExecutionEnvironment env) {
        return null;
    }

    @Override
    protected Properties loadFromNativeConfig(StreamExecutionEnvironment env) {
        return null;
    }

    @Override
    public String getKey(byte[] value) throws Exception {
        return null;
    }

    public static void main(String[] args) throws Exception {
        TradeReplayJob job = new TradeReplayJob(null);
        job.execute();
    }

}
