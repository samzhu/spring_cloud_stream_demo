package com.example.demo.consumer;

import com.example.demo.config.MqChannel;
import com.example.demo.dto.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResultEventReceiver {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;   //實現向瀏覽器發送信息的功能

    @StreamListener(target = MqChannel.resultChannel)
    public void receiveGameCreateResultEvent(
            @Header("username") String username,
            Message<ResponseData> eventMessage) {
        this.sendToUser(username, eventMessage);
    }

    @StreamListener(MqChannel.userNotifyChannel)
    public void receiveUserNotify(
            @Header("username") String username,
            Message<ResponseData> eventMessage) {
        this.sendToUser(username, eventMessage);
    }

    private void sendToUser(String username, Message<ResponseData> eventMessage) {
        log.info("接收 ResponseData username={}, eventMessage={}", username, eventMessage);
        ResponseData responseData = eventMessage.getPayload();
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", responseData);
    }
}
