package Thread;

import sun.swing.AccumulativeRunnable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class CallAble {

    public static void main(String[] args) {
       /* ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit( new AA());*/
        //executorService.execute();


        AA a = new AA();
        FutureTask futureTask = new FutureTask(a);
        new Thread(futureTask).start();
        //futureTask.run();


    }



}
class AA implements Callable{
    public Throwable call() throws Exception {
        for (int i = 0 ;i<10;i++){
            System.out.println(i);
        }
        return null;

    }
}

