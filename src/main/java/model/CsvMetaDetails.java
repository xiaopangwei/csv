package model;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/4
 * @time 2 :24 PM
 */
public class CsvMetaDetails implements Serializable {
    private static final long     serialVersionUID = 1765417032641042158L;
    /**
     * CSV文件类型
     */

    private CsvSourceType         csvSourceType = null;
    /**
     * 映射字段表<KEY:目标表的索引,VALUE:源表的索引>
     */

    private Map<Integer, Integer> mappingField  = null;
    /**
     * 映射字段表<KEY:目标表的索引,VALUE:插入的内容>
     */

    private Map<Integer, String>  addedElements = null;
    /**
     * 表头行,含有列信息
     */
    private String                tableHead     = null;
    /**
     * CSV数据中的字段个数
     */
    private int                   fieldNum      = 0;

    /**
     * Gets field num.
     *
     * @return the field num
     */
    public int getFieldNum() {
        return fieldNum;
    }

    /**
     * Sets field num.
     *
     * @param fieldNum the field num
     */
    public void setFieldNum(int fieldNum) {
        this.fieldNum = fieldNum;
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
     * Gets mapping field.
     *
     * @return the mapping field
     */
    public Map<Integer, Integer> getMappingField() {
        return mappingField;
    }

    /**
     * Sets mapping field.
     *
     * @param mappingField the mapping field
     */
    public void setMappingField(Map<Integer, Integer> mappingField) {
        this.mappingField = mappingField;
    }

    /**
     * Gets added elements.
     *
     * @return the added elements
     */
    public Map<Integer, String> getAddedElements() {
        return addedElements;
    }

    /**
     * Sets added elements.
     *
     * @param addedElements the added elements
     */
    public void setAddedElements(Map<Integer, String> addedElements) {
        this.addedElements = addedElements;
    }

    /**
     * Gets table head.
     *
     * @return the table head
     */
    public String getTableHead() {
        return tableHead;
    }

    /**
     * Sets table head.
     *
     * @param tableHead the table head
     */
    public void setTableHead(String tableHead) {
        this.tableHead = tableHead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CsvMetaDetails that = (CsvMetaDetails) o;

        if (fieldNum != that.fieldNum) {
            return false;
        }
        if (csvSourceType != that.csvSourceType) {
            return false;
        }
        if (mappingField != null ? !mappingField.equals(that.mappingField) : that.mappingField != null) return false;
        if (addedElements != null ? !addedElements.equals(that.addedElements) : that.addedElements != null) return false;
        return tableHead != null ? tableHead.equals(that.tableHead) : that.tableHead == null;
    }

    @Override
    public int hashCode() {
        int result = csvSourceType != null ? csvSourceType.hashCode() : 0;
        result = 31 * result + (mappingField != null ? mappingField.hashCode() : 0);
        result = 31 * result + (addedElements != null ? addedElements.hashCode() : 0);
        result = 31 * result + (tableHead != null ? tableHead.hashCode() : 0);
        result = 31 * result + fieldNum;
        return result;
    }

    @Override
    public String toString() {
        return "CsvMetaDetails{" +
                "csvSourceType=" + csvSourceType +
                ", mappingField=" + mappingField +
                ", addedElements=" + addedElements +
                ", tableHead='" + tableHead + '\'' +
                ", fieldNum=" + fieldNum +
                '}';
    }
}
