package com.example.demo.consumer;

import com.example.demo.dto.ResponseData;
import com.example.demo.entity.EventResult;
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
public class EventResultReceiver {
    @Autowired
    private EventResultRepository eventResultRepository;
    @Autowired
    private Processor processor;

    @StreamListener(target = Processor.INPUT)
    public void receiveGameCreateResultEvent(
            @Header(name = "username", required = false) String username,
            Message<ResponseData> eventMessage) {
        ResponseData responseData = eventMessage.getPayload();
        log.info("username={}, responseData={}", username, responseData);
        EventResult eventResult = new EventResult();
        eventResult.setEventId(responseData.getEventId());
        eventResult.setCode(responseData.getCode());
        eventResult.setMessage(responseData.getMessage());
        eventResult.setUserName(username);
        eventResult.setGameId(responseData.getGameId());
        eventResult.setGameName(responseData.getGameName());
        eventResultRepository.save(eventResult);
    }
}
