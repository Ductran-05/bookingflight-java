-- City table inserts
INSERT INTO City (id, cityCode, cityName) VALUES
(UUID(), 'NYC', 'New York City'),
(UUID(), 'LON', 'London'),
(UUID(), 'TYO', 'Tokyo'),
(UUID(), 'PAR', 'Paris'),
(UUID(), 'DXB', 'Dubai'),
(UUID(), 'SIN', 'Singapore');

-- Airline table inserts
INSERT INTO Airline (id, airlineCode, airlineName) VALUES
(UUID(), 'UAL', 'United Airlines'),
(UUID(), 'DAL', 'Delta Air Lines'),
(UUID(), 'AAL', 'American Airlines'),
(UUID(), 'BAW', 'British Airways'),
(UUID(), 'SIA', 'Singapore Airlines'),
(UUID(), 'EMI', 'Emirates');

-- Airport table inserts (references City)
INSERT INTO Airport (id, airportCode, airportName, cityId) VALUES
(UUID(), 'JFK', 'John F. Kennedy International Airport', (SELECT id FROM City WHERE cityCode = 'NYC')),
(UUID(), 'LHR', 'London Heathrow Airport', (SELECT id FROM City WHERE cityCode = 'LON')),
(UUID(), 'HND', 'Tokyo Haneda Airport', (SELECT id FROM City WHERE cityCode = 'TYO')),
(UUID(), 'CDG', 'Charles de Gaulle Airport', (SELECT id FROM City WHERE cityCode = 'PAR')),
(UUID(), 'DXB', 'Dubai International Airport', (SELECT id FROM City WHERE cityCode = 'DXB')),
(UUID(), 'SIN', 'Singapore Changi Airport', (SELECT id FROM City WHERE cityCode = 'SIN'));

-- Plane table inserts (references Airline)
INSERT INTO Plane (id, planeCode, planeName, airlineId) VALUES
(UUID(), 'B737', 'Boeing 737-800', (SELECT id FROM Airline WHERE airlineCode = 'UAL')),
(UUID(), 'A320', 'Airbus A320', (SELECT id FROM Airline WHERE airlineCode = 'DAL')),
(UUID(), 'B777', 'Boeing 777-300ER', (SELECT id FROM Airline WHERE airlineCode = 'AAL')),
(UUID(), 'A380', 'Airbus A380', (SELECT id FROM Airline WHERE airlineCode = 'BAW')),
(UUID(), 'B787', 'Boeing 787 Dreamliner', (SELECT id FROM Airline WHERE airlineCode = 'SIA')),
(UUID(), 'A350', 'Airbus A350', (SELECT id FROM Airline WHERE airlineCode = 'EMI'));

-- Seat table inserts
INSERT INTO Seat (id, seatCode, seatName, price, description) VALUES
(UUID(), 'ECO-A', 'Economy Class A', 100, 'Standard Economy Seat'),
(UUID(), 'ECO-B', 'Economy Class B', 120, 'Economy Seat with Extra Legroom'),
(UUID(), 'BUS-A', 'Business Class A', 500, 'Standard Business Class Seat'),
(UUID(), 'BUS-B', 'Business Class B', 600, 'Business Class Window Seat'),
(UUID(), 'FST-A', 'First Class A', 1000, 'First Class Suite'),
(UUID(), 'FST-B', 'First Class B', 1200, 'First Class Suite with Shower');

-- Flight table inserts
INSERT INTO Flight (id, flightCode, planeId, departureAirportId, arrivalAirportId, departureTime, arrivalTime, originPrice) VALUES
(UUID(), 'FL001', (SELECT id FROM Plane WHERE planeCode = 'B737'), 
    (SELECT id FROM Airport WHERE airportCode = 'JFK'), 
    (SELECT id FROM Airport WHERE airportCode = 'LHR'), 
    STR_TO_DATE('10:00 20/03/2024', '%H:%i %d/%m/%Y'), 
    STR_TO_DATE('22:00 20/03/2024', '%H:%i %d/%m/%Y'), 
    500),
(UUID(), 'FL002', (SELECT id FROM Plane WHERE planeCode = 'A320'), (SELECT id FROM Airport WHERE airportCode = 'LHR'), (SELECT id FROM Airport WHERE airportCode = 'HND'), '2024-03-21 11:00:00', '2024-03-22 01:00:00', 600),
(UUID(), 'FL003', (SELECT id FROM Plane WHERE planeCode = 'B777'), (SELECT id FROM Airport WHERE airportCode = 'HND'), (SELECT id FROM Airport WHERE airportCode = 'CDG'), '2024-03-22 09:00:00', '2024-03-22 15:00:00', 450),
(UUID(), 'FL004', (SELECT id FROM Plane WHERE planeCode = 'A380'), (SELECT id FROM Airport WHERE airportCode = 'CDG'), (SELECT id FROM Airport WHERE airportCode = 'DXB'), '2024-03-23 14:00:00', '2024-03-24 02:00:00', 700),
(UUID(), 'FL005', (SELECT id FROM Plane WHERE planeCode = 'B787'), (SELECT id FROM Airport WHERE airportCode = 'DXB'), (SELECT id FROM Airport WHERE airportCode = 'SIN'), '2024-03-24 16:00:00', '2024-03-25 04:00:00', 550),
(UUID(), 'FL006', (SELECT id FROM Plane WHERE planeCode = 'A350'), (SELECT id FROM Airport WHERE airportCode = 'SIN'), (SELECT id FROM Airport WHERE airportCode = 'JFK'), '2024-03-25 08:00:00', '2024-03-25 20:00:00', 650);

-- Flight_Seat table inserts
INSERT INTO FlightSeat (id, flightId, seatId, available) VALUES
(UUID(), (SELECT id FROM Flight WHERE flightCode = 'FL001'), (SELECT id FROM Seat WHERE seatCode = 'ECO-A'), true),
(UUID(), (SELECT id FROM Flight WHERE flightCode = 'FL002'), (SELECT id FROM Seat WHERE seatCode = 'ECO-B'), true),
(UUID(), (SELECT id FROM Flight WHERE flightCode = 'FL003'), (SELECT id FROM Seat WHERE seatCode = 'BUS-A'), true),
(UUID(), (SELECT id FROM Flight WHERE flightCode = 'FL004'), (SELECT id FROM Seat WHERE seatCode = 'BUS-B'), true),
(UUID(), (SELECT id FROM Flight WHERE flightCode = 'FL005'), (SELECT id FROM Seat WHERE seatCode = 'FST-A'), true),
(UUID(), (SELECT id FROM Flight WHERE flightCode = 'FL006'), (SELECT id FROM Seat WHERE seatCode = 'FST-B'), true);

-- Flight_Airport table inserts (for intermediate stops)
INSERT INTO FlightAirport (id, flightId, airportId, arrivalTime, departureTime) VALUES
(UUID(), 
    (SELECT id FROM Flight WHERE flightCode = 'FL001'), 
    (SELECT id FROM Airport WHERE airportCode = 'HND'), 
    STR_TO_DATE('16:00 20/03/2024', '%H:%i %d/%m/%Y'),
    STR_TO_DATE('17:00 20/03/2024', '%H:%i %d/%m/%Y')
);