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
public class QuotationOfAS400Processor extends AbstractProcessor {
    private static QuotationOfAS400Processor quotationOfAS400Processor = null;

    /**
     * Gets processor.
     *
     * @return the processor
     */
    public static QuotationOfAS400Processor getProcessor() {
        if (quotationOfAS400Processor == null) {
            synchronized (QuotationOfAS400Processor.class) {
                if (quotationOfAS400Processor == null) {
                    quotationOfAS400Processor = new QuotationOfAS400Processor();
                    quotationOfAS400Processor.bindSourceType(QuotationOfAS400Processor.class);
                }
            }
        }
        return quotationOfAS400Processor;
    }

    private QuotationOfAS400Processor() {

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
            put(2, "third");
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
