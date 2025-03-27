package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.dto.request.TicketRequest;
import com.bookingflight.app.dto.response.TicketResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toTicket(TicketRequest ticketRequest);
    TicketResponse toTicketResponse(Ticket ticket);
}
