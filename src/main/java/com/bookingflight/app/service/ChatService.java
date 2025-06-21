package com.bookingflight.app.service;

import com.bookingflight.app.domain.Chat;
import com.bookingflight.app.domain.ChatSession;
import com.bookingflight.app.dto.request.ChatRequest;
import com.bookingflight.app.dto.response.ChatResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.ChatRepository;
import com.bookingflight.app.repository.ChatSessionRepository;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.genai.Client;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final Client client;

    @PostConstruct
    public void init() {
    }
    public ChatResponse sendMessage(ChatRequest request) {
        // Verify chat session exists
        ChatSession session = chatSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_SESSION_NOT_FOUND));

        // Get conversation history for context
        List<Chat> conversationHistory = chatRepository.findBySessionOrderByCreatedAtAsc(request.getSessionId());
        System.out.print("beginning0");

        try {
            GenerateContentResponse aiResponse = client.models.generateContent(
                    "gemini-2.5-flash",
                    request.getPrompt(),
                    null
            );
            // For demo purposes, create a simple AI-like response
            System.out.print("beginning1");

            System.out.print(aiResponse);
            
            // Save the conversation
            Chat chat = Chat.builder()
                    .session(request.getSessionId())
                    .prompt(request.getPrompt())
                    .response(aiResponse.text())
                    .createdAt(LocalDateTime.now())
                    .build();
            
            chatRepository.save(chat);
            
            // Return response
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setPrompt(request.getPrompt());
            chatResponse.setResponse(aiResponse.text());
            
            return chatResponse;
            
        } catch (Exception e) {
            e.printStackTrace(); // or log it
            System.out.println("ERROR: " + e.getMessage());
            throw new AppException(ErrorCode.CHATBOT_ERROR);
        }
    }

    public ChatSession createChatSession(String ownerId) {
        ChatSession session = ChatSession.builder()
                .owner(ownerId)
                .createdAt(LocalDateTime.now())
                .build();
        return chatSessionRepository.save(session);
    }

    public List<Chat> getChatHistory(String sessionId) {
        // Verify session exists
        chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_SESSION_NOT_FOUND));
        
        return chatRepository.findBySessionOrderByCreatedAtAsc(sessionId);
    }

    public List<ChatSession> getAllChatSessions(String ownerId) {
        return chatSessionRepository.findByOwnerOrderByCreatedAtDesc(ownerId);
    }

    @Transactional
    public void deleteChatSession(String sessionId) {
        // Verify session exists
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_SESSION_NOT_FOUND));
        
        // Delete all chats in this session first
        chatRepository.deleteBySession(sessionId);
        
        // Then delete the session
        chatSessionRepository.delete(session);
    }

} 