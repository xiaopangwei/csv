import common.CsvProcessorRuntimeException;
import model.CsvSourceType;
import processor.AbstractProcessor;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/2
 * @time 11 :21 PM
 */
public class Client {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws CsvProcessorRuntimeException the csv processor runtime exception
     */
    public static void main(String[] args) throws CsvProcessorRuntimeException {
        AbstractProcessor processor1 = AbstractProcessor.getProcessorByType(CsvSourceType.QUEUE_OF_AS400);
        byte[]            result1    = processor1.process("0,1,23,456".getBytes());


        AbstractProcessor processor2 = AbstractProcessor.getProcessorByType(CsvSourceType.QUEUE_OF_UNIX);
        byte[]            result2    = processor2.process("0,1,23,456,789,1123,234".getBytes());


        AbstractProcessor processor3 = AbstractProcessor.getProcessorByType(CsvSourceType.QUOTATION_OF_AS400);
        byte[]            result3    = processor3.process("0,1,23,456".getBytes());

        AbstractProcessor processor4 = AbstractProcessor.getProcessorByType(CsvSourceType.QUOTATION_OF_UNIX);
        byte[]            result4    = processor4.process("0,1,23,456".getBytes());

//        AbstractProcessor processor5 = AbstractProcessor.getProcessorByType(CsvSourceType.QUOTATION_OF_UNIX);
//        byte[]            result5    = processor5.process("0,1,23,456".getBytes());
//
//
//        AbstractProcessor processor6 = AbstractProcessor.getProcessorByType(CsvSourceType.QUEUE_OF_AS400);
//        byte[]            result6    = processor6.process("0,1,23,456".getBytes());

//        processor2.process("1,23,456,789,1123,234,0".getBytes());

//        System.out.println(processor4.getCsvMetaDetails() == processor4.getCsvMetaDetails());
//        System.out.println(processor1.getCsvMetaDetails() != processor2.getCsvMetaDetails());
//        System.out.println(processor1.getCsvMetaDetails() != processor3.getCsvMetaDetails());
//        System.out.println(processor3.getCsvMetaDetails() != processor2.getCsvMetaDetails());


    }
}
