package com.example.demo.config;

import cn.izern.sequence.Sequence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

    @Bean
    public Sequence sequence() {
        Sequence sequence = new Sequence();
        return sequence;
    }
}
