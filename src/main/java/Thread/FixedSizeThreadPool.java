package Thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedSizeThreadPool {

    //1.需要一个仓库
    private BlockingQueue<Runnable> blockingQueue;
    //2.需要一个线程的集合
    private List<Thread>  workers;
    //3.需要一个人来干活
    public static class Worker extends  Thread{

        private FixedSizeThreadPool pool;

        public Worker(FixedSizeThreadPool pool){
            this.pool = pool;
        }

        @Override
        public void run() {
            while(this.pool.isWorking||this.pool.blockingQueue.size()>0){
                Runnable task = null;
                try {
                    if(this.pool.isWorking){
                        task = this.pool.blockingQueue.take();
                    }else{
                        task = this.pool.blockingQueue.poll();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(task !=null){
                    task.run();
                    System.out.println("线程："+Thread.currentThread().getName()+"执行完毕");
                }
            }
        }
    }
    //4.需要进行线程池的初始化，仓库大小和集合大小
    public FixedSizeThreadPool(int poolSize,int taskSize) {
        if(poolSize<=0||taskSize<=0){
            throw new IllegalArgumentException("非法参数");
        }
        this.blockingQueue = new LinkedBlockingQueue<Runnable>(taskSize);
        this.workers = Collections.synchronizedList(new ArrayList<Thread>());
        for (int i=0;i<poolSize;i++){
            Worker worker = new Worker(this);
            worker.start();
            workers.add(worker);
        }
    }
    //5.需要往仓库里放任务（不阻塞）
    public boolean submit(Runnable task){
        if(isWorking){
            return this.blockingQueue.offer(task);
        }else{
            return false;
        }

    }
    //6.需要往仓库里放任务（阻塞）
    public void execute(Runnable task){
        try {
            this.blockingQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //7.需要有一个关闭的方法
    //a.关闭的时候，仓库要停止有新的任务进来
    //b.关闭的时候，如果仓库还有东西要执行完
    //c.如果再去仓库拿东西就不能阻塞了
    //d.如果还有线程阻塞，我们要哦进行中断
    private volatile boolean isWorking = true;
    public void shutDown(){
        this.isWorking = false;
        for (Thread thread:workers){
            if(thread.getState().equals(Thread.State.BLOCKED)){
               thread.interrupt();
            }
        }
    }


}
