package observer.impl;

import observer.util.PatternUtils;

import java.io.File;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 9:01 PM
 */
public class ProcessIdHashStrategy extends AbstractHashStrategy {


    @Override
    protected int getHashKey(File file) {
        return PatternUtils.getProcessId(file.getName());
    }
}
