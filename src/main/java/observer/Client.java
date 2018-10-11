package observer;

import observer.util.PatternUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 11:21 PM
 */
public class Client {
    public static void main(String[] args) throws Exception {
        File                        parentPathWithDate = new File("/data/tradereplay/20181002");
        List<File>                  csvFiles           = (List<File>) FileUtils.listFiles(parentPathWithDate, null, false);
        List<File>                  inputSourceCsvList = csvFiles.stream().filter((file) -> PatternUtils.isValidSource(file.getName())).collect(Collectors.toList());
        int                         numOfCsv           = csvFiles.size();
        DispatchStrategyStaticProxy proxy              = new DispatchStrategyStaticProxy("observer.impl.DxsPortHashStrategy");
        proxy.choose(numOfCsv, inputSourceCsvList);


    }
}
