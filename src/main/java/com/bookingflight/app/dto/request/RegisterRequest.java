package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.NotBlank;
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
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    // Optional: thêm regex nếu muốn password có ít nhất 1 chữ số, 1 chữ hoa, ...
    // @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "Password must
    // contain at least one uppercase letter and one digit")
    String password;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    String email;

    @NotBlank(message = "Full name is required")
    @Size(max = 50, message = "Full name must be at most 50 characters")
    String fullName;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9}$", message = "Invalid phone number format")
    String phone;

}
