-- City table inserts
INSERT INTO City (id, city_code, city_name) VALUES
(UUID(), 'NYC', 'New York City'),
(UUID(), 'LON', 'London'),
(UUID(), 'TYO', 'Tokyo'),
(UUID(), 'PAR', 'Paris'),
(UUID(), 'DXB', 'Dubai'),
(UUID(), 'SIN', 'Singapore');

-- Airline table inserts
INSERT INTO Airline (id, airline_code, airline_name) VALUES
(UUID(), 'UAL', 'United Airlines'),
(UUID(), 'DAL', 'Delta Air Lines'),
(UUID(), 'AAL', 'American Airlines'),
(UUID(), 'BAW', 'British Airways'),
(UUID(), 'SIA', 'Singapore Airlines'),
(UUID(), 'EMI', 'Emirates');

-- Airport table inserts (references City)
INSERT INTO Airport (id, airport_code, airport_name, city_id) VALUES
(UUID(), 'JFK', 'John F. Kennedy International Airport', (SELECT id FROM City WHERE city_code = 'NYC')),
(UUID(), 'LHR', 'London Heathrow Airport', (SELECT id FROM City WHERE city_code = 'LON')),
(UUID(), 'HND', 'Tokyo Haneda Airport', (SELECT id FROM City WHERE city_code = 'TYO')),
(UUID(), 'CDG', 'Charles de Gaulle Airport', (SELECT id FROM City WHERE city_code = 'PAR')),
(UUID(), 'DXB', 'Dubai International Airport', (SELECT id FROM City WHERE city_code = 'DXB')),
(UUID(), 'SIN', 'Singapore Changi Airport', (SELECT id FROM City WHERE city_code = 'SIN'));

-- Plane table inserts (references Airline)
INSERT INTO Plane (id, plane_code, plane_name, airline_id) VALUES
(UUID(), 'B737', 'Boeing 737-800', (SELECT id FROM Airline WHERE airline_code = 'UAL')),
(UUID(), 'A320', 'Airbus A320', (SELECT id FROM Airline WHERE airline_code = 'DAL')),
(UUID(), 'B777', 'Boeing 777-300ER', (SELECT id FROM Airline WHERE airline_code = 'AAL')),
(UUID(), 'A380', 'Airbus A380', (SELECT id FROM Airline WHERE airline_code = 'BAW')),
(UUID(), 'B787', 'Boeing 787 Dreamliner', (SELECT id FROM Airline WHERE airline_code = 'SIA')),
(UUID(), 'A350', 'Airbus A350', (SELECT id FROM Airline WHERE airline_code = 'EMI'));

-- Seat table inserts
INSERT INTO Seat (id, seat_code, seat_name, price, description) VALUES
(UUID(), 'ECO-A', 'Economy Class A', 100, 'Standard Economy Seat'),
(UUID(), 'ECO-B', 'Economy Class B', 120, 'Economy Seat with Extra Legroom'),
(UUID(), 'BUS-A', 'Business Class A', 500, 'Standard Business Class Seat'),
(UUID(), 'BUS-B', 'Business Class B', 600, 'Business Class Window Seat'),
(UUID(), 'FST-A', 'First Class A', 1000, 'First Class Suite'),
(UUID(), 'FST-B', 'First Class B', 1200, 'First Class Suite with Shower');

-- Role table inserts
INSERT INTO Role (id, role_name) VALUES
(UUID(), 'ADMIN'),
(UUID(), 'USER'),
(UUID(), 'MANAGER'),
(UUID(), 'AGENT'),
(UUID(), 'SUPPORT'),
(UUID(), 'GUEST');

-- Permission table inserts
INSERT INTO Permission (id, name, api_path, method, model) VALUES
(UUID(), 'View Flights', '/api/flights', 'GET', 'Flight'),
(UUID(), 'Book Ticket', '/api/tickets', 'POST', 'Ticket'),
(UUID(), 'Manage Users', '/api/users', 'ALL', 'User'),
(UUID(), 'View Reports', '/api/reports', 'GET', 'Report'),
(UUID(), 'Manage Flights', '/api/flights', 'ALL', 'Flight'),
(UUID(), 'Manage Bookings', '/api/bookings', 'ALL', 'Booking');

