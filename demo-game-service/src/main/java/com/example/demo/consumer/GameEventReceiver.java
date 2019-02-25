package com.example.demo.consumer;

import com.example.demo.config.MqChannel;
import com.example.demo.dto.GameCreateEvent;
import com.example.demo.dto.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Component
public class GameEventReceiver {

    @StreamListener(target = MqChannel.gameChannel)
    @SendTo(MqChannel.resultChannel)
    public Message<ResponseData> receiveGameCreateEvent(
            @Header(name = "username", required = false) String username,
            @Header(name = "Authorization", required = false) String authorization,
            Message<GameCreateEvent> eventMessage) {
        GameCreateEvent gameCreateEvent = eventMessage.getPayload();
        log.info("Message spanTraceId={}", eventMessage.getHeaders().get("spanTraceId"));
        log.info("Message spanId={}", eventMessage.getHeaders().get("spanId"));
        log.info("Message x-exception-message={}", eventMessage.getHeaders().get("x-exception-message"));
        log.info("Message x-exception-stacktrace={}", eventMessage.getHeaders().get("x-exception-stacktrace"));
        log.info("接收 GameCreateDto username={}, gameCreateEvent={}", username, gameCreateEvent);
        if (!StringUtils.hasText(username)) {
            throw new RuntimeException("需要帳號");
        }
        try {
            // 假設正在處裡帳號開通
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 發送處理結果
        ResponseData responseData = new ResponseData()
                .setEventId(gameCreateEvent.getEventId())
                .setCode("0")
                .setGameId(gameCreateEvent.getGameId())
                .setGameName(gameCreateEvent.getGameName())
                .setMessage(gameCreateEvent.getGameName() + " 創建帳號已完成")
                .setDateTime(LocalDateTime.now());
        return MessageBuilder.withPayload(responseData)
                .setHeader("username", username)
                .build();
    }
}