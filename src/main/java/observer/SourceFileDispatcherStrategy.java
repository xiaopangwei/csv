package observer;

import observer.exception.DispatchException;
import org.apache.commons.collections4.MultiMap;

import java.io.IOException;
import java.util.List;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 8:42 PM
 */
public interface SourceFileDispatcherStrategy<T, E> {

    static final String PREFIX = "sub-job-";
    static final String SUFFIX = ".result";

    MultiMap<T, E> choose(int numOfSubJob, List<E> fileList) throws DispatchException, IOException;

}
