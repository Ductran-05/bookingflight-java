package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.Min;
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

    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    String password;

    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    String email;

    @NotBlank(message = "Full name must not be blank")
    @Size(max = 50, message = "Full name must not exceed 50 characters")
    String fullName;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9}$", message = "Invalid phone number format")
    String phone;

    @NotNull(message = "Role must not be null")
    @Min(value = 1, message = "Role must be a positive number")
    Number role;
}
