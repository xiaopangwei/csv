package inhenritence;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/5
 * @time 8:37 PM
 */
public class Son2 extends Parent {
    private static Son2 ourInstance = new Son2();

    public static Son2 getInstance() {
        return ourInstance;
    }


    public void setName(String name) {
        super.setName(name);
    }

    public String getName() {
        return super.name;
    }

}
