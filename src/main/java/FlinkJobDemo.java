import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkFixedPartitioner;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /9/28
 * @time 7 :29 PM
 */
public class FlinkJobDemo {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        environment.getConfig().disableSysoutLogging();
        environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<Long> s1 = environment.generateSequence(0, 100).setParallelism(4);

        DataStreamSource<Long> s2 = environment.generateSequence(100, 210).setParallelism(1);

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.157.133:9092");
        properties.put("acks", "1");
        properties.put("group.id", "100");
        properties.put("max.in.flight.requests.per.connection", "1");


//        KafkaFlinkProducer producer = new KafkaFlinkProducer("topic-test", properties, new FlinkFixedPartitioner<>());
        s1.keyBy(new KeySelector<Long, Long>() {
            @Override
            public Long getKey(Long value) throws Exception {
                return value;
            }
        }).map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long value) throws Exception {
                return value;
            }
        })
                .addSink(new FlinkKafkaProducer010<Long>("topic-test", new SerializationSchema<Long>() {
                    @Override
                    public byte[] serialize(Long element) {
                        return String.valueOf(element).getBytes();
                    }
                }, properties,new FlinkFixedPartitioner<>())).setParallelism(4);

//        DataStream<Long> s3=s1.connect(s2).map(new CoMapFunction<Long, Long, Long>() {
//            @Override
//            public Long map1(Long value) throws Exception {
//                return value;
//            }
//
//            @Override
//            public Long map2(Long value) throws Exception {
//                return value;
//            }
//        });
//        s3.map(new MapFunction<Long, Long>() {
//            @Override
//            public Long map(Long aLong) throws Exception {
//                Thread.sleep(1000);
//                return aLong+1;
//            }
//        }).print();
//        s2.map(new MapFunction<Long, Long>() {
//            @Override
//            public Long map(Long aLong) throws Exception {
//                Thread.sleep(2000);
//                return aLong+2;
//            }
//        }).print();


//        ConnectedStreams<Long,Long> connectedStream=s1.connect(s2);
//        connectedStream.flatMap(new CoFlatMapFunction<Long, Long, Long>() {
//            @Override
//            public void flatMap1(Long value, Collector<Long> out) throws Exception {
//                                System.out.print("ThreadId["+Thread.currentThread().getName()
//                        +"] subTaskId:["
//                        +"] value=["+String.valueOf(value)+"]\n");
//                out.collect(value);
//            }
//
//            @Override
//            public void flatMap2(Long value, Collector<Long> out) throws Exception {
//                                System.out.print("ThreadId["+Thread.currentThread().getName()
//                        +"] subTaskId:["
//                        +"] value=["+String.valueOf(value)+"]\n");
//                out.collect(value);
//            }
//        });
//        s1.partitionCustom(new Partitioner<Long>() {
//            @Override
//            public int partition(Long aLong, int i) {
//                System.out.println("partition aLong="+aLong+" i="+i);
//                return aLong.intValue()%i;
//            }
//        }, new KeySelector<Long, Long>() {
//            @Override
//            public Long getKey(Long aLong) throws Exception {
//                return aLong;
//            }
//        }).printToErr();
//        DataStreamSource<Long>     s2          = environment.generateSequence(101, 200);
//        DataStream<Long> unionedStream=s1.union(s2);
//        unionedStream.flatMap(new RichFlatMapFunction<Long, Long>() {
//            @Override
//            public void flatMap(Long aLong, Collector<Long> collector) throws Exception {
//                System.out.print("ThreadId["+Thread.currentThread().getName()
//                        +"] subTaskId:["+getRuntimeContext().getIndexOfThisSubtask()
//                        +"] value=["+String.valueOf(aLong)+"]\n");
//                collector.collect(aLong);
//            }
//        }).print();
        Logger.getRootLogger().setLevel(Level.OFF);
        System.out.println(environment.getExecutionPlan());
        //JobExecutionResult result= environment.execute("UNION");
        environment.setParallelism(4);
        environment.execute();


    }
}
