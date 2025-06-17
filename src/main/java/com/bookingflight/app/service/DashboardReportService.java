package com.bookingflight.app.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Flight_Seat;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.domain.TicketStatus;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.DashboardResponse;
import com.bookingflight.app.dto.response.YearlyTicketResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.AirlineRepository;
import com.bookingflight.app.repository.AirportRepository;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.Flight_SeatRepository;
import com.bookingflight.app.repository.TicketRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardReportService {
        final FlightRepository flightRepository;
        final TicketRepository ticketRepository;
        final AirlineRepository airlineRepository;
        final AirportRepository airportRepository;
        final Flight_SeatRepository flightSeatRepository;

        // Read Dashboard Report (GET /dashboard)
        public ResponseEntity<APIResponse<DashboardResponse>> getDashboardReport() {
                DashboardResponse dashboardResponse = getDashboardData();
                APIResponse<DashboardResponse> response = APIResponse.<DashboardResponse>builder()
                                .Code(200)
                                .Message("Dashboard report retrieved successfully")
                                .data(dashboardResponse)
                                .build();
                return ResponseEntity.ok(response);
        }

        // Read Dashboard Report by Year (GET /dashboard/{year})
        public ResponseEntity<APIResponse<YearlyTicketResponse>> getYearlyTicketReport(int year) {
                // Validation: Only allow years up to current year
                int currentYear = LocalDate.now().getYear();
                if (year > currentYear) {
                        throw new AppException(ErrorCode.INVALID_REPORT_DATE);
                }

                int maxMonth = (year == currentYear) ? LocalDate.now().getMonthValue() : 12;

                List<YearlyTicketResponse.MonthlyTicketData> monthlyData = new ArrayList<>();
                for (int month = 1; month <= maxMonth; month++) {
                        LocalDate startDate = LocalDate.of(year, month, 1);
                        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

                        // Find flights in the month
                        List<Flight> flightsInMonth = flightRepository.findAll().stream()
                                        .filter(flight -> {
                                                LocalDate departureDate = flight.getDepartureTime().toLocalDate();
                                                return departureDate != null &&
                                                                !departureDate.isBefore(startDate) &&
                                                                !departureDate.isAfter(endDate);
                                        })
                                        .toList();

                        int maxTickets = 0;
                        int ticketsSold = 0;
                        for (Flight flight : flightsInMonth) {
                                // Calculate tickets sold
                                List<Ticket> tickets = ticketRepository.findAll().stream()
                                                .filter(ticket -> {
                                                        return ticket.getFlight().getId().equals(flight.getId())
                                                                        && ticket.getTicketStatus()
                                                                                        .equals(TicketStatus.USED);
                                                })
                                                .toList();
                                ticketsSold += tickets.size(); // All tickets are sold

                                // Calculate max tickets (total quantity from Flight_Seat)
                                List<Flight_Seat> flightSeats = flightSeatRepository.findAllByFlightId(flight.getId());
                                maxTickets += flightSeats.stream()
                                                .mapToInt(Flight_Seat::getQuantity)
                                                .sum();
                        }

                        monthlyData.add(YearlyTicketResponse.MonthlyTicketData.builder()
                                        .month(month)
                                        .maxTickets(maxTickets)
                                        .ticketsSold(ticketsSold)
                                        .build());
                }

                YearlyTicketResponse yearlyTicketResponse = YearlyTicketResponse.builder()
                                .year(year)
                                .months(monthlyData)
                                .build();

                APIResponse<YearlyTicketResponse> response = APIResponse.<YearlyTicketResponse>builder()
                                .Code(200)
                                .Message("Yearly ticket report retrieved successfully")
                                .data(yearlyTicketResponse)
                                .build();
                return ResponseEntity.ok(response);
        }

        // Helper method to calculate dashboard data
        private DashboardResponse getDashboardData() {
                int currentYear = LocalDate.now().getYear();
                int lastYear = currentYear - 1;

                // Calculate revenue and flight count for this year
                LocalDate thisYearStart = LocalDate.of(currentYear, 1, 1);
                LocalDate thisYearEnd = LocalDate.of(currentYear, 12, 31);
                List<Flight> flightsThisYear = flightRepository.findAll().stream()
                                .filter(flight -> {
                                        LocalDate departureDate = flight.getDepartureTime().toLocalDate();
                                        return departureDate != null &&
                                                        !departureDate.isBefore(thisYearStart) &&
                                                        !departureDate.isAfter(thisYearEnd);
                                })
                                .toList();

                double revenueThisYear = 0;
                for (Flight flight : flightsThisYear) {
                        List<Ticket> tickets = ticketRepository.findAll().stream()
                                        .filter(ticket -> ticket.getFlight().getId().equals(flight.getId()))
                                        .toList();
                        revenueThisYear += tickets.stream()
                                        .mapToDouble(ticket -> ticket.getSeat().getPrice().doubleValue())
                                        .sum();
                }

                // Calculate revenue and flight count for last year
                LocalDate lastYearStart = LocalDate.of(lastYear, 1, 1);
                LocalDate lastYearEnd = LocalDate.of(lastYear, 12, 31);
                List<Flight> flightsLastYear = flightRepository.findAll().stream()
                                .filter(flight -> {
                                        LocalDate departureDate = flight.getDepartureTime().toLocalDate();
                                        return departureDate != null &&
                                                        !departureDate.isBefore(lastYearStart) &&
                                                        !departureDate.isAfter(lastYearEnd);
                                })
                                .toList();

                double revenueLastYear = 0;
                for (Flight flight : flightsLastYear) {
                        List<Ticket> tickets = ticketRepository.findAll().stream()
                                        .filter(ticket -> ticket.getFlight().getId().equals(flight.getId()))
                                        .toList();
                        revenueLastYear += tickets.stream()
                                        .mapToDouble(ticket -> ticket.getSeat().getPrice().doubleValue())
                                        .sum();
                }

                // Calculate revenue and flight count for this month (May 2025)
                LocalDate thisMonthStart = LocalDate.of(currentYear, 5, 1);
                LocalDate thisMonthEnd = LocalDate.of(currentYear, 5, 31);
                List<Flight> flightsThisMonth = flightRepository.findAll().stream()
                                .filter(flight -> {
                                        LocalDate departureDate = flight.getDepartureTime().toLocalDate();
                                        return departureDate != null &&
                                                        !departureDate.isBefore(thisMonthStart) &&
                                                        !departureDate.isAfter(thisMonthEnd);
                                })
                                .toList();

                double revenueThisMonth = 0;
                for (Flight flight : flightsThisMonth) {
                        List<Ticket> tickets = ticketRepository.findAll().stream()
                                        .filter(ticket -> ticket.getFlight().getId().equals(flight.getId()))
                                        .toList();
                        revenueThisMonth += tickets.stream()
                                        .mapToDouble(ticket -> ticket.getSeat().getPrice().doubleValue())
                                        .sum();
                }

                // Calculate revenue and flight count for last month (April 2025)
                LocalDate lastMonthStart = LocalDate.of(currentYear, 4, 1);
                LocalDate lastMonthEnd = LocalDate.of(currentYear, 4, 30);
                List<Flight> flightsLastMonth = flightRepository.findAll().stream()
                                .filter(flight -> {
                                        LocalDate departureDate = flight.getArrivalTime().toLocalDate();
                                        return departureDate != null &&
                                                        !departureDate.isBefore(lastMonthStart) &&
                                                        !departureDate.isAfter(lastMonthEnd);
                                })
                                .toList();

                double revenueLastMonth = 0;
                for (Flight flight : flightsLastMonth) {
                        List<Ticket> tickets = ticketRepository.findAll().stream()
                                        .filter(ticket -> ticket.getFlight().getId().equals(flight.getId()))
                                        .toList();
                        revenueLastMonth += tickets.stream()
                                        .mapToDouble(ticket -> ticket.getSeat().getPrice().doubleValue())
                                        .sum();
                }

                // Count airlines and airports
                int airlineCount = (int) airlineRepository.count();
                int airportCount = (int) airportRepository.count();

                // Calculate airline popularity based on all tickets
                Map<String, Integer> ticketCountByAirline = new HashMap<>();
                int totalTicketsSold = 0;
                List<Ticket> allTickets = ticketRepository.findAll();
                for (Ticket ticket : allTickets) {
                        Flight flight = ticket.getFlight();
                        String airlineName;
                        try {
                                airlineName = flight.getPlane().getAirline().getAirlineName();
                        } catch (NullPointerException e) {
                                airlineName = "Unknown"; // Handle cases where airline is null
                        }
                        ticketCountByAirline.merge(airlineName, 1, Integer::sum);
                        totalTicketsSold++;
                }

                // Calculate airline popularity
                Map<String, Double> airlinePopularity = new LinkedHashMap<>();
                if (totalTicketsSold == 0) {
                        // If no tickets sold, return empty popularity to indicate no data
                        airlinePopularity.put("No Data", 0.0);
                } else {
                        List<Map.Entry<String, Integer>> sortedAirlines = ticketCountByAirline.entrySet().stream()
                                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                                        .collect(Collectors.toList());

                        int topCount = Math.min(3, sortedAirlines.size());
                        double otherPercentage = 0.0;
                        for (int i = 0; i < sortedAirlines.size(); i++) {
                                String airlineName = sortedAirlines.get(i).getKey();
                                int ticketCount = sortedAirlines.get(i).getValue();
                                double percentage = (ticketCount * 100.0 / totalTicketsSold);
                                if (i < topCount) {
                                        airlinePopularity.put(airlineName, percentage);
                                } else {
                                        otherPercentage += percentage;
                                }
                        }
                        if (otherPercentage > 0) {
                                airlinePopularity.put("Other", otherPercentage);
                        }
                }

                return DashboardResponse.builder()
                                .revenueThisYear(revenueThisYear)
                                .revenueLastYear(revenueLastYear)
                                .revenueThisMonth(revenueThisMonth)
                                .revenueLastMonth(revenueLastMonth)
                                .flightCountThisYear(flightsThisYear.size())
                                .flightCountLastYear(flightsLastYear.size())
                                .flightCountThisMonth(flightsThisMonth.size())
                                .flightCountLastMonth(flightsLastMonth.size())
                                .airlineCount(airlineCount)
                                .airportCount(airportCount)
                                .airlinePopularity(airlinePopularity)
                                .build();
        }
}