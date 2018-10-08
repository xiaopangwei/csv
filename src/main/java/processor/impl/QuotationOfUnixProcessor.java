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
public class QuotationOfUnixProcessor extends AbstractProcessor {
    private static QuotationOfUnixProcessor quotationOfUnixProcessor = null;

    /**
     * Gets processor.
     *
     * @return the processor
     */
    public static QuotationOfUnixProcessor getProcessor() {
        if (quotationOfUnixProcessor == null) {
            synchronized (QuotationOfUnixProcessor.class) {
                if (quotationOfUnixProcessor == null) {
                    quotationOfUnixProcessor = new QuotationOfUnixProcessor();
                    quotationOfUnixProcessor.bindSourceType(QuotationOfUnixProcessor.class);
                }
            }
        }
        return quotationOfUnixProcessor;

    }

    private QuotationOfUnixProcessor() {

    }

    @Override
    protected Map<Integer, Integer> getMappingFieldMap() {
        return new HashMap<Integer, Integer>(30) {{
            put(0, 2);
            put(1, 1);
            put(2, 0);
            put(3, 3);
        }};
    }

    @Override
    protected Map<Integer, String> getAddedElementsMap() {
        return new HashMap<Integer, String>() {{
            put(10, "last");
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
