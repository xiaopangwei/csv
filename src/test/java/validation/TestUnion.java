package validation;

import org.junit.Test;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/3
 * @time 3:26 PM
 */
public class TestUnion {

    @Test
    public void testUnion() throws Exception{
        StreamStub.s1.union(StreamStub.s2).map(new CustomMapFunction()).print();
        StreamStub.environment.execute();
    }
}
