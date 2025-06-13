package com.bookingflight.app.domain;

public enum TicketStatus {
    AVAILABLE, // Chưa được đặt
    BOOKED, // Đã được đặt (chờ thanh toán hoặc đang giữ chỗ)
    BOARDING, // Đang bay
    CANCELLED, // Đã huỷ
    USED // Đã sử dụng
}
