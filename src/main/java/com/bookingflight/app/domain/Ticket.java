package com.bookingflight.app.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Ticket")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "flightId")
    Flight flight;
    @ManyToOne
    @JoinColumn(name = "seatId")
    Seat seat;
    @ManyToOne
    @JoinColumn(name = "accountId", nullable = true)
    Account account;

    String passengerName;
    String passengerPhone;
    String passengerIDCard;
    String passengerEmail;
    @Builder.Default
    Boolean haveBaggage = false;
    @Builder.Default
    Boolean isBooked = false;
    int seatNumber;
    String urlImage;

    @Enumerated(EnumType.STRING)
    TicketStatus ticketStatus;
    LocalDateTime pickupAt;
}
