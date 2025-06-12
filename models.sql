-- Use the flight booking database
USE `bookingflight-java`;

-- City table inserts
INSERT INTO city (id, city_code, city_name) VALUES
(UUID(), 'NYC', 'New York City'),
(UUID(), 'LON', 'London'),
(UUID(), 'TYO', 'Tokyo'),
(UUID(), 'PAR', 'Paris'),
(UUID(), 'DXB', 'Dubai'),
(UUID(), 'SIN', 'Singapore');

-- Airline table inserts
INSERT INTO airline (id, airline_code, airline_name) VALUES
(UUID(), 'UAL', 'United Airlines'),
(UUID(), 'DAL', 'Delta Air Lines'),
(UUID(), 'AAL', 'American Airlines'),
(UUID(), 'BAW', 'British Airways'),
(UUID(), 'SIA', 'Singapore Airlines'),
(UUID(), 'EMI', 'Emirates');

-- Airport table inserts (references city)
INSERT INTO airport (id, airport_code, airport_name, city_id) VALUES
(UUID(), 'JFK', 'John F. Kennedy International Airport', (SELECT id FROM city WHERE city_code = 'NYC')),
(UUID(), 'LHR', 'London Heathrow Airport', (SELECT id FROM city WHERE city_code = 'LON')),
(UUID(), 'HND', 'Tokyo Haneda Airport', (SELECT id FROM city WHERE city_code = 'TYO')),
(UUID(), 'CDG', 'Charles de Gaulle Airport', (SELECT id FROM city WHERE city_code = 'PAR')),
(UUID(), 'DXB', 'Dubai International Airport', (SELECT id FROM city WHERE city_code = 'DXB')),
(UUID(), 'SIN', 'Singapore Changi Airport', (SELECT id FROM city WHERE city_code = 'SIN'));

-- Plane table inserts (references airline)
INSERT INTO plane (id, plane_code, plane_name, airline_id) VALUES
(UUID(), 'B737', 'Boeing 737-800', (SELECT id FROM airline WHERE airline_code = 'UAL')),
(UUID(), 'A320', 'Airbus A320', (SELECT id FROM airline WHERE airline_code = 'DAL')),
(UUID(), 'B777', 'Boeing 777-300ER', (SELECT id FROM airline WHERE airline_code = 'AAL')),
(UUID(), 'A380', 'Airbus A380', (SELECT id FROM airline WHERE airline_code = 'BAW')),
(UUID(), 'B787', 'Boeing 787 Dreamliner', (SELECT id FROM airline WHERE airline_code = 'SIA')),
(UUID(), 'A350', 'Airbus A350', (SELECT id FROM airline WHERE airline_code = 'EMI'));

-- Seat table inserts
INSERT INTO seat (id, seat_code, seat_name, price, description) VALUES
(UUID(), 'ECO-A', 'Economy Class A', 100, 'Standard Economy Seat'),
(UUID(), 'ECO-B', 'Economy Class B', 120, 'Economy Seat with Extra Legroom'),
(UUID(), 'BUS-A', 'Business Class A', 500, 'Standard Business Class Seat'),
(UUID(), 'BUS-B', 'Business Class B', 600, 'Business Class Window Seat'),
(UUID(), 'FST-A', 'First Class A', 1000, 'First Class Suite'),
(UUID(), 'FST-B', 'First Class B', 1200, 'First Class Suite with Shower');

-- Flight table inserts with current date + 1 (fixed syntax)
INSERT INTO flight (id, flight_code, plane_id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, origin_price) VALUES
(UUID(), 'FL001', 
    (SELECT id FROM plane WHERE plane_code = 'B737'), 
    (SELECT id FROM airport WHERE airport_code = 'JFK'), 
    (SELECT id FROM airport WHERE airport_code = 'LHR'), 
    DATE_ADD(NOW(), INTERVAL 1 DAY), 
    DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 12 HOUR), 
    500),

(UUID(), 'FL002', 
    (SELECT id FROM plane WHERE plane_code = 'A320'), 
    (SELECT id FROM airport WHERE airport_code = 'LHR'), 
    (SELECT id FROM airport WHERE airport_code = 'HND'), 
    DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 2 HOUR), 
    DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 3 HOUR), 
    600),

(UUID(), 'FL003', 
    (SELECT id FROM plane WHERE plane_code = 'B777'), 
    (SELECT id FROM airport WHERE airport_code = 'HND'), 
    (SELECT id FROM airport WHERE airport_code = 'CDG'), 
    DATE_ADD(NOW(), INTERVAL 2 DAY), 
    DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 6 HOUR), 
    450),

(UUID(), 'FL004', 
    (SELECT id FROM plane WHERE plane_code = 'A380'), 
    (SELECT id FROM airport WHERE airport_code = 'CDG'), 
    (SELECT id FROM airport WHERE airport_code = 'DXB'), 
    DATE_ADD(NOW(), INTERVAL 3 DAY), 
    DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 12 HOUR), 
    700),

(UUID(), 'FL005', 
    (SELECT id FROM plane WHERE plane_code = 'B787'), 
    (SELECT id FROM airport WHERE airport_code = 'DXB'), 
    (SELECT id FROM airport WHERE airport_code = 'SIN'), 
    DATE_ADD(NOW(), INTERVAL 4 DAY), 
    DATE_ADD(DATE_ADD(NOW(), INTERVAL 4 DAY), INTERVAL 12 HOUR), 
    550),

(UUID(), 'FL006', 
    (SELECT id FROM plane WHERE plane_code = 'A350'), 
    (SELECT id FROM airport WHERE airport_code = 'SIN'), 
    (SELECT id FROM airport WHERE airport_code = 'JFK'), 
    DATE_ADD(NOW(), INTERVAL 5 DAY), 
    DATE_ADD(DATE_ADD(NOW(), INTERVAL 5 DAY), INTERVAL 12 HOUR), 
    650);
    
-- Flight_Seat table inserts
INSERT INTO flight_seat (id, flight_id, seat_id) VALUES
(UUID(), (SELECT id FROM flight WHERE flight_code = 'FL001'), (SELECT id FROM seat WHERE seat_code = 'ECO-A')),
(UUID(), (SELECT id FROM flight WHERE flight_code = 'FL002'), (SELECT id FROM seat WHERE seat_code = 'ECO-B')),
(UUID(), (SELECT id FROM flight WHERE flight_code = 'FL003'), (SELECT id FROM seat WHERE seat_code = 'BUS-A')),
(UUID(), (SELECT id FROM flight WHERE flight_code = 'FL004'), (SELECT id FROM seat WHERE seat_code = 'BUS-B')),
(UUID(), (SELECT id FROM flight WHERE flight_code = 'FL005'), (SELECT id FROM seat WHERE seat_code = 'FST-A')),
(UUID(), (SELECT id FROM flight WHERE flight_code = 'FL006'), (SELECT id FROM seat WHERE seat_code = 'FST-B'));

-- Intermediate stop (same day, 6h after FL001 departure)
INSERT INTO flight_airport (id, flight_id, airport_id, arrival_time, departure_time) VALUES
(UUID(), 
    (SELECT id FROM flight WHERE flight_code = 'FL001'), 
    (SELECT id FROM airport WHERE airport_code = 'HND'), 
DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 6 HOUR),
    DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 7 HOUR)
);