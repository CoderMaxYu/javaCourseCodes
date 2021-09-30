package com.yuwq.gateway.outbound;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工厂
 */
public class HttpNameThreadFactory implements ThreadFactory {

    //定义线程组
    private final ThreadGroup group;

    //记录线程数
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    //线程前缀名
    private final String namePrefix;

    //是否守护线程
    private final boolean daemon;

    public HttpNameThreadFactory(String namePrefix, boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        SecurityManager securityManage = System.getSecurityManager();
        //返回当前线程的线程组
        group = (securityManage!=null) ? securityManage.getThreadGroup()
                :Thread.currentThread().getThreadGroup();
    }

    public HttpNameThreadFactory(String namePrefix) {
        this(namePrefix, false);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group,r,namePrefix+"-thread-"+threadNumber.getAndIncrement(),0);
        //设置是否守护线程
        t.setDaemon(daemon);
        return t;
    }
}
