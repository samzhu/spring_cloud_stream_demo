package com.example.demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

// https://www.e4developer.com/2018/02/05/handling-bad-messages-with-rabbitmq-and-spring-cloud-stream/

@Slf4j
@Component
public class DlqResultReceiverAmqp {
    private static final String ORIGINAL_QUEUE = "game.create.message.v1.gameGroup";
    private static final String DLQ = ORIGINAL_QUEUE + ".dlq";
    private static final String X_RETRIES_HEADER = "x-retries";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = DLQ)
    public void receiveDLQResult(Message failedMessage) {
//        log.info("amqp getMessageProperties={}", failedMessage.getMessageProperties());
//        log.info("amqp getHeaders={}", failedMessage.getMessageProperties().getHeaders());

        Map<String, Object> headers = failedMessage.getMessageProperties().getHeaders();
        headers.keySet().forEach(key -> {
            log.info("key={}", key);
            log.info("Object={}", headers.get(key));


        });
        // x-original-routingKey
        // x-original-exchange
        // contentType
//        log.info("amqp spanTraceId={}", headers.get("spanTraceId"));
//        log.info("amqp spanId={}", headers.get("spanId"));
//        log.info("amqp x-exception-message={}", headers.get("x-exception-message"));
//        log.info("amqp x-exception-stacktrace={}", headers.get("x-exception-stacktrace"));



        String messageBody = new String(failedMessage.getBody());
        log.info("amqp messageBody={}", messageBody);


    }

    //    @RabbitListener(queues = DLQ)
    public void rePublish(Message failedMessage) {
        log.info("Message={}", failedMessage);
        failedMessage = attemptToRepair(failedMessage);
        log.info("attemptToRepair Message={}", failedMessage);
        Object retriesHeaderObj = failedMessage.getMessageProperties().getHeaders().get(X_RETRIES_HEADER);
        log.info("retriesHeaderObj={}", retriesHeaderObj);
        Integer retriesHeader = (Integer) failedMessage.getMessageProperties().getHeaders().get(X_RETRIES_HEADER);

        if (retriesHeader == null) {
            retriesHeader = Integer.valueOf(0);
        }
        if (retriesHeader < 3) {
            failedMessage.getMessageProperties().getHeaders().put(X_RETRIES_HEADER, retriesHeader + 1);
            this.rabbitTemplate.send(ORIGINAL_QUEUE, failedMessage);
        } else {
            System.out.println("Writing to databse: " + failedMessage.toString());
            //we can write to a database or move to a parking lot queue
        }
    }

    private Message attemptToRepair(Message failedMessage) {
        String messageBody = new String(failedMessage.getBody());
        log.info("messageBody={}", messageBody);
        if (messageBody.contains("vegetables")) {
            System.out.println("Repairing message: " + failedMessage.toString());
            messageBody = messageBody.replace("vegetables", "cakes");
            return MessageBuilder.withBody(messageBody.getBytes()).copyHeaders(failedMessage.getMessageProperties().getHeaders()).build();
        }
        return failedMessage;
    }
}
