package com.service.achievementservice.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketService {
    private static final Map<Long, List<String>> userSocketMapping = new HashMap<>();
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAchievementNotification(final Long userId, final Long achievementId) {
        List<String> sockets = userSocketMapping.get(userId);

        sockets.forEach(s -> messagingTemplate.convertAndSendToUser(s, "topic/achievement-notification", achievementId));

    }

    public static void addSocketMapping(Long userId, String socketId) {
        List<String> sockets = userSocketMapping.get(userId);

        if (sockets == null) { // Entry does not exist yet
            sockets = new ArrayList<>();
            sockets.add(socketId);
            userSocketMapping.put(userId, sockets);
        } else { // Entry exists, add the socketId
            sockets.add(socketId);
        }
    }
}
