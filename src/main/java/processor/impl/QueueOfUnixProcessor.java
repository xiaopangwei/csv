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
public class QueueOfUnixProcessor extends AbstractProcessor {

    private static QueueOfUnixProcessor queueOfUnixProcessor = null;

    /**
     * Gets processor.
     *
     * @return the processor
     */
    public static QueueOfUnixProcessor getProcessor() {
        if (queueOfUnixProcessor == null) {
            synchronized (QueueOfUnixProcessor.class) {
                if (queueOfUnixProcessor == null) {
                    queueOfUnixProcessor = new QueueOfUnixProcessor();
                    queueOfUnixProcessor.bindSourceType(QueueOfUnixProcessor.class);

                }
            }
        }
        return queueOfUnixProcessor;
    }

    private QueueOfUnixProcessor() {

    }

    @Override
    protected Map<Integer, String> getAddedElementsMap() {
        return new HashMap<Integer, String>() {{
            put(-1, "first");
        }};
    }

    @Override
    protected Map<Integer, Integer> getMappingFieldMap() {
        return new HashMap<Integer, Integer>(30) {{
            put(0, 1);
            put(1, 0);
            put(2, 2);
            put(3, 3);
            put(4, 4);
            put(5, 5);
            put(6, 6);
        }};
    }

    @Override
    protected int getNumOfField() {
        return 7;
    }

    @Override
    protected String getTableHead() {
        return "A,B,C,D,E,F,G";
    }


}
