package com.example.demo.controller;

import cn.izern.sequence.Sequence;
import com.example.demo.client.GameClient;
import com.example.demo.config.MqChannel;
import com.example.demo.dto.EventResult;
import com.example.demo.dto.GameCreateEvent;
import com.example.demo.dto.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class GameController {
    @Autowired
    private Sequence sequence;
    @Autowired
    private MqChannel mqChannel;
    @Autowired
    private GameClient gameClient;

    private Map<String, String> gameNameMap = new HashMap();

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("初始化遊戲資料");
        gameNameMap.put("001", "人中之龍 Online");
        gameNameMap.put("002", "魔法禁書目錄 Ⅲ（とある魔術の禁書目録Ⅲ）");
        gameNameMap.put("003", "復活邪神 Re ; universe（ロマンシング サガ リ・ユニバース）");
        gameNameMap.put("004", "迷霧機甲（ミストギア）");
        gameNameMap.put("005", "Dragalia Lost ～失落的龍絆～（ドラガリアロスト）");
    }

    /**
     * 申請帳號開通
     * Websocket 跟 Polling 共用, 所以還是會回傳 EventId 供 Polling 查詢
     * 配合 axios 可存取 Cookie, 所以配置 Access-Control-Allow-Credentials
     *
     * @param principal
     * @param gameCreateEvent
     * @return
     */
    //
    @CrossOrigin(origins = "*", allowCredentials = "true")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PostMapping(path = "/api/game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData create(
            Principal principal,
            @RequestBody GameCreateEvent gameCreateEvent) {
        String eventId = String.valueOf(sequence.nextId());
        String gameName = gameNameMap.get(gameCreateEvent.getGameId());
        gameCreateEvent.setEventId(eventId);
        gameCreateEvent.setGameName(gameName);
        // 發出遊戲開通事件
        Message<GameCreateEvent> message = MessageBuilder.withPayload(gameCreateEvent)
                .setHeader("username", principal.getName())
                .build();
        Boolean sendResult = mqChannel.gameChannelOutput().send(message);
        // 回傳處理結果(到 MQ 的部分)
        ResponseData responseData = new ResponseData();
        responseData.setEventId(eventId);
        responseData.setCode("0");
        responseData.setDateTime(LocalDateTime.now());
        responseData.setMessage(gameCreateEvent.getGameName() + " 創建帳號中...請稍後");
        return responseData;
    }

    @CrossOrigin(origins = "*", allowCredentials = "true")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/api/gameEvent/{eventId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public EventResult getGameEvent(
            @PathVariable("eventId") Long eventId) {
        Resource<EventResult> gameEventResource = gameClient.getById(eventId);
        return gameEventResource.getContent();
    }
}
