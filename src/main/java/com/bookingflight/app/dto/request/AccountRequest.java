package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {

    @NotBlank(message = "PASSWORD_IS_REQUIRED")
    @Size(min = 6, max = 50, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "EMAIL_IS_REQUIRED")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "FULL_NAME_IS_REQUIRED")
    @Size(max = 50, message = "FULL_NAME_INVALID")
    String fullName;

    @NotBlank(message = "PHONE_IS_REQUIRED")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9}$", message = "PHONE_INVALID")
    String phone;

    @Size(max = 255, message = "AVATAR_URL_TOO_LONG")
    String avatar;

    @NotNull(message = "ROLE_IS_REQUIRED")
    String roleId;
}
