package processor.impl;

import processor.AbstractProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/2
 * @time 5 :25 PM
 */
public class QueueOfAS400Processor extends AbstractProcessor {

    private static QueueOfAS400Processor queueOfAS400Processor = null;

    /**
     * Gets processor.
     *
     * @return the processor
     */
    public static QueueOfAS400Processor getProcessor() {
        if (queueOfAS400Processor == null) {
            synchronized (QueueOfAS400Processor.class) {
                if (queueOfAS400Processor == null) {
                    queueOfAS400Processor = new QueueOfAS400Processor();
                    queueOfAS400Processor.bindSourceType(QueueOfAS400Processor.class);
                }
            }
        }
        return queueOfAS400Processor;
    }

    private QueueOfAS400Processor() {
    }


    @Override
    protected Map<Integer, Integer> getMappingFieldMap() {
        return new HashMap<Integer, Integer>(30) {{
            put(0, 3);
            put(1, 2);
            put(2, 1);
            put(3, 0);
        }};

    }

    @Override
    protected Map<Integer, String> getAddedElementsMap() {
        return new HashMap<Integer, String>() {{
            put(1, "second");
        }};

    }

    @Override
    protected int getNumOfField() {
        return 4;
    }

    @Override
    protected String getTableHead() {
        return "A,B,C,D";
    }

}
