package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResult {
    private String eventId;
    private String userName;
    private String gameId;
    private String gameName;
    private String code;
    private String message;
    private LocalDateTime dateTime;
}
