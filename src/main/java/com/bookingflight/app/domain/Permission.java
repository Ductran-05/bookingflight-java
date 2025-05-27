package com.bookingflight.app.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;
    String apiPath;
    String method;
    String model;
    
    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;
    
    @UpdateTimestamp
    LocalDateTime updatedAt;
    
    @Builder.Default
    @Column(nullable = false)
    Boolean isDeleted = false;
    
    LocalDateTime deletedAt;
}
