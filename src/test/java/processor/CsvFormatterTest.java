package processor;

import org.junit.Assert;
import org.junit.Test;
import utils.CsvFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/2
 * @time 11:39 PM
 */
public class CsvFormatterTest {
    private static final byte[] srcByteArray="0,1,23,456".getBytes();
    @Test
    public void testGetMaxCopyableSequence(){
        Map map=new HashMap();
        map.put(0,0);
        map.put(1,1);
        map.put(2,2);
        map.put(3,3);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,0),4);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,1),3);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,2),2);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,3),1);

        map.clear();
        map.put(0,3);
        map.put(1,1);
        map.put(2,2);
        map.put(3,0);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,0),1);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,1),2);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,2),1);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,3),1);


        map.clear();
        map.put(0,3);
        map.put(2,1);
        map.put(1,2);
        map.put(3,0);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,0),1);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,1),1);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,2),1);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,3),1);


        map.clear();
        map.put(0,0);
        map.put(1,2);
        map.put(2,1);
        map.put(3,3);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,0),1);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,1),1);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,2),1);
        Assert.assertEquals(CsvFormatter.getMaxCopyableSequence(map,3),1);
    }

    @Test
    public void testAddNewElement4Map(){
        Map map=new HashMap();
        map.put(0,"-");
        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,map)),"0,-,1,23,456");


        map.clear();
        map.put(-1,"-");
        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,map)),"-,0,1,23,456");


        map.clear();
        map.put(1,"-");
        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,map)),"0,1,-,23,456");


        map.clear();
        map.put(2,"-");
        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,map)),"0,1,23,-,456");

        map.clear();
        map.put(3,"-");
        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,map)),"0,1,23,456,-");

        map.clear();
        map.put(4,"-");
        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,map)),"0,1,23,456,-");


        map.clear();
        map.put(0,"-");
        map.put(2,"-");
        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,map)),"0,-,1,-,23,456");


        map.clear();
        map.put(-1,"-");
        map.put(0,"-");
        map.put(1,"-");
        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,map)),"-,-,-,0,1,23,456");


        map.clear();
        map.put(0,"-");
        map.put(1,"-");
        map.put(2,"-");
        map.put(3,"-");
        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,map)),"0,-,-,-,-,1,23,456");

    }

    @Test
    public void testAddNewElement(){

        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,0,"-")),"0,-,1,23,456");

        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,-1,"-")),"-,0,1,23,456");

        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,1,"-")),"0,1,-,23,456");

        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,2,"-")),"0,1,23,-,456");

        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,3,"-")),"0,1,23,456,-");

        Assert.assertEquals(new String(CsvFormatter.addNewElement(srcByteArray,4,4,"-")),"0,1,23,456,-");

    }


    @Test
    public void testExchangeFieldOrder(){
        Map<Integer,Integer> map=new HashMap<>();

        //0打头
        map.clear();
        map.put(0,0);
        map.put(1,1);
        map.put(2,2);
        map.put(3,3);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"0,1,23,456");


        map.clear();
        map.put(0,0);
        map.put(1,1);
        map.put(2,3);
        map.put(3,2);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"0,1,456,23");

        map.clear();
        map.put(0,0);
        map.put(1,2);
        map.put(2,3);
        map.put(3,1);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"0,23,456,1");

        map.clear();
        map.put(0,0);
        map.put(1,2);
        map.put(2,1);
        map.put(3,3);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"0,23,1,456");

        map.clear();
        map.put(0,0);
        map.put(1,3);
        map.put(2,2);
        map.put(3,1);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"0,456,23,1");

        map.clear();
        map.put(0,0);
        map.put(1,3);
        map.put(2,1);
        map.put(3,2);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"0,456,1,23");

//
        //1 打头
        map.clear();
        map.put(0,1);
        map.put(1,0);
        map.put(2,2);
        map.put(3,3);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"1,0,23,456");

        map.clear();
        map.put(0,1);
        map.put(1,0);
        map.put(2,3);
        map.put(3,2);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"1,0,456,23");

        map.clear();
        map.put(0,1);
        map.put(1,2);
        map.put(2,0);
        map.put(3,3);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"1,23,0,456");

        map.clear();
        map.put(0,1);
        map.put(1,2);
        map.put(2,3);
        map.put(3,0);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"1,23,456,0");


        map.clear();
        map.put(0,1);
        map.put(1,3);
        map.put(2,0);
        map.put(3,2);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"1,456,0,23");

        map.clear();
        map.put(0,1);
        map.put(1,3);
        map.put(2,2);
        map.put(3,0);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"1,456,23,0");

        //2打头

        map.clear();
        map.put(0,2);
        map.put(1,0);
        map.put(2,1);
        map.put(3,3);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"23,0,1,456");

        map.clear();
        map.put(0,2);
        map.put(1,0);
        map.put(2,3);
        map.put(3,1);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"23,0,456,1");


        map.clear();
        map.put(0,2);
        map.put(1,1);
        map.put(2,3);
        map.put(3,0);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"23,1,456,0");

        map.clear();
        map.put(0,2);
        map.put(1,1);
        map.put(2,0);
        map.put(3,3);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"23,1,0,456");

        map.clear();
        map.put(0,2);
        map.put(1,3);
        map.put(2,0);
        map.put(3,1);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"23,456,0,1");

        map.clear();
        map.put(0,2);
        map.put(1,3);
        map.put(2,1);
        map.put(3,0);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"23,456,1,0");

        //3打头

        map.clear();
        map.put(0,3);
        map.put(1,0);
        map.put(2,1);
        map.put(3,2);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"456,0,1,23");

        map.clear();
        map.put(0,3);
        map.put(1,0);
        map.put(2,2);
        map.put(3,1);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"456,0,23,1");


        map.clear();
        map.put(0,3);
        map.put(1,1);
        map.put(2,0);
        map.put(3,2);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"456,1,0,23");

        map.clear();
        map.put(0,3);
        map.put(1,1);
        map.put(2,2);
        map.put(3,0);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"456,1,23,0");

        map.clear();
        map.put(0,3);
        map.put(1,2);
        map.put(2,0);
        map.put(3,1);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"456,23,0,1");

        map.clear();
        map.put(0,3);
        map.put(1,2);
        map.put(2,1);
        map.put(3,0);
        Assert.assertEquals(new String(CsvFormatter.exchangeFieldOrder(srcByteArray,map)),"456,23,1,0");
//
    }

}
