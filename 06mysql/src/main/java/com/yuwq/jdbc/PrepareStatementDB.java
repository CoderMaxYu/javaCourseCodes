package com.yuwq.jdbc;

import com.google.common.collect.Lists;
import com.yuwq.po.Order;
import com.yuwq.util.SnowFlake;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.*;

/**
 * 2）使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
 */
public class PrepareStatementDB {

    public static Connection initConnection() throws Exception {
        // 1.注册数据库驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 2.与数据库建立连接
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/shop_test?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8", "root", "123456");
    }

    public void insert(List<Order> userOrderLogs, Connection connection) throws Exception {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO `order` (`order_id`, `user_id`, `total_price`) VALUES ( ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < userOrderLogs.size(); i++) {
                Order order = userOrderLogs.get(i);
                ps.setLong(1, order.getOrderId());
                ps.setLong(2, order.getUserId());
                ps.setBigDecimal(3, order.getTotalPrice());
                ps.executeUpdate();
                ps.clearParameters();
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public int insertBatch(List<Order> orderList, Connection connection) throws Exception {
        int count = 0;
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO `order` (`order_id`, `user_id`, `total_price`) VALUES ( ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < orderList.size(); i++) {
                Order order = orderList.get(i);
                ps.setLong(1, order.getOrderId());
                ps.setLong(2, order.getUserId());
                ps.setBigDecimal(3, order.getTotalPrice());
                ps.addBatch();
                if(i % 10000==0){
                    int[] r = ps.executeBatch();
                    count += r.length;
                    //清空已有的sql
                    ps.clearBatch();
                }
            }
            //为了确保缓存没有sql语句未被执行
            ps.executeBatch();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return count;
    }

    private void insertTest(List<Order> orderList) throws Exception {
        Connection connection = initConnection();
//        insert(userOrderLogs, connection);
        try {
            connection.setAutoCommit(false);
            insert(orderList, connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.rollback();
            // 关闭资源
            connection.close();
        }
    }



    private int insertBatchTest(List<Order> userOrderLogs) throws Exception {
        Connection connection = initConnection();
//        insertBatch(userOrderLogs, connection);
        int count = 0;
        try {
            connection.setAutoCommit(false);
            count = insertBatch(userOrderLogs, connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.rollback();
            // 关闭资源
            connection.close();
        }
        return count;
    }

    // cpu核心数
    int cors = Runtime.getRuntime().availableProcessors();
    // 自定义线程池如何生成线程
    NamedThreadFactory threadFactory = new NamedThreadFactory("thread-yuwq-");
    // keepAliveTime代表最大线程空闲多长时间退出
    // hander拒绝策略
    // 1.ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
    // 2.ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。如果线程队列已满，则后续提交的任务都会被丢弃，且是静默丢弃。
    // 3.ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提交被拒绝的任务。
    // 4.ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
    private ExecutorService proxyService = new ThreadPoolExecutor(cors, cors * 2,
            1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1000),
            threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

    private int insertBatchThreadTest(List<Order> orderList) throws Exception {
        int count = 0;
        List<Future<Integer>> futures = Lists.newArrayList();
        Lists.partition(orderList, 10_0000).forEach(vos -> {
            Future<Integer> future = proxyService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return insertBatchTest(vos);
                }
            });
            futures.add(future);
        });
        for (Future<Integer> future : futures) {
            count += future.get();
        }
        proxyService.shutdown();
        return count;
    }

    private void insertBatchThreadTest1(List<Order> orderList) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(100_0000 / 10_0000);
        Lists.partition(orderList, 10_0000).forEach(vos -> {
            Runnable runnable = () -> {
                try {
                    insertBatchTest(vos);
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(runnable).start();
        });
        countDownLatch.await();
    }

    public static void main(String[] args) throws Exception {
        long now = System.currentTimeMillis();
        List<Order> orderList = Lists.newArrayList();
        for (int i = 0 ; i < 100_0000; i++) {
            Order order = new Order();
            order.setOrderId(SnowFlake.getPrimaryKey());
            order.setUserId(1L);
            order.setTotalPrice(new BigDecimal(100));
            orderList.add(order);
        }
        PrepareStatementDB statementDB = new PrepareStatementDB();
        long start = System.currentTimeMillis();
        statementDB.insertTest(orderList);
        System.out.println("循环插入用时：" + (System.currentTimeMillis() - start));
        long start1 = System.currentTimeMillis();
        statementDB.insertBatchTest(orderList);
        System.out.println("批量插入用时：" + (System.currentTimeMillis() - start1));
        long start2 = System.currentTimeMillis();
        int count = statementDB.insertBatchThreadTest(orderList);
        System.out.println("批量线程插入count：" + count + "用时：" + (System.currentTimeMillis() - start2));
        long start3 = System.currentTimeMillis();
        statementDB.insertBatchThreadTest1(orderList);
        System.out.println("批量线程1插入用时：" + (System.currentTimeMillis() - start3));

    }
}
