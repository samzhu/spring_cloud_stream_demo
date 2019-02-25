package com.example.demo.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MqChannel {
    // 名稱需對應到 cloud.stream.bindings channelName
    String gameChannel = "gameChannel";
    String resultChannel = "resultChannel";
    String userNotifyChannel = "userNotifyChannel";

    @Output(gameChannel)
    MessageChannel gameChannelOutput();

    @Input(resultChannel)
    SubscribableChannel resultChannelInput();

    @Input(userNotifyChannel)
    SubscribableChannel userNotifyChannelInput();
}
