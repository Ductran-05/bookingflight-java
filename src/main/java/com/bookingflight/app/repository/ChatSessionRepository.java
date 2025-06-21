package com.bookingflight.app.repository;

import com.bookingflight.app.domain.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, String>, JpaSpecificationExecutor<ChatSession> {
    List<ChatSession> findByOwnerOrderByCreatedAtDesc(String owner);
}
