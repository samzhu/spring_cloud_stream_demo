package com.example.demo.client;

import com.example.demo.dto.EventResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "resultservice-v1")
public interface GameClient {

    @GetMapping(path = "/eventResults/{eventId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Resource<EventResult> getById(@PathVariable("eventId") Long eventId);
}
