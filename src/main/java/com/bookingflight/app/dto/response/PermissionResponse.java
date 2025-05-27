package com.bookingflight.app.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    String id;

    String name;
    String apiPath;
    String method;
    String model;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isDeleted;
    LocalDateTime deletedAt;
}
