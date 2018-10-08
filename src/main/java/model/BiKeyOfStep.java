package model;

import java.io.Serializable;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/4
 * @time 2 :23 AM
 */
public class BiKeyOfStep implements Serializable {
    private static final long serialVersionUID = 4219887485809005435L;
    private CsvSourceType csvSourceType;
    private int           fieldIndex;

    /**
     * Instantiates a new Bi key of step.
     *
     * @param csvSourceType the csv source type
     * @param fieldIndex    the field index
     */
    public BiKeyOfStep(CsvSourceType csvSourceType, int fieldIndex) {
        this.csvSourceType = csvSourceType;
        this.fieldIndex = fieldIndex;
    }

    /**
     * Gets csv source type.
     *
     * @return the csv source type
     */
    public CsvSourceType getCsvSourceType() {
        return csvSourceType;
    }

    /**
     * Sets csv source type.
     *
     * @param csvSourceType the csv source type
     */
    public void setCsvSourceType(CsvSourceType csvSourceType) {
        this.csvSourceType = csvSourceType;
    }

    /**
     * Gets field index.
     *
     * @return the field index
     */
    public int getFieldIndex() {
        return fieldIndex;
    }

    /**
     * Sets field index.
     *
     * @param fieldIndex the field index
     */
    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BiKeyOfStep that = (BiKeyOfStep) o;

        if (fieldIndex != that.fieldIndex) {
            return false;
        }
        return csvSourceType == that.csvSourceType;
    }

    @Override
    public int hashCode() {
        int result = csvSourceType.hashCode();
        result = 31 * result + fieldIndex;
        return result;
    }

    @Override
    public String toString() {
        return "BiKeyOfStep{" +
                "csvSourceType=" + csvSourceType +
                ", fieldIndex=" + fieldIndex +
                '}';
    }
}
