package model;

import common.TradeReplayRuntimeException;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018 /10/2
 * @time 12 :05 PM
 */
public enum CsvSourceType {

    //输入的CSV格式

    /**
     * UNIX系统产生的交易重演队列文件
     */
    QUEUE_OF_UNIX,
    /**
     * UNIX系统产生的交易重演逐笔行情文件
     */
    QUOTATION_OF_UNIX,
    /**
     * AS400系统产生的交易重演队列文件
     */
    QUEUE_OF_AS400,
    /**
     * AS400系统产生的交易重演逐笔行情文件
     */
    QUOTATION_OF_AS400;

    public static CsvSourceType getTypeByName(String type) {
        for (CsvSourceType item : CsvSourceType.values()) {
            if (item.name().equals(type)) {
                return item;
            }
        }
        throw new TradeReplayRuntimeException();
    }

}
