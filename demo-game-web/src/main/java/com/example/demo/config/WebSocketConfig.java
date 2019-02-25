package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker  //開啟使用STOMP協議來傳輸基於代理的消息
// WebSocketMessageBrokerConfigurer or AbstractWebSocketMessageBrokerConfigurer
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //允許客戶端使用socketJs方式訪問，訪問點為ws，允許跨域
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
//        registry.addEndpoint("/endpointSang").withSockJS();
//        registry.addEndpoint("/endpointChat").withSockJS();
//        registry.addEndpoint("/socket").withSockJS()
//                .setInterceptors( new ChatHandlerShareInterceptor())
//                .setStreamBytesLimit(512 * 1024)
//                .setHttpMessageCacheSize(1000)
//                .setDisconnectDelay(30 * 1000);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //訂閱廣播 Broker（消息代理）名稱
        registry.enableSimpleBroker("/queue");
//        registry.enableSimpleBroker("/topic"); // Enables a simple in-memory broker
        //全局使用的訂閱前綴（客戶端訂閱路徑上會體現出來）
        registry.setApplicationDestinationPrefixes("/app/");
        //點對點使用的訂閱前綴（客戶端訂閱路徑上會體現出來），不設置的話，默認也是/user/
        registry.setUserDestinationPrefix("/user/");
    }

    @Override
    public void configureWebSocketTransport(final WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
                        // 客户端与服务器端建立连接后，此处记录谁上线了
                        String username = session.getPrincipal().getName();
                        log.info("online: " + username);
                        super.afterConnectionEstablished(session);
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        // 客户端与服务器端断开连接后，此处记录谁下线了
                        String username = session.getPrincipal().getName();
                        log.info("offline: " + username);
                        super.afterConnectionClosed(session, closeStatus);
                    }
                };
            }
        });
    }
}