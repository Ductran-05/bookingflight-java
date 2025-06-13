package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {

    @NotNull(message = "Amount is required")
    @Min(value = 1000, message = "Amount must be at least 1000")
    Integer amount;

    @NotNull(message = "Order info is required")
    List<String> orderInfo;


    // Ngôn ngữ mặc định "vn", bạn có thể validate nếu cần chỉ nhận "vn", "en", ...
    @Pattern(regexp = "^(vn|en)?$", message = "Language must be 'vn' or 'en'")
    String language = "vn";
}
