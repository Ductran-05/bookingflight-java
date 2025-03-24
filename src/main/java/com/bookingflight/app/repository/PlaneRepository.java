package com.bookingflight.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookingflight.app.domain.Plane;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, String> {

    Plane findByPlaneCode(String planeCode);

}
