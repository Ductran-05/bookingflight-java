package com.bookingflight.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Seat;
import com.bookingflight.app.dto.request.SeatRequest;
import com.bookingflight.app.dto.response.SeatResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

// @Mapper(componentModel = "spring")
// public interface SeatMapper {
//     Seat toSeat(SeatRequest request);

//     SeatResponse toSeatResponse(Seat entity);

//     void updateSeat(@MappingTarget Seat seat, SeatRequest request);
// }
@Component
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatMapper {
    public Seat toSeat(SeatRequest request) {
        return Seat.builder()
                .seatCode(request.getSeatCode())
                .seatName(request.getSeatName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();
    }

    public SeatResponse toSeatResponse(Seat entity) {
        return SeatResponse.builder()
                .id(entity.getId())
                .seatCode(entity.getSeatCode())
                .seatName(entity.getSeatName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .build();
    }

    public void updateSeat(@MappingTarget Seat seat, SeatRequest request) {
        seat.setSeatCode(request.getSeatCode());
        seat.setSeatName(request.getSeatName());
        seat.setPrice(request.getPrice());
        seat.setDescription(request.getDescription());
    }
}