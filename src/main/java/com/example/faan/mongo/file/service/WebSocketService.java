package com.example.faan.mongo.file.service;

import com.example.faan.mongo.modelos.dto.SavePost;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyNewPost(SavePost post) {
        messagingTemplate.convertAndSend("/topic/newPost", post);
    }
}
