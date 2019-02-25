package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatDto {
    private String fromUserName;
    private String chatTo;
    private String message;
    private LocalDateTime dateTime;
}
