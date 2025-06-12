package com.bookingflight.app.domain;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "Seat")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String seatCode;
    String seatName;
    BigDecimal price;
    String description;
}
