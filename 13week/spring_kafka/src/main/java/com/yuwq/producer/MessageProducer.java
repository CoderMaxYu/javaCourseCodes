package com.yuwq.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yuwq.common.constant.TopicConst;
import com.yuwq.common.message.PayMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class MessageProducer {
    private static Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    @Autowired
    private KafkaTemplate kafkaTemplate;
    private Gson gson = new GsonBuilder().create();

    public void send(PayMessage payMessage) {
        String msg = gson.toJson(payMessage);
        kafkaTemplate.send(TopicConst.PAY_TOPIC_2, msg);
        logger.info("MessageProducer: send: message is: [" + msg + "]");

    }

}