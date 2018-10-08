package job;

import org.apache.flink.api.common.JobExecutionResult;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/6
 * @time 11:16 AM
 */
public interface StreamRecordExecutor<IN> {

    JobExecutionResult execute() throws Exception;
}

