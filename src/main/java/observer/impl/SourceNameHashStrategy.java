package observer.impl;

import java.io.File;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 9:01 PM
 */
public class SourceNameHashStrategy extends AbstractHashStrategy {


    @Override
    protected int getHashKey(File file) {
        return file.hashCode();
    }
}
