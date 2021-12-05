package com.yuwq.pubsub;

import redis.clients.jedis.JedisPubSub;

public class Subscriber extends JedisPubSub {

    public Subscriber(){}
    @Override
    public void onMessage(String channel, String message) {       //收到消息会调用
        System.out.println(String.format("订阅者收到消息, channel %s, message %s", channel, message));
    }
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {    //订阅了频道会调用
        System.out.println(String.format("订阅成功, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {   //取消订阅 会调用
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }
}
