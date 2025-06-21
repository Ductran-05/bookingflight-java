package com.bookingflight.app.controller;

import com.bookingflight.app.domain.Chat;
import com.bookingflight.app.domain.ChatSession;
import com.bookingflight.app.dto.request.ChatRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.ChatResponse;
import com.bookingflight.app.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatService chatService;

    @PostMapping("/message")
    public ResponseEntity<APIResponse<ChatResponse>> sendMessage(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatService.sendMessage(request);
        APIResponse<ChatResponse> apiResponse = APIResponse.<ChatResponse>builder()
                .Code(200)
                .Message("Message sent successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/session")
    public ResponseEntity<APIResponse<ChatSession>> createChatSession(@RequestParam String ownerId) {
        ChatSession session = chatService.createChatSession(ownerId);
        APIResponse<ChatSession> apiResponse = APIResponse.<ChatSession>builder()
                .Code(201)
                .Message("Chat session created successfully")
                .data(session)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/history/{sessionId}")
    public ResponseEntity<APIResponse<List<Chat>>> getChatHistory(@PathVariable String sessionId) {
        List<Chat> history = chatService.getChatHistory(sessionId);
        APIResponse<List<Chat>> apiResponse = APIResponse.<List<Chat>>builder()
                .Code(200)
                .Message("Chat history retrieved successfully")
                .data(history)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/sessions/{ownerId}")
    public ResponseEntity<APIResponse<List<ChatSession>>> getAllChatSessions(@PathVariable String ownerId) {
        List<ChatSession> sessions = chatService.getAllChatSessions(ownerId);
        APIResponse<List<ChatSession>> apiResponse = APIResponse.<List<ChatSession>>builder()
                .Code(200)
                .Message("Chat sessions retrieved successfully")
                .data(sessions)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<APIResponse<String>> deleteChatSession(@PathVariable String sessionId) {
        chatService.deleteChatSession(sessionId);
        APIResponse<String> apiResponse = APIResponse.<String>builder()
                .Code(200)
                .Message("Chat session and all associated chats deleted successfully")
                .data("Session deleted")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
} 