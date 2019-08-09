package Thread;

public class TestThreadPool {
    public static void main(String[] args) {
        FixedSizeThreadPool pool = new FixedSizeThreadPool(3,6);
        for (int j=0;j<6;j++){
            pool.submit(new Runnable() {
                public void run() {
                    System.out.println("一个线程被放在了我们的仓库中");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("有一个线程被唤醒了");
                    }
                }
            });
        }
        pool.shutDown();
    }
}
