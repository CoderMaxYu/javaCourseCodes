package com.yuwq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;

/**
 * @author yuwq
 * @date 2021/12/12
 **/
public class ActiveMqDemo {

    public static void main(String[] args) {
        // 定义Destination
//        Destination destination = new ActiveMQQueue("test.queue");
        Destination destination = new ActiveMQTopic("test.topic");

        // 测试
        testDestination(destination);
    }

    public static void testDestination(Destination destination) {
        try {
            // 创建连接和会话
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
            ActiveMQConnection conn = (ActiveMQConnection) factory.createConnection();
            conn.start();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // 创建两个消费者 并 绑定消息监听器
            MessageConsumer consumer1 = session.createConsumer(destination);
            MessageConsumer consumer2 = session.createConsumer(destination);
            consumer1.setMessageListener(new TextMessageListener("consumer1"));
            consumer2.setMessageListener(new TextMessageListener("consumer2"));

            // 模拟生产消息
            MessageProducer producer = session.createProducer(destination);
            int index = 0;
            while (index++ < 20) {
                TextMessage message = session.createTextMessage(index + " message.");
                producer.send(message);
            }

            Thread.sleep(20000);
            session.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
