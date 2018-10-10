package observer;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/10
 * @time 11:21 PM
 */
public class Client {
    public static void main(String[] args) throws Exception{
        DispatchStrategyStaticProxy proxy=new DispatchStrategyStaticProxy("observer.impl.DxsPortHashStrategy");
        proxy.choose(4,null);


    }
}
