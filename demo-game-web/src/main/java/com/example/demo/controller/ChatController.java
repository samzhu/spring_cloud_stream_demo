package com.example.demo.controller;

import com.example.demo.dto.ChatDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;   //实现向浏览器发送信息的功能

    /**
     * 转到消息推送，对应前端chat.html中的stomp.send("/chat1",{},text)
     * 此处未使用注解，而是使用代码编程来完成推送，目的是为了方便对批量（可理解为分组）推送进行扩展
     * 编程实现消息推送,点对点使用convertAndSendToUser，广播使用convertAndSendTo
     * 使用注解可参考全局推送里面的具体实现，注：点对点用SendToUser()来代替SendTo()
     */
    @MessageMapping("/chat")
//    可以直接在参数中获取Principal，Principal中包含有当前用户的用户名。
    public void handleChat(
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor,
            @Header("simpSessionId") String sessionId,
            @Payload ChatDto chatDto) {
//        String accessorsessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
        log.info("header.sessionId  ={}", sessionId);
//        log.info("accessor={}", headerAccessor);
        // accessor=StompHeaderAccessor [headers={spanTraceId=4030be5da58fa500, spanId=4030be5da58fa500, simpMessageType=MESSAGE, stompCommand=SEND, nativeHeaders={destination=[/app/chat], content-length=[31], spanTraceId=[4030be5da58fa500], spanId=[4030be5da58fa500], spanSampled=[1]}, simpSessionAttributes={}, simpHeartbeat=[J@7708d399, simpUser=org.springframework.security.authentication.UsernamePasswordAuthenticationToken@441f0e52: Principal: org.springframework.security.core.userdetails.User@c1f2: Username: 222; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Granted Authorities: ROLE_USER; Credentials: [PROTECTED]; Authenticated: true; Details: org.springframework.security.web.authentication.WebAuthenticationDetails@2cd90: RemoteIpAddress: 0:0:0:0:0:0:0:1; SessionId: 7C524153126F2BDCEB1005440497BD96; Granted Authorities: ROLE_USER, spanSampled=1, lookupDestination=/chat, simpSessionId=vd0v1faq, simpDestination=/app/chat}]

        chatDto.setFromUserName(principal.getName());
        chatDto.setDateTime(LocalDateTime.now());
        log.info("chatDto={}", chatDto);
        messagingTemplate.convertAndSendToUser(chatDto.getChatTo(), "/queue/notifications", chatDto);
    }
}
