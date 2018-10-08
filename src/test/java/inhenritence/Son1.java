package inhenritence;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/5
 * @time 8:37 PM
 */
public class Son1 extends Parent {
    private static Son1 ourInstance = new Son1();
    private static int  age         = 23;

    public static Son1 getInstance() {
        return ourInstance;
    }

    private Son1() {
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getName() {
        return super.name;
    }

    public static int getAge() {
        return age;
    }
}
