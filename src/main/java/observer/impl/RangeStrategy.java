package observer.impl;

import observer.SourceFileDispatcherStrategy;
import observer.exception.DispatchException;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 9:01 PM
 */
public class RangeStrategy implements SourceFileDispatcherStrategy<Integer, File> {


    @Override
    public MultiMap<Integer, File> choose(int numOfSubJob, List<File> fileList) throws DispatchException {

        MultiMap<Integer, File> multiMap  = new MultiValueMap<>();
        int                     blockSize = fileList.size() / numOfSubJob;
        List<List<File>>        subLists  = subList(fileList, blockSize);
        for (int i = 0; i < subLists.size(); i++) {
            List<File> subFiles = subLists.get(i);
            for (File item : subFiles) {
                multiMap.put(i, item);
            }
        }
        return multiMap;

    }


    private static <T> List<List<T>> subList(List<T> list, int blockSize) {
        List<List<T>> lists = new ArrayList<List<T>>();
        if (list != null && blockSize > 0) {
            int listSize = list.size();
            if (listSize <= blockSize) {
                lists.add(list);
                return lists;
            }
            int batchSize = listSize / blockSize;
            int remain    = listSize % blockSize;
            for (int i = 0; i < batchSize; i++) {
                int fromIndex = i * blockSize;
                int toIndex   = fromIndex + blockSize;
                System.out.println("fromIndex=" + fromIndex + ", toIndex=" + toIndex);
                lists.add(list.subList(fromIndex, toIndex));
            }
            if (remain > 0) {
                System.out.println("fromIndex=" + (listSize - remain) + ", toIndex=" + (listSize));
                lists.add(list.subList(listSize - remain, listSize));
            }
        }
        return lists;
    }
}
