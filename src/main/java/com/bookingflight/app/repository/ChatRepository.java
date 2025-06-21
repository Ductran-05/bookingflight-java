package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, String>, JpaSpecificationExecutor<Chat> {
    List<Chat> findBySessionOrderByCreatedAtAsc(String session);
    void deleteBySession(String session);
} 