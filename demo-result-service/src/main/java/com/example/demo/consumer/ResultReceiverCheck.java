package com.example.demo.consumer;

import com.example.demo.dto.ResponseData;
import com.example.demo.repository.EventResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ResultReceiverCheck {
    @Autowired
    private EventResultRepository eventResultRepository;
    @Autowired
    private Processor processor;

    @StreamListener(target = Processor.INPUT, condition = "headers['username']=='222'")
    public void createGamePlus(
            @Header(name = "username", required = false) String username,
            Message<ResponseData> eventMessage) {
        ResponseData responseData = eventMessage.getPayload();
        log.info("responseData={}", responseData);
        ResponseData responseDataToWeb = new ResponseData()
                .setCode("0")
                .setGameId(responseData.getGameId())
                .setGameName(responseData.getGameName())
                .setMessage(responseData.getGameName() + ", 歡迎 VIP 馬上充值 送好禮!!")
                .setDateTime(LocalDateTime.now());
        Message<ResponseData> responseDataMessage = MessageBuilder.withPayload(responseDataToWeb)
                .setHeader("username", username)
                .build();
        processor.output().send(responseDataMessage);
    }
}