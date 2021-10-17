
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class HomeWork02 {

    public static void main(String[] args) throws Exception{

        long start=System.currentTimeMillis();

        /** 1.守护线程方式 begin **/
//        Runnable task = () -> {
//            int result = sum();
//            Thread t = Thread.currentThread();
//            System.out.println("当前线程:" + t.getName());
//            System.out.println("异步计算结果为："+result);
//        };
//        Thread thread = new Thread(task);
//        thread.setName("test-thread-1");
//        thread.setDaemon(false);
//        thread.start();
        /** 1.守护线程方式 end **/

        /** 2.future 方式begin **/
        // 在这里创建一个线程或线程池，
//        ExecutorService executor = Executors.newCachedThreadPool();
//        Future<Integer> result = executor.submit(new Callable<Integer>() {
//            public Integer call() throws Exception {
//                return sum();
//            }
//        });
//        executor.shutdown();
//        System.out.println("异步计算结果为："+result.get());
        /** 2.future 方式 end **/

        /** 3.futuretask begin **/
//        FutureTask<Integer> futureTask = new FutureTask<>(new CallableTask());
//        new Thread(futureTask).start();
//        System.out.println("异步计算结果为："+futureTask.get());
        /** 3.futuretask end **/

        /** 4. CompletableFuture begin **/
//        Integer result1 = CompletableFuture.supplyAsync(()->{return sum();}).join();
//        System.out.println("异步计算结果为："+result1);
        /** 4. CompletableFuture end **/

        /** 5.LockSupport begin **/
//        Thread mainThread =Thread.currentThread();
//        System.out.println("currentThread===>"+mainThread.getName());
//        Thread test = new countFibo();
//        test.start();
//        test.sleep(3000);
//        LockSupport.unpark(test);
//        LockSupport.park(test);
//        test.sleep(10000);
//        Thread.interrupted();
//        LockSupport.unpark(test);
//        LockSupport.unpark(mainThread);

        /** 5.LockSupport end **/

        /** 6.Lock begin **/
//        Lock lock = new ReentrantLock(true);
//        lock.lock();
//        int result = sum();
//        System.out.println("异步计算结果为："+result);
//        lock.unlock();
        /** 6.Lock end **/

        /** 7.Semaphore begin **/
//        Semaphore semaphore = new Semaphore(1);
//        semaphore.acquire();
//        new countFibo2().start();
//        semaphore.release();
        /** 7.Semaphore end **/

        /** 8.CountDownLatch begin **/
//        CountDownLatch latch = new CountDownLatch(1);
//        new Thread(new countDownLatchTask(latch)).start();
//        latch.await(); //主线程等待
        /** 8.CountDownLatch end **/

        /** 9.CyclicBarrier begin **/
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
//        new Thread(new CyclicBarrierTask(cyclicBarrier)).start();
//        Thread.sleep(10000L);
        /** 9.CyclicBarrier end **/

        /** 10.join begin **/
        Runnable task = () -> {
            int result = sum();
            Thread t = Thread.currentThread();
            System.out.println("当前线程:" + t.getName());
            System.out.println("异步计算结果为："+result);
        };
        Thread thread = new Thread(task);
        thread.start();
        thread.join();
//        thread.join();
        /** 10.join  end **/
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }

    static class countFibo extends Thread{
        @Override public void run() {
            int result = sum();
            LockSupport.park();
            System.out.println("异步计算结果为："+result);
        }
    }

    static class countFibo2 extends Thread{
        @Override public void run() {
            int result = sum();
            System.out.println("异步计算结果为："+result);
        }
    }

    static class countDownLatchTask implements Runnable{
        private CountDownLatch countDownLatch;
        public countDownLatchTask(CountDownLatch countDownLatch){
            this.countDownLatch =countDownLatch;
        }
        @Override public void run() {
            int result = sum();
            System.out.println("异步计算结果为："+result);
            this.countDownLatch.countDown();
        }
    }

    static class CyclicBarrierTask implements Runnable{
        private CyclicBarrier cyclicBarrier;
        public CyclicBarrierTask(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier =cyclicBarrier;
        }
        @Override public void run() {
            try {
                int result = sum();
                System.out.println("异步计算结果为："+result);
                this.cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    static class CallableTask implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
           return  sum();
        }
    }
}