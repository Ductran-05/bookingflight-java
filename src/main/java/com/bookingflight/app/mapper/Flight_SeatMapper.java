package com.bookingflight.app.mapper;

import org.mapstruct.Mapper;

import com.bookingflight.app.domain.Flight_Seat;
import com.bookingflight.app.dto.request.Flight_SeatRequest;
import com.bookingflight.app.dto.response.Flight_SeatResponse;

@Mapper(componentModel = "spring")
public interface Flight_SeatMapper {
    Flight_Seat toFlight_Seat(Flight_SeatRequest request);

    Flight_SeatResponse toFlight_SeatResponse(Flight_Seat entity);
}
