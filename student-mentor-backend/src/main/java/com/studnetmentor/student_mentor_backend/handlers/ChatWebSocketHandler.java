package com.studnetmentor.student_mentor_backend.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studnetmentor.student_mentor_backend.models.ChatMessage;
import com.studnetmentor.student_mentor_backend.repositories.ChatMessageRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private final ChatMessageRepository chatMessageRepository;

    public ChatWebSocketHandler(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        
        if (userId != null) {
            userSessions.put(userId, session);
            System.out.println("✅ User Registered: " + userId);
        } else {
            System.out.println("❌ Connection rejected: No userId provided");
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.remove(userId);
            System.out.println("User Disconnected: " + userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        
        // 1. Parse JSON
        JsonNode jsonNode = objectMapper.readTree(payload);
        String senderId = jsonNode.get("senderId").asText();
        String recipientId = jsonNode.get("targetId").asText(); 
        String content = jsonNode.get("text").asText();

        // 2. Save to Database (PERSISTENCE)
        ChatMessage savedMsg = new ChatMessage(senderId, recipientId, content);
        chatMessageRepository.save(savedMsg);

        // 3. Private Routing Logic
        WebSocketSession recipientSession = userSessions.get(recipientId);
        
        // If Recipient is Online, send it immediately
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(payload));
        }
        
        
    }

    private String getUserIdFromSession(WebSocketSession session) {
        try {
            URI uri = session.getUri();
            String query = uri.getQuery(); 
            if (query != null && query.contains("userId=")) {
                String[] params = query.split("&");
                for (String param : params) {
                    if (param.startsWith("userId=")) {
                        return param.split("=")[1];
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}