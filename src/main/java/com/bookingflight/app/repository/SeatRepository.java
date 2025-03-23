package com.bookingflight.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookingflight.app.domain.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {

}
