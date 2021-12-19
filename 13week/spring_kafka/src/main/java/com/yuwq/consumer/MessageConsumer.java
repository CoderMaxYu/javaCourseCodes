package com.yuwq.consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yuwq.common.constant.TopicConst;
import com.yuwq.common.message.PayMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private Gson gson = new GsonBuilder().create();

    @KafkaListener(topics = TopicConst.PAY_TOPIC_2)
    public void onMessage(String payMessage) {
        PayMessage msg = gson.fromJson(payMessage, PayMessage.class);
        logger.info("MessageConsumer: onMessage: message is: [" + msg + "]");
    }
}