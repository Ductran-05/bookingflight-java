package com.bookingflight.app.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    @OneToOne
    @JoinColumn(name = "flightId")
    Flight flight;
    @OneToOne
    @JoinColumn(name = "seatId")
    Seat seat;
    String passengerName;
    String passengerPhone;
    String passengerIDCard;
    String passengerEmail;
    Boolean haveBaggage;
}
