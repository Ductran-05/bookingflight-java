package com.bookingflight.app.service;

import com.bookingflight.app.domain.Chat;
import com.bookingflight.app.domain.ChatSession;
import com.bookingflight.app.dto.request.ChatRequest;
import com.bookingflight.app.dto.response.ChatResponse;
import com.bookingflight.app.dto.response.FlightResponse;
import com.bookingflight.app.dto.response.AirlineResponse;
import com.bookingflight.app.dto.response.AirportResponse;
import com.bookingflight.app.dto.response.CityResponse;
import com.bookingflight.app.dto.response.TicketResponse;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.ChatRepository;
import com.bookingflight.app.repository.ChatSessionRepository;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.google.genai.Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final Client client;
    
    // Inject the other services for data fetching
    private final FlightService flightService;
    private final AirlineService airlineService;
    private final AirportService airportService;
    private final CityService cityService;
    private final TicketService ticketService;

    @PostConstruct
    public void init() {
    }
    public ChatResponse sendMessage(ChatRequest request) {
        // Verify chat session exists
        ChatSession session = chatSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_SESSION_NOT_FOUND));

        // Get conversation history for context
        List<Chat> conversationHistory = chatRepository.findBySessionOrderByCreatedAtAsc(request.getSessionId());
        
        try {
            // Analyze the user prompt to understand what information they need
            String userPrompt = request.getPrompt().toLowerCase();
            String contextualData = extractRelevantData(userPrompt);
            
            // Create enhanced prompt with system context and relevant data
            String enhancedPrompt = buildEnhancedPrompt(request.getPrompt(), contextualData, conversationHistory);
            
            GenerateContentResponse aiResponse = client.models.generateContent(
                    "gemini-2.5-flash",
                    enhancedPrompt,
                    null
            );
            
            // Save the conversation
            Chat chat = Chat.builder()
                    .session(request.getSessionId())
                    .prompt(request.getPrompt())
                    .response(aiResponse.text())
                    .createdAt(LocalDateTime.now())
                    .build();
            
            chatRepository.save(chat);
            
            // Return response
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setPrompt(request.getPrompt());
            chatResponse.setResponse(aiResponse.text());
            
            return chatResponse;
            
        } catch (Exception e) {
            e.printStackTrace(); // or log it
            System.out.println("ERROR: " + e.getMessage());
            throw new AppException(ErrorCode.CHATBOT_ERROR);
        }
    }

    public ChatSession createChatSession(String ownerId) {
        ChatSession session = ChatSession.builder()
                .owner(ownerId)
                .createdAt(LocalDateTime.now())
                .build();
        return chatSessionRepository.save(session);
    }

    public List<Chat> getChatHistory(String sessionId) {
        // Verify session exists
        chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_SESSION_NOT_FOUND));
        
        return chatRepository.findBySessionOrderByCreatedAtAsc(sessionId);
    }

    public List<ChatSession> getAllChatSessions(String ownerId) {
        return chatSessionRepository.findByOwnerOrderByCreatedAtDesc(ownerId);
    }

    @Transactional
    public void deleteChatSession(String sessionId) {
        // Verify session exists
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_SESSION_NOT_FOUND));
        
        // Delete all chats in this session first
        chatRepository.deleteBySession(sessionId);
        
        // Then delete the session
        chatSessionRepository.delete(session);
    }

    /**
     * Enhanced data extraction with specific query handling
     */
    private String extractRelevantData(String userPrompt) {
        StringBuilder contextData = new StringBuilder();
        
        try {
            // Check for specific flight route queries
            String routeInfo = extractRouteQuery(userPrompt);
            if (!routeInfo.isEmpty()) {
                contextData.append("=== FLIGHT ROUTES ===\n");
                contextData.append(routeInfo);
                contextData.append("\n\n");
            }
            
            // Check for price-related queries
            if (containsPriceKeywords(userPrompt)) {
                contextData.append("=== PRICING INFORMATION ===\n");
                contextData.append(getPricingInformation());
                contextData.append("\n\n");
            }
            
            // Flight-related queries
            if (containsFlightKeywords(userPrompt)) {
                contextData.append("=== AVAILABLE FLIGHTS ===\n");
                contextData.append(getFlightInformation(userPrompt));
                contextData.append("\n\n");
            }
            
            // Airline-related queries
            if (containsAirlineKeywords(userPrompt)) {
                contextData.append("=== AIRLINES INFORMATION ===\n");
                contextData.append(getAirlineInformation());
                contextData.append("\n\n");
            }
            
            // Airport/City-related queries
            if (containsLocationKeywords(userPrompt)) {
                contextData.append("=== AIRPORTS AND CITIES ===\n");
                contextData.append(getLocationInformation());
                contextData.append("\n\n");
            }
            
            // Booking/Ticket-related queries
            if (containsBookingKeywords(userPrompt)) {
                contextData.append("=== BOOKING INFORMATION ===\n");
                contextData.append(getBookingInformation());
                contextData.append("\n\n");
            }
            
            // Help and general queries
            if (containsHelpKeywords(userPrompt)) {
                contextData.append("=== HELP INFORMATION ===\n");
                contextData.append(getHelpInformation());
                contextData.append("\n\n");
            }
            
        } catch (Exception e) {
            System.err.println("Error extracting relevant data: " + e.getMessage());
        }
        
        return contextData.toString();
    }

    private String extractRouteQuery(String userPrompt) {
        StringBuilder routeInfo = new StringBuilder();
        
        // Look for city/airport codes and names in the prompt
        List<String> possibleRoutes = findRoutePatterns(userPrompt);
        
        if (!possibleRoutes.isEmpty()) {
            routeInfo.append("Searching for flights on the following routes:\n");
            for (String route : possibleRoutes) {
                routeInfo.append("• ").append(route).append("\n");
            }
            
            // Get flights that might match these routes
            try {
                Pageable pageable = PageRequest.of(0, 10);
                ResultPaginationDTO flightResults = flightService.getAllFlights(null, pageable);
                
                @SuppressWarnings("unchecked")
                List<FlightResponse> flights = (List<FlightResponse>) flightResults.getResult();
                
                if (!flights.isEmpty()) {
                    routeInfo.append("\nMatching flights found:\n");
                    for (FlightResponse flight : flights) {
                        // Check if flight matches any of the route patterns
                        String flightRoute = flight.getDepartureAirportName() + " to " + flight.getArrivalAirportName();
                        for (String searchRoute : possibleRoutes) {
                            if (routeMatches(searchRoute, flightRoute, flight)) {
                                routeInfo.append(formatFlightDetails(flight));
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                routeInfo.append("Error retrieving route information.\n");
            }
        }
        
        return routeInfo.toString();
    }

    private List<String> findRoutePatterns(String prompt) {
        List<String> routes = new java.util.ArrayList<>();
        
        // Common patterns: "from X to Y", "X to Y", "X - Y", etc.
        Pattern[] patterns = {
            Pattern.compile("from\\s+([a-zA-Z\\s]+)\\s+to\\s+([a-zA-Z\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([a-zA-Z]{3})\\s+to\\s+([a-zA-Z]{3})", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([a-zA-Z\\s]+)\\s+to\\s+([a-zA-Z\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([a-zA-Z\\s]+)\\s*-\\s*([a-zA-Z\\s]+)", Pattern.CASE_INSENSITIVE)
        };
        
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(prompt);
            while (matcher.find()) {
                String origin = matcher.group(1).trim();
                String destination = matcher.group(2).trim();
                routes.add(origin + " → " + destination);
            }
        }
        
        return routes;
    }

    private boolean routeMatches(String searchRoute, String flightRoute, FlightResponse flight) {
        String[] searchParts = searchRoute.split("→");
        if (searchParts.length != 2) return false;
        
        String searchOrigin = searchParts[0].trim().toLowerCase();
        String searchDestination = searchParts[1].trim().toLowerCase();
        
        String flightOrigin = flight.getDepartureAirportName().toLowerCase();
        String flightDestination = flight.getArrivalAirportName().toLowerCase();
        
        return (flightOrigin.contains(searchOrigin) || searchOrigin.contains(flightOrigin)) &&
               (flightDestination.contains(searchDestination) || searchDestination.contains(flightDestination));
    }

    private String formatFlightDetails(FlightResponse flight) {
        return String.format(
            "  Flight %s: %s → %s\n" +
            "    Departure: %s | Arrival: %s\n" +
            "    Aircraft: %s | Price: $%.2f\n" +
            "    Status: %s\n\n",
            flight.getFlightCode(),
            flight.getDepartureAirportName(),
            flight.getArrivalAirportName(),
            flight.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
            flight.getArrivalTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
            flight.getPlaneName(),
            flight.getOriginPrice(),
            flight.getFlightStatus()
        );
    }

    private boolean containsPriceKeywords(String prompt) {
        String[] priceKeywords = {"price", "cost", "fare", "cheap", "expensive", "budget", "money", "dollar", "$"};
        return containsAnyKeyword(prompt, priceKeywords);
    }

    private boolean containsHelpKeywords(String prompt) {
        String[] helpKeywords = {"help", "how", "what", "can you", "assistance", "guide", "explain", "tell me"};
        return containsAnyKeyword(prompt, helpKeywords);
    }

    private boolean containsFlightKeywords(String prompt) {
        String[] flightKeywords = {"flight", "flights", "fly", "departure", "arrival", "schedule", "route", "journey"};
        return containsAnyKeyword(prompt, flightKeywords);
    }

    private boolean containsAirlineKeywords(String prompt) {
        String[] airlineKeywords = {"airline", "airlines", "carrier", "company", "operator"};
        return containsAnyKeyword(prompt, airlineKeywords);
    }

    private boolean containsLocationKeywords(String prompt) {
        String[] locationKeywords = {"airport", "airports", "city", "cities", "destination", "location", "from", "to", "where"};
        return containsAnyKeyword(prompt, locationKeywords);
    }

    private boolean containsBookingKeywords(String prompt) {
        String[] bookingKeywords = {"book", "booking", "reserve", "ticket", "tickets", "seat", "price", "cost", "fare"};
        return containsAnyKeyword(prompt, bookingKeywords);
    }

    private boolean containsAnyKeyword(String prompt, String[] keywords) {
        for (String keyword : keywords) {
            if (prompt.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private String getPricingInformation() {
        try {
            StringBuilder pricingInfo = new StringBuilder();
            
            // Get some flights to show price ranges
            Pageable pageable = PageRequest.of(0, 10);
            ResultPaginationDTO flightResults = flightService.getAllFlights(null, pageable);
            
            @SuppressWarnings("unchecked")
            List<FlightResponse> flights = (List<FlightResponse>) flightResults.getResult();
            
            if (!flights.isEmpty()) {
                double minPrice = flights.stream().mapToDouble(f -> f.getOriginPrice().doubleValue()).min().orElse(0);
                double maxPrice = flights.stream().mapToDouble(f -> f.getOriginPrice().doubleValue()).max().orElse(0);
                double avgPrice = flights.stream().mapToDouble(f -> f.getOriginPrice().doubleValue()).average().orElse(0);
                
                pricingInfo.append(String.format("PRICE RANGE:\n"));
                pricingInfo.append(String.format("• Minimum: $%.2f\n", minPrice));
                pricingInfo.append(String.format("• Maximum: $%.2f\n", maxPrice));
                pricingInfo.append(String.format("• Average: $%.2f\n\n", avgPrice));
                
                pricingInfo.append("SAMPLE FLIGHT PRICES:\n");
                flights.stream().limit(5).forEach(flight -> {
                    pricingInfo.append(String.format("• %s (%s → %s): $%.2f\n",
                        flight.getFlightCode(),
                        flight.getDepartureAirportName(),
                        flight.getArrivalAirportName(),
                        flight.getOriginPrice()));
                });
            } else {
                pricingInfo.append("No pricing information available at this time.\n");
            }
            
            pricingInfo.append("\nNOTE: Prices may vary based on:\n");
            pricingInfo.append("• Seat class (Economy, Business, First)\n");
            pricingInfo.append("• Booking date and travel date\n");
            pricingInfo.append("• Availability and demand\n");
            pricingInfo.append("• Additional services (baggage, meals, etc.)\n");
            
            return pricingInfo.toString();
        } catch (Exception e) {
            return "Unable to retrieve pricing information at this time.\n";
        }
    }

    private String getHelpInformation() {
        StringBuilder helpInfo = new StringBuilder();
        
        helpInfo.append("I can help you with:\n\n");
        helpInfo.append("🛫 FLIGHT INFORMATION:\n");
        helpInfo.append("• Search flights by route (e.g., \"flights from New York to London\")\n");
        helpInfo.append("• Check flight schedules and availability\n");
        helpInfo.append("• View flight prices and aircraft information\n\n");
        
        helpInfo.append("✈️ AIRLINES & AIRCRAFT:\n");
        helpInfo.append("• Information about available airlines\n");
        helpInfo.append("• Aircraft types and specifications\n");
        helpInfo.append("• Airline popularity and statistics\n\n");
        
        helpInfo.append("🏢 AIRPORTS & DESTINATIONS:\n");
        helpInfo.append("• Airport information and locations\n");
        helpInfo.append("• City and destination details\n");
        helpInfo.append("• Route availability\n\n");
        
        helpInfo.append("🎫 BOOKING ASSISTANCE:\n");
        helpInfo.append("• Booking guidelines and policies\n");
        helpInfo.append("• Seat selection information\n");
        helpInfo.append("• Pricing and fare information\n");
        helpInfo.append("• Baggage and additional services\n\n");
        
        helpInfo.append("💡 EXAMPLE QUESTIONS:\n");
        helpInfo.append("• \"Show me flights from Tokyo to Paris\"\n");
        helpInfo.append("• \"What airlines are available?\"\n");
        helpInfo.append("• \"How much do flights cost?\"\n");
        helpInfo.append("• \"What airports are in New York?\"\n");
        helpInfo.append("• \"How do I book a ticket?\"\n");
        
        return helpInfo.toString();
    }

    private String getFlightInformation(String userPrompt) {
        try {
            Pageable pageable = PageRequest.of(0, 10); // Get first 10 flights
            ResultPaginationDTO flightResults = flightService.getAllFlights(null, pageable);
            
            StringBuilder flightInfo = new StringBuilder();
            
            @SuppressWarnings("unchecked")
            List<FlightResponse> flights = (List<FlightResponse>) flightResults.getResult();
            
            if (flights.isEmpty()) {
                flightInfo.append("No flights are currently available.\n");
            } else {
                for (FlightResponse flight : flights) {
                    flightInfo.append(String.format(
                        "Flight %s: %s → %s\n" +
                        "  Departure: %s\n" +
                        "  Arrival: %s\n" +
                        "  Aircraft: %s\n" +
                        "  Price: $%.2f\n" +
                        "  Status: %s\n\n",
                        flight.getFlightCode(),
                        flight.getDepartureAirportName(),
                        flight.getArrivalAirportName(),
                        flight.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                        flight.getArrivalTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                        flight.getPlaneName(),
                        flight.getOriginPrice(),
                        flight.getFlightStatus()
                    ));
                }
            }
            
            return flightInfo.toString();
        } catch (Exception e) {
            return "Unable to retrieve flight information at this time.\n";
        }
    }

    private String getAirlineInformation() {
        try {
            Pageable pageable = PageRequest.of(0, 20);
            ResultPaginationDTO airlineResults = airlineService.getAllAirline(null, pageable);
            
            StringBuilder airlineInfo = new StringBuilder();
            
            @SuppressWarnings("unchecked")
            List<AirlineResponse> airlines = (List<AirlineResponse>) airlineResults.getResult();
            
            if (airlines.isEmpty()) {
                airlineInfo.append("No airline information available.\n");
            } else {
                for (AirlineResponse airline : airlines) {
                    airlineInfo.append(String.format(
                        "• %s (%s)\n",
                        airline.getAirlineName(),
                        airline.getAirlineCode()
                    ));
                }
            }
            
            return airlineInfo.toString();
        } catch (Exception e) {
            return "Unable to retrieve airline information at this time.\n";
        }
    }

    private String getLocationInformation() {
        try {
            StringBuilder locationInfo = new StringBuilder();
            
            // Get airports
            Pageable pageable = PageRequest.of(0, 20);
            ResultPaginationDTO airportResults = airportService.getAllAirports(null, pageable);
            
            @SuppressWarnings("unchecked")
            List<AirportResponse> airports = (List<AirportResponse>) airportResults.getResult();
            
            locationInfo.append("AIRPORTS:\n");
            if (airports.isEmpty()) {
                locationInfo.append("No airports available.\n");
            } else {
                for (AirportResponse airport : airports) {
                    locationInfo.append(String.format(
                        "• %s (%s) - %s\n",
                        airport.getAirportName(),
                        airport.getAirportCode(),
                        airport.getCityName()
                    ));
                }
            }
            
            // Get cities
            ResultPaginationDTO cityResults = cityService.getAllCities(null, pageable);
            
            @SuppressWarnings("unchecked")
            List<CityResponse> cities = (List<CityResponse>) cityResults.getResult();
            
            locationInfo.append("\nCITIES:\n");
            if (cities.isEmpty()) {
                locationInfo.append("No cities available.\n");
            } else {
                for (CityResponse city : cities) {
                    locationInfo.append(String.format(
                        "• %s (%s)\n",
                        city.getCityName(),
                        city.getCityCode()
                    ));
                }
            }
            
            return locationInfo.toString();
        } catch (Exception e) {
            return "Unable to retrieve location information at this time.\n";
        }
    }

    private String getBookingInformation() {
        try {
            StringBuilder bookingInfo = new StringBuilder();
            
            bookingInfo.append("BOOKING GUIDELINES:\n");
            bookingInfo.append("• Tickets can be booked for any available flight\n");
            bookingInfo.append("• Seat selection is available during booking\n");
            bookingInfo.append("• Prices vary by flight and seat class\n");
            bookingInfo.append("• Baggage options can be added during booking\n");
            bookingInfo.append("• Cancellation policies apply based on ticket type\n");
            bookingInfo.append("• Valid ID required for all passengers\n\n");
            
            bookingInfo.append("SEAT CLASSES AVAILABLE:\n");
            bookingInfo.append("• Economy Class: Standard seating with basic amenities\n");
            bookingInfo.append("• Business Class: Enhanced comfort and premium services\n");
            bookingInfo.append("• First Class: Luxury experience with finest amenities\n");
            
            return bookingInfo.toString();
        } catch (Exception e) {
            return "Unable to retrieve booking information at this time.\n";
        }
    }

    private String buildEnhancedPrompt(String originalPrompt, String contextualData, List<Chat> conversationHistory) {
        StringBuilder enhancedPrompt = new StringBuilder();
        
        // System context
        enhancedPrompt.append("You are a helpful flight booking assistant. ");
        enhancedPrompt.append("You have access to real-time flight information and can help users with:\n");
        enhancedPrompt.append("- Finding flights between destinations\n");
        enhancedPrompt.append("- Providing airline information\n");
        enhancedPrompt.append("- Airport and city details\n");
        enhancedPrompt.append("- Booking assistance and guidelines\n");
        enhancedPrompt.append("- Flight schedules and prices\n\n");
        
        // Add contextual data if available
        if (!contextualData.trim().isEmpty()) {
            enhancedPrompt.append("CURRENT SYSTEM DATA:\n");
            enhancedPrompt.append(contextualData);
            enhancedPrompt.append("\n");
        }
        
        // Add conversation history for context (last 3 exchanges)
        if (!conversationHistory.isEmpty()) {
            enhancedPrompt.append("RECENT CONVERSATION:\n");
            int start = Math.max(0, conversationHistory.size() - 6); // Last 3 exchanges (6 messages)
            for (int i = start; i < conversationHistory.size(); i++) {
                Chat chat = conversationHistory.get(i);
                enhancedPrompt.append("User: ").append(chat.getPrompt()).append("\n");
                enhancedPrompt.append("Assistant: ").append(chat.getResponse()).append("\n");
            }
            enhancedPrompt.append("\n");
        }
        
        // Instructions for response
        enhancedPrompt.append("Please provide a helpful, accurate response based on the available data. ");
        enhancedPrompt.append("If specific flight information is requested but not available in the data, ");
        enhancedPrompt.append("explain what information is available and suggest how the user can get more specific details. ");
        enhancedPrompt.append("Always be friendly and professional.\n\n");
        
        // Current user prompt
        enhancedPrompt.append("CURRENT USER QUESTION: ");
        enhancedPrompt.append(originalPrompt);
        
        return enhancedPrompt.toString();
    }

}