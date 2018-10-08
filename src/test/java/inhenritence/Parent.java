package inhenritence;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/5
 * @time 8:37 PM
 */
public abstract class Parent {
    public static  String name        = "haha";
    private static int    age         = 100;
    private static Parent ourInstance = null;

    public static Parent getInstance() {
        return ourInstance;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public static int getAge() {
        return age;
    }
}
