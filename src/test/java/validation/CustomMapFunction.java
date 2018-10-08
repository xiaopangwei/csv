package validation;

import org.apache.flink.api.common.functions.RichMapFunction;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/3
 * @time 3:47 PM
 */
public class CustomMapFunction extends RichMapFunction<Long,Long>{
    @Override
    public Long map(Long aLong) throws Exception {
//        System.err.println(Thread.currentThread().getName()+"<-->"+aLong);
        return aLong;
    }
}
