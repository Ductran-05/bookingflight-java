package com.bookingflight.app.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {

    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must be at most 50 characters")
    String roleName;

    @NotEmpty(message = "Permission list must not be empty")
    List<@NotBlank(message = "Permission ID must not be blank") String> permissionId;
}
