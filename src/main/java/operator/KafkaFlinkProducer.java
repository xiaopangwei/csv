package operator;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkKafkaPartitioner;
import org.apache.flink.streaming.util.serialization.SerializationSchema;

import java.util.Properties;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/4
 * @time 3 :27 PM
 */
public class KafkaFlinkProducer extends FlinkKafkaProducer010<byte[]> {


    /**
     * Instantiates a new Kafka flink producer.
     *
     * @param topicId           the topic id
     * @param producerConfig    the producer config
     * @param customPartitioner the custom partitioner
     */
    public KafkaFlinkProducer(String topicId,
                              Properties producerConfig,
                              FlinkKafkaPartitioner<byte[]> customPartitioner) {

        super(topicId, new CustomSerializationOfByte(), producerConfig, customPartitioner);
    }


    /**
     * The type Custom serialization of byte.
     */
    static class CustomSerializationOfByte implements SerializationSchema<byte[]> {

        @Override
        public byte[] serialize(byte[] element) {
            return element;
        }
    }


}


