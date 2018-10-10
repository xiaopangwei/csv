package observer.impl;

import observer.SourceFileDispatcherStrategy;
import observer.exception.DispatchException;
import observer.util.MapUtils;
import org.apache.commons.collections4.MultiMap;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 11:16 PM
 */
public abstract class AbstractHashStrategy implements SourceFileDispatcherStrategy<Integer, File> {
    @Override
    public MultiMap<Integer, File> choose(int numOfSubJob, List<File> fileList) throws DispatchException {
        Map<Integer, List<File>> listMap = fileList
                .stream()
                .collect(Collectors.groupingBy(file -> getHashKey(file) % numOfSubJob));

        return MapUtils.toMultiValueMap(listMap);
    }

    protected abstract int getHashKey(File file);
}
