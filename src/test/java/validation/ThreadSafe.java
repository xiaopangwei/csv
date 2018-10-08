package validation;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/5
 * @time 5:08 PM
 */
public class ThreadSafe {
    private static final ThreadSafe ourInstance = new ThreadSafe();

    public synchronized static ThreadSafe getInstance() {
        return ourInstance;
    }

    private AtomicInteger num = new AtomicInteger(0);

    private ThreadSafe() {
    }

    public void add() {
        num.incrementAndGet();

    }

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getInstance().add();
                    int length = (int) (1 + Math.random() * 40);
                    try {
                        Thread.sleep(length);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        System.out.println(getInstance().num);
    }
}
