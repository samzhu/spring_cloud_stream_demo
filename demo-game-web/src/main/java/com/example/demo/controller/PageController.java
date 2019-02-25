package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class PageController {

    @RequestMapping(value = "/login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "/gameWebsocket")
    public String toGameWebsocket() {
        return "gameWebsocket";
    }

    @RequestMapping(value = "/gamePolling")
    public String toGamePolling() {
        return "gamePolling";
    }

    @RequestMapping(value = "/chat")
    public String toChat() {
        return "chat";
    }
}
