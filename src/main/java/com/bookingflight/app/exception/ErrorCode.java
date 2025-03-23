package com.bookingflight.app.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "User existed"),
    USER_NOT_EXISTED(1003, "User not existed"),
    UNIDENTIFIED_EXCEPTION(9999, "Unidentified exception"),
    PASSWORD_INVALID(1002, "Password must be at least 3 characters"),
    INCORRECT_PASSWORD(1004, "Incorrect password"),
    AIRPORT_EXISTED(1001, "Airport's name existed"),
    AIRPORT_NOT_EXISTED(1003, "Airport not existed"),
    EMAIL_EXISTED(1001, "Email existed"),
    SEATCLASS_EXISTED(1001, "Seatclass exists"),
    SEATCLASS_NOT_EXISTED(1003, "Seatclass not existed"),
    FLIGHT_EXISTED(1001, "Flight's number existed"),
    FLIGHT_NOT_EXISTED(1003, "Flight not existed"),
    BOOKING_EXISTED(1001, "Booking's id existed"),
    BOOKING_NOT_EXISTED(1003, "Booking not existed"),
    ACCOUNT_NOT_EXISTED(1003, "Account not existed"),
    SEATCLASS_NAME_EXISTED(1001, "Seatclass name existed"),
    PLANE_NOT_EXISTED(1003, "Plane not existed"),
    CUSTOMER_NOT_EXISTED(1003, "Customer not existed"),
    TICKET_EXISTED(1001, "Ticket number existed"),
    TICKET_NOT_EXISTED(1003, "Ticket number not existed"),;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
