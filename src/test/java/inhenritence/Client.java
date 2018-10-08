package inhenritence;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/5
 * @time 8:43 PM
 */
public class Client {

    public static void main(String[] args) {
        SuperClass superClass = new SuperClass();
        SuperClass subClass   = new SubClass();
        superClass.find();//根据对象引用类型决定调用父类或子类的方法
        subClass.find(); //根据对象引用类型决定调用父类或子类的方法
        System.out.println("+++++++++++++++++++++++++++++++++");
        superClass.test();
        subClass.test();
        System.out.println("+++++++++++++++++++++++++++++++++");
        SubClass subClass1 = new SubClass();
        superClass.find();//根据对象引用类型决定调用父类或子类的方法
        subClass1.find(); //根据对象引用类型决定调用父类或子类的方法
    }


    static class SuperClass {

        public static void find() {
            System.out.println("find");
        }

        public void test() {
            System.out.println("superclass test method");
        }

    }

    static class SubClass extends SuperClass {

        public static void find() {
            System.out.println("hello");
        }

        @Override
        public void test() {
            System.out.println("subclass test method");
        }
    }

}
