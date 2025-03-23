// package com.bookingflight.app.service;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.stereotype.Service;

// import com.bookingflight.app.dto.request.SeatRequest;
// import com.bookingflight.app.dto.response.SeatResponse;
// import com.bookingflight.app.exception.AppException;
// import com.bookingflight.app.exception.ErrorCode;
// import com.bookingflight.app.mapper.SeatMapper;
// import com.bookingflight.app.repository.SeatRepository;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class SeatService {
// private final SeatRepository seatRepository;
// private final SeatMapper seatMapper;

// public List<SeatResponse> getAllSeats() {
// return seatRepository.findAll().stream()
// .map(seatMapper::toSeatResponse)
// .collect(Collectors.toList());
// }

// public SeatResponse getSeatById(String id) {
// return seatMapper.toSeatResponse(seatRepository.findById(id)
// .orElseThrow(() -> new AppException(ErrorCode.SEATCLASS_NOT_EXISTED)));
// }

// public SeatResponse createSeat(SeatRequest request) {
// if (seatRepository) {
// throw new AppException(ErrorCode.SEATCLASS_EXISTED);
// }
// return
// seatMapper.toSeatResponse(seatRepository.save(seatMapper.toSeat(request)));
// }
// }
