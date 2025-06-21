package com.bookingflight.app.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Chat")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @JoinColumn(name = "chatSessionId")
    String session;

    @Column(columnDefinition = "TEXT")
    String prompt;

    @Column(columnDefinition = "LONGTEXT")
    String response;

    @Column(nullable = false)
    LocalDateTime createdAt;

}
