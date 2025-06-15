package com.bookingflight.app.service;

import com.bookingflight.app.domain.Airline;
import com.bookingflight.app.domain.Plane;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.AirlineRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AirlinePopularityResponse;
import com.bookingflight.app.dto.response.AirlineResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AirlineMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.repository.AirlineRepository;
import com.bookingflight.app.repository.PlaneRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class AirlineService {

        private final PlaneRepository planeRepository;

        private final ResultPanigationMapper resultPanigationMapper;
        private final AirlineMapper airlineMapper;
        private final AirlineRepository airlineRepository;

        private final PlaneService planeService;

        public ResultPaginationDTO getAllAirline(Specification<Airline> spec, Pageable pageable) {
                Page<AirlineResponse> page = airlineRepository.findAll(spec, pageable)
                                .map(airlineMapper::toAirlineResponse);
                return resultPanigationMapper.toResultPanigationMapper(page);
        }

        public AirlineResponse getAirlineById(String id) {
                return airlineMapper.toAirlineResponse(airlineRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.AIRLINE_NOT_EXISTED)));
        }

        public AirlineResponse createAirline(AirlineRequest request) {
                Airline airline = airlineMapper.toAirline(request);
                if (airlineRepository.existsByAirlineCode(airline.getAirlineCode())) {
                        throw new AppException(ErrorCode.AIRLINE_EXISTED);
                }
                return airlineMapper.toAirlineResponse(airlineRepository.save(airline));
        }

        public AirlineResponse updateAirline(String id, AirlineRequest request) {
                Airline airline = airlineRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.AIRLINE_NOT_EXISTED));
                airlineMapper.updateAirline(airline, request);
                return airlineMapper.toAirlineResponse(airlineRepository.save(airline));
        }

        public void deleteAirline(String id) {
                Airline airline = airlineRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.AIRLINE_NOT_EXISTED));
                for (Plane plane : planeRepository.findAllByAirline(airline)) {
                        planeService.deletePlane(plane.getId());
                }
                airlineRepository.delete(airline);
        }

        public ResponseEntity<APIResponse<AirlinePopularityResponse>> getAirlinePopularity() {
                List<Object[]> ticketCounts = airlineRepository.countTicketsByAirline();
                long totalTickets = ticketCounts.stream()
                                .mapToLong(row -> ((Number) row[2]).longValue())
                                .sum();

                if (totalTickets == 0) {
                        return ResponseEntity.ok(APIResponse.<AirlinePopularityResponse>builder()
                                        .Code(200)
                                        .Message("No tickets sold")
                                        .data(new AirlinePopularityResponse(
                                                        new AirlinePopularityResponse.AirlineInfo("None", 0.0),
                                                        new AirlinePopularityResponse.AirlineInfo("None", 0.0),
                                                        new AirlinePopularityResponse.AirlineInfo("None", 0.0)))
                                        .build());
                }

                List<AirlinePopularityResponse.AirlineInfo> airlineInfos = new ArrayList<>();
                for (Object[] row : ticketCounts) {
                        String airlineName = (String) row[1];
                        long tickets = ((Number) row[2]).longValue();
                        double percentage = (tickets * 100.0) / totalTickets;
                        airlineInfos.add(new AirlinePopularityResponse.AirlineInfo(airlineName, percentage));
                }

                airlineInfos.sort(Comparator.comparingDouble(AirlinePopularityResponse.AirlineInfo::getPercentage)
                                .reversed());

                AirlinePopularityResponse responseData = new AirlinePopularityResponse();
                responseData.setAirline1(
                                airlineInfos.size() > 0 ? airlineInfos.get(0)
                                                : new AirlinePopularityResponse.AirlineInfo("None", 0.0));
                responseData.setAirline2(
                                airlineInfos.size() > 1 ? airlineInfos.get(1)
                                                : new AirlinePopularityResponse.AirlineInfo("None", 0.0));

                double otherPercentage = airlineInfos.size() > 2
                                ? airlineInfos.subList(2, airlineInfos.size()).stream()
                                                .mapToDouble(AirlinePopularityResponse.AirlineInfo::getPercentage).sum()
                                : 0.0;
                responseData.setOtherAirlines(
                                new AirlinePopularityResponse.AirlineInfo("Other Airlines", otherPercentage));

                APIResponse<AirlinePopularityResponse> response = APIResponse.<AirlinePopularityResponse>builder()
                                .Code(200)
                                .Message("Get airline popularity successfully")
                                .data(responseData)
                                .build();
                return ResponseEntity.ok(response);
        }
}
