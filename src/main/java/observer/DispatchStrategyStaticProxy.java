package observer;

import observer.exception.DispatchException;
import observer.exception.DispatchRuntimeException;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 9:09 PM
 */
public class DispatchStrategyStaticProxy implements SourceFileDispatcherStrategy<Integer, File> {

    private SourceFileDispatcherStrategy<Integer, File> strategy;

    public <T extends SourceFileDispatcherStrategy> DispatchStrategyStaticProxy(String strategyClazz) {
        try {
            Class<T>                                    tClass   = (Class<T>) Class.forName(strategyClazz);
            SourceFileDispatcherStrategy<Integer, File> instance = tClass.newInstance();
            this.strategy = instance;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public MultiMap<Integer, File> choose(int numOfSubJob, List<File> fileList) throws DispatchException, IOException {
        validateArgument(numOfSubJob, fileList);
        MultiMap<Integer, File> result = strategy.choose(numOfSubJob, fileList);
        saveDispatchResult(numOfSubJob, fileList.get(0).getParentFile(), result);
        return result;
    }

    protected void saveDispatchResult(int numOfSubJob, File resultPath, MultiMap<Integer, File> resultMap) throws IOException {

        if (!resultPath.isDirectory()) {
            throw new DispatchRuntimeException();
        } else {
            String parent = resultPath.getParent();

            for (int i = 0; i < numOfSubJob; i++) {
                StringBuilder ithResultFilePath = new StringBuilder().append(parent)
                        .append(File.separator)
                        .append(PREFIX)
                        .append(i)
                        .append(SUFFIX);
                File ithResultFile = new File(ithResultFilePath.toString());
                if (resultMap.containsKey(i)) {
                    FileUtils.touch(ithResultFile);

                    FileUtils.write(ithResultFile, resultMap.get(i).toString());


                } else {
                    FileUtils.touch(ithResultFile);
                }
            }

        }
    }

    protected void validateArgument(int numOfSubJob, List<File> fileList) {

        if (numOfSubJob < 0 || numOfSubJob % 4 != 0) {
            throw new DispatchRuntimeException();
        }

        if (fileList == null || fileList.isEmpty()) {
            throw new DispatchRuntimeException();
        }

        boolean existence = fileList.stream().allMatch(file -> file.exists());
        if (!existence) {
            throw new DispatchRuntimeException();
        }

    }


}
