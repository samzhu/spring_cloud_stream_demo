package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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