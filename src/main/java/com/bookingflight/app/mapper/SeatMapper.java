package com.bookingflight.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.bookingflight.app.domain.Seat;
import com.bookingflight.app.dto.request.SeatRequest;
import com.bookingflight.app.dto.response.SeatResponse;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat toSeat(SeatRequest request);

    SeatResponse toSeatResponse(Seat entity);

    void updateSeat(@MappingTarget Seat seat, SeatRequest request);
}
