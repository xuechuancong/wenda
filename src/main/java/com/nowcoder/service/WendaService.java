package com.nowcoder.service;


import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class WendaService {

    public String getMessages(int userId) {
        return "Hello, coder!" + String.valueOf(userId);
    }
}
