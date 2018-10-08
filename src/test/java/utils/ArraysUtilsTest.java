package utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/3
 * @time 12:20 AM
 */
public class ArraysUtilsTest {

    @Test
    public void testIsSeparator()

    {
        byte[] b1 = {0, 1, 2, 3, 44, 44, -1};
        Assert.assertFalse(ArraysUtils.isSeparator(b1, -1));
        Assert.assertFalse(ArraysUtils.isSeparator(b1, 10));
        Assert.assertFalse(ArraysUtils.isSeparator(b1, 2));
        Assert.assertFalse(ArraysUtils.isSeparator(b1, 3));
        Assert.assertTrue(ArraysUtils.isSeparator(b1, 4));
        Assert.assertTrue(ArraysUtils.isSeparator(b1, 5));
        Assert.assertFalse(ArraysUtils.isSeparator(b1, 6));
        Assert.assertFalse(ArraysUtils.isSeparator(b1, 7));
    }

    @Test
    public void testFillSeparator()

    {
        byte[] b1 = {0, 1, 2, 3};
        ArraysUtils.fillSeparator(b1,0);
        Assert.assertArrayEquals(b1,new byte[]{44,1,2,3});

        byte[] b2 = {0, 1, 2, 3};
        ArraysUtils.fillSeparator(b2,-1);
        Assert.assertArrayEquals(b2,new byte[]{0,1,2,3});

        byte[] b3= {0, 1, 2, 3};
        ArraysUtils.fillSeparator(b3,1);
        Assert.assertArrayEquals(b3,new byte[]{0,44,2,3});

        byte[] b4= {0, 1, 2, 3};
        ArraysUtils.fillSeparator(b4,2);
        Assert.assertArrayEquals(b4,new byte[]{0,1,44,3});

        byte[] b5= {0, 1, 2, 3};
        ArraysUtils.fillSeparator(b5,3);
        Assert.assertArrayEquals(b5,new byte[]{0,1,2,44});
    }

    @Test
    public void testCopyArray(){
        byte[] src=new byte[]{1,2,3,4};
        byte[] target=new byte[]{5,6,7,8};
        ArraysUtils.copyArray(src,0,target,0,4);
        Assert.assertArrayEquals(target,new byte[]{1,2,3,4});

        src=new byte[]{1,2,3,4};
        target=new byte[]{5,6,7,8};
        ArraysUtils.copyArray(src,0,target,1,10);
        Assert.assertArrayEquals(target,new byte[]{5,1,2,3});

        src=new byte[]{1,2,3,4};
        target=new byte[]{5,6,7,8};
        ArraysUtils.copyArray(src,0,target,2,4);
        Assert.assertArrayEquals(target,new byte[]{5,6,1,2});

    }


}
