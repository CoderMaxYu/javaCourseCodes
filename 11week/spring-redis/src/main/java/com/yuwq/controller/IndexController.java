package com.yuwq.controller;

import com.yuwq.service.RedisLock;
import com.yuwq.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    RedisLock redisLock;

    int count = 10000;

    /**
     * 1000个异步线程，模拟减库存
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/index")
    @ResponseBody
    public String index() throws InterruptedException {

        SnowFlake snowFlake = new SnowFlake(2, 3);

        int clientcount = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(clientcount);

        ExecutorService executorService = Executors.newFixedThreadPool(clientcount);
        long start = System.currentTimeMillis();
        for (int i = clientcount; i > 0; i--) {
            executorService.execute(() -> {

                //通过Snowflake算法获取唯一的ID字符串
                String id = Long.toString(snowFlake.nextId());
                try {
                    redisLock.lock(id, 20);
                    count--;
                } finally {
                    redisLock.unlock(id);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        log.info("执行线程数:{},总耗时:{},count数为:{}", clientcount, end - start, count);
        return "Hello";
    }
}