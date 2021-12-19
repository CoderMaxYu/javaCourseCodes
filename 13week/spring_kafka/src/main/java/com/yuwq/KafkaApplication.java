package com.yuwq;

import com.yuwq.common.message.PayMessage;
import com.yuwq.common.util.ToolsUtil;
import com.yuwq.producer.MessageProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(KafkaApplication.class, args);
        MessageProducer producer = applicationContext.getBean(MessageProducer.class);
        while (true){
            PayMessage message = new PayMessage();
            message.setFee(ToolsUtil.getFee());
            message.setOrderCode(ToolsUtil.getNextCode());
            message.setSendTime(System.currentTimeMillis());
            producer.send(message);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