-- Permission_Role table inserts
INSERT INTO Permission_Role (id, permission_id, role_id) VALUES
(UUID(), (SELECT id FROM Permission WHERE name = 'View Flights'), (SELECT id FROM Role WHERE role_name = 'ADMIN')),
(UUID(), (SELECT id FROM Permission WHERE name = 'Book Ticket'), (SELECT id FROM Role WHERE role_name = 'USER')),
(UUID(), (SELECT id FROM Permission WHERE name = 'Manage Users'), (SELECT id FROM Role WHERE role_name = 'ADMIN')),
(UUID(), (SELECT id FROM Permission WHERE name = 'View Reports'), (SELECT id FROM Role WHERE role_name = 'MANAGER')),
(UUID(), (SELECT id FROM Permission WHERE name = 'Manage Flights'), (SELECT id FROM Role WHERE role_name = 'AGENT')),
(UUID(), (SELECT id FROM Permission WHERE name = 'Manage Bookings'), (SELECT id FROM Role WHERE role_name = 'SUPPORT'));

-- Flight table inserts
INSERT INTO Flight (id, flight_code, plane_id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, origin_price) VALUES
(UUID(), 'FL001', (SELECT id FROM Plane WHERE plane_code = 'B737'), (SELECT id FROM Airport WHERE airport_code = 'JFK'), (SELECT id FROM Airport WHERE airport_code = 'LHR'), '2024-03-20 10:00:00', '2024-03-20 22:00:00', 500),
(UUID(), 'FL002', (SELECT id FROM Plane WHERE plane_code = 'A320'), (SELECT id FROM Airport WHERE airport_code = 'LHR'), (SELECT id FROM Airport WHERE airport_code = 'HND'), '2024-03-21 11:00:00', '2024-03-22 01:00:00', 600),
(UUID(), 'FL003', (SELECT id FROM Plane WHERE plane_code = 'B777'), (SELECT id FROM Airport WHERE airport_code = 'HND'), (SELECT id FROM Airport WHERE airport_code = 'CDG'), '2024-03-22 09:00:00', '2024-03-22 15:00:00', 450),
(UUID(), 'FL004', (SELECT id FROM Plane WHERE plane_code = 'A380'), (SELECT id FROM Airport WHERE airport_code = 'CDG'), (SELECT id FROM Airport WHERE airport_code = 'DXB'), '2024-03-23 14:00:00', '2024-03-24 02:00:00', 700),
(UUID(), 'FL005', (SELECT id FROM Plane WHERE plane_code = 'B787'), (SELECT id FROM Airport WHERE airport_code = 'DXB'), (SELECT id FROM Airport WHERE airport_code = 'SIN'), '2024-03-24 16:00:00', '2024-03-25 04:00:00', 550),
(UUID(), 'FL006', (SELECT id FROM Plane WHERE plane_code = 'A350'), (SELECT id FROM Airport WHERE airport_code = 'SIN'), (SELECT id FROM Airport WHERE airport_code = 'JFK'), '2024-03-25 08:00:00', '2024-03-25 20:00:00', 650);

-- Flight_Seat table inserts
INSERT INTO Flight_Seat (id, flight_id, seat_id, available) VALUES
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL001'), (SELECT id FROM Seat WHERE seat_code = 'ECO-A'), true),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL002'), (SELECT id FROM Seat WHERE seat_code = 'ECO-B'), true),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL003'), (SELECT id FROM Seat WHERE seat_code = 'BUS-A'), true),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL004'), (SELECT id FROM Seat WHERE seat_code = 'BUS-B'), true),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL005'), (SELECT id FROM Seat WHERE seat_code = 'FST-A'), true),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL006'), (SELECT id FROM Seat WHERE seat_code = 'FST-B'), true);

-- Flight_Airport table inserts (for intermediate stops)
INSERT INTO Flight_Airport (id, flight_id, airport_id, arrival_time, departure_time) VALUES
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL001'), (SELECT id FROM Airport WHERE airport_code = 'HND'), '2024-03-20 16:00:00', '2024-03-20 17:00:00'),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL002'), (SELECT id FROM Airport WHERE airport_code = 'CDG'), '2024-03-21 18:00:00', '2024-03-21 19:00:00'),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL003'), (SELECT id FROM Airport WHERE airport_code = 'DXB'), '2024-03-22 12:00:00', '2024-03-22 13:00:00'),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL004'), (SELECT id FROM Airport WHERE airport_code = 'SIN'), '2024-03-23 20:00:00', '2024-03-23 21:00:00'),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL005'), (SELECT id FROM Airport WHERE airport_code = 'JFK'), '2024-03-24 22:00:00', '2024-03-24 23:00:00'),
(UUID(), (SELECT id FROM Flight WHERE flight_code = 'FL006'), (SELECT id FROM Airport WHERE airport_code = 'LHR'), '2024-03-25 14:00:00', '2024-03-25 15:00:00');