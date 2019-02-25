package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class EventResult {
    @Id
    private String eventId;
    private String userName;
    private String gameId;
    private String gameName;
    private String code;
    private String message;
    @CreatedDate
    private LocalDateTime dateTime;
}