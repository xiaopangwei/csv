package operator;

import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkKafkaPartitioner;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/4
 * @time 4 :09 PM
 */
public class PartitionerOfJobSet extends FlinkKafkaPartitioner<byte[]> {


    private int indexOfJobSet;

    /**
     * Instantiates a new Partitioner of job set.
     *
     * @param indexOfJobSet the index of job set
     */
    public PartitionerOfJobSet(int indexOfJobSet) {
        this.indexOfJobSet = indexOfJobSet;
    }

    @Override
    public void open(int parallelInstanceId, int parallelInstances) {
        parallelInstanceId = indexOfJobSet;
    }

    @Override
    public int partition(byte[] record, byte[] key, byte[] value, String targetTopic, int[] partitions) {
        int maxPartitionIndex = partitions.length - 1;
        return partitions[indexOfJobSet % maxPartitionIndex];

    }
}