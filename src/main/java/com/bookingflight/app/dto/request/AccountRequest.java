package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class AccountRequest {

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    String password;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email is invalid")
    String email;

    @NotBlank(message = "Full name is required")
    String fullName;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9}$", message = "Phone is invalid")
    String phone;

    @Size(max = 255, message = "Avatar is too long")
    String avatar;

    @NotNull(message = "Role is required")
    String roleId;
}
