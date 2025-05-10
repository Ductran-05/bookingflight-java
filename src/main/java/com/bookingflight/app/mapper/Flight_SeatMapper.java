package com.bookingflight.app.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.bookingflight.app.domain.Flight_Seat;
import com.bookingflight.app.dto.request.Flight_SeatRequest;
import com.bookingflight.app.dto.response.Flight_SeatResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.SeatRepository;

@Mapper(componentModel = "spring")
public interface Flight_SeatMapper {
        @Mapping(target = "flight", ignore = true)
        @Mapping(target = "seat", ignore = true)
        Flight_Seat toFlight_Seat(Flight_SeatRequest request,
                        @Context FlightRepository flightRepository,
                        @Context SeatRepository seatRepository);

        @AfterMapping
        default void setAttributes(Flight_SeatRequest request, @MappingTarget Flight_Seat flightSeat,
                        @Context FlightRepository flightRepository,
                        @Context SeatRepository seatRepository) {

                flightSeat.setFlight(flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED)));
                flightSeat.setSeat(seatRepository.findById(request.getSeatId())
                                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_EXISTED)));

        }

        // Chuyển từ Entity -> Response
        @Mapping(target = "flightId", source = "flight.id")
        @Mapping(target = "seatId", source = "seat.id")
        Flight_SeatResponse toFlight_SeatResponse(Flight_Seat entity,
                        @Context FlightRepository flightRepository,
                        @Context SeatRepository seatRepository);

        @AfterMapping
        default void setAttributesResponse(@MappingTarget Flight_SeatResponse response, Flight_Seat entity) {
                response.setFlightId(entity.getFlight().getId()); // Thêm thông tin mã chuyến bay
                response.setSeatId(entity.getSeat().getId()); // Thêm thông tin tên ghế
        }
}
