package com.example.demo.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MqChannel {
    String gameChannel = "gameChannel";
    String resultChannel = "resultChannel";

    @Input(gameChannel)
    SubscribableChannel gameChannelInput();

    @Output(resultChannel)
    MessageChannel resultChannelOutput();
}
