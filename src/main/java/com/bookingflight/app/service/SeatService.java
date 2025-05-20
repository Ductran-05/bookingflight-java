package com.bookingflight.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Seat;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.SeatRequest;
import com.bookingflight.app.dto.response.SeatResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.mapper.SeatMapper;
import com.bookingflight.app.repository.SeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final ResultPanigationMapper resultPanigationMapper;

    public ResultPaginationDTO getAllSeats(Specification<Seat> spec, Pageable pageable) {
        Page<SeatResponse> page = seatRepository.findAll(spec, pageable)
                .map(seatMapper::toSeatResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }

    public SeatResponse getSeatById(String id) {
        return seatMapper.toSeatResponse(seatRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_EXISTED)));
    }

    public SeatResponse createSeat(SeatRequest request) {
        if (seatRepository.getSeatBySeatCode(request.getSeatCode()) != null) {
            throw new AppException(ErrorCode.SEAT_EXISTED);
        }
        return seatMapper.toSeatResponse(seatRepository.save(seatMapper.toSeat(request)));
    }

    public SeatResponse updateSeat(String id, SeatRequest request) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_EXISTED));
        seatMapper.updateSeat(seat, request);
        return seatMapper.toSeatResponse(seatRepository.save(seat));
    }

    public void deleteSeat(String id) {
        seatRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_EXISTED));
        seatRepository.deleteById(id);
    }
}
