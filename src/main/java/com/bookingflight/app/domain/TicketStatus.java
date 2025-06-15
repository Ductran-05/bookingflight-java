package com.bookingflight.app.domain;

public enum TicketStatus {
    AVAILABLE, // Chưa được đặt
    BOOKED, // Đã được đặt
    BOARDING, // Đang bay
    CANCELLED, // Đã huỷ
    USED // Đã sử dụng
}
