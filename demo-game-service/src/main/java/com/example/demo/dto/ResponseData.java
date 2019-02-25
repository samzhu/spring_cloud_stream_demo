package com.example.demo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ResponseData {
    private String eventId;
    private String code;
    private String message;
    //
    private String userName;
    private LocalDateTime dateTime;
    //
    private String gameId;
    private String gameName;
}
