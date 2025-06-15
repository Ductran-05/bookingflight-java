package com.bookingflight.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@lombok.Data
@lombok.NoArgsConstructor
@lombok.ToString
@Builder
public class APIResponse<T> {
    private int Code;
    private String Message;
    private T data;
}
