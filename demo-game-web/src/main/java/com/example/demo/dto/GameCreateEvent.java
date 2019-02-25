package com.example.demo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GameCreateEvent {
    private String eventId;
    private String gameId;
    private String gameName;
}
