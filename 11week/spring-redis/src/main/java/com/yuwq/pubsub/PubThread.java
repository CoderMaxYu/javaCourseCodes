package com.yuwq.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PubThread extends Thread{


    private JedisPool jedisPool;

    public PubThread(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void run() {
        Jedis jedis = jedisPool.getResource();

        for (int i = 1; i < 101; i++) {
            jedis.publish("stock","stock_"+i);
            System.out.println("添加库存："+"stock_"+i);
        }

    }
}
