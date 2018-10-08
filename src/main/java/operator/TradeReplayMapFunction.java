package operator;

import model.CsvSourceType;
import org.apache.flink.api.common.functions.RichMapFunction;
import processor.AbstractProcessor;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/6
 * @time 10:43 PM
 */
public class TradeReplayMapFunction extends RichMapFunction<byte[], byte[]> {
    private CsvSourceType csvSourceType;

    public TradeReplayMapFunction(CsvSourceType csvSourceType) {
        this.csvSourceType = csvSourceType;
    }

    @Override
    public byte[] map(byte[] value) throws Exception {
        AbstractProcessor processor = AbstractProcessor.getProcessorByType(csvSourceType);
        return processor.process(value);
    }
}
