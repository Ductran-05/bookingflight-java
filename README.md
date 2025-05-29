# Booking Flight Application

## Permission Management

### Quick Setup - Sample Permissions

#### Generate Sample Permissions
```sql
INSERT INTO permission (id, name, api_path, method, model, created_at, updated_at, is_deleted, deleted_at) 
VALUES 
    -- Account permissions
    (UUID(), 'READ_ACCOUNT', '/api/accounts', 'GET', 'Account', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_ACCOUNT_BY_ID', '/api/accounts/*', 'GET', 'Account', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_ACCOUNT', '/api/accounts', 'POST', 'Account', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_ACCOUNT', '/api/accounts/*', 'PUT', 'Account', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_ACCOUNT', '/api/accounts/*', 'DELETE', 'Account', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPLOAD_AVATAR', '/api/accounts/*/avatar', 'POST', 'Account', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_AVATAR', '/api/accounts/*/avatar', 'DELETE', 'Account', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- Flight permissions
    (UUID(), 'READ_FLIGHT', '/api/flights', 'GET', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_FLIGHT_BY_ID', '/api/flights/*', 'GET', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_FLIGHT', '/api/flights', 'POST', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_FLIGHT', '/api/flights/*', 'PUT', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_FLIGHT', '/api/flights/*', 'DELETE', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_FLIGHT_SEATS', '/api/flights/seats/*', 'GET', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- Ticket permissions
    (UUID(), 'READ_TICKET', '/api/tickets', 'GET', 'Ticket', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_TICKET_BY_ID', '/api/tickets/*', 'GET', 'Ticket', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_TICKET', '/api/tickets', 'POST', 'Ticket', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_TICKET', '/api/tickets/*', 'PUT', 'Ticket', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_TICKET', '/api/tickets/*', 'DELETE', 'Ticket', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- Seat permissions
    (UUID(), 'READ_SEAT', '/api/seats', 'GET', 'Seat', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_SEAT_BY_ID', '/api/seats/*', 'GET', 'Seat', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_SEAT', '/api/seats', 'POST', 'Seat', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_SEAT', '/api/seats/*', 'PUT', 'Seat', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_SEAT', '/api/seats/*', 'DELETE', 'Seat', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- Airline permissions
    (UUID(), 'READ_AIRLINE', '/api/airlines', 'GET', 'Airline', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_AIRLINE_BY_ID', '/api/airlines/*', 'GET', 'Airline', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_AIRLINE', '/api/airlines', 'POST', 'Airline', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_AIRLINE', '/api/airlines/*', 'PUT', 'Airline', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_AIRLINE', '/api/airlines/*', 'DELETE', 'Airline', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- Airport permissions
    (UUID(), 'READ_AIRPORT', '/api/airports', 'GET', 'Airport', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_AIRPORT_BY_ID', '/api/airports/*', 'GET', 'Airport', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_AIRPORT', '/api/airports', 'POST', 'Airport', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_AIRPORT', '/api/airports/*', 'PUT', 'Airport', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_AIRPORT', '/api/airports/*', 'DELETE', 'Airport', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- City permissions
    (UUID(), 'READ_CITY', '/api/cities', 'GET', 'City', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_CITY_BY_ID', '/api/cities/*', 'GET', 'City', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_CITY', '/api/cities', 'POST', 'City', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_CITY', '/api/cities/*', 'PUT', 'City', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_CITY', '/api/cities/*', 'DELETE', 'City', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- Plane permissions
    (UUID(), 'READ_PLANE', '/api/planes', 'GET', 'Plane', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_PLANE_BY_ID', '/api/planes/*', 'GET', 'Plane', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_PLANE', '/api/planes', 'POST', 'Plane', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_PLANE', '/api/planes/*', 'PUT', 'Plane', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_PLANE', '/api/planes/*', 'DELETE', 'Plane', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- Role permissions
    (UUID(), 'READ_ROLE', '/api/roles', 'GET', 'Role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_ROLE_BY_ID', '/api/roles/*', 'GET', 'Role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_ROLE', '/api/roles', 'POST', 'Role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_ROLE', '/api/roles/*', 'PUT', 'Role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_ROLE', '/api/roles/*', 'DELETE', 'Role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- Permission permissions
    (UUID(), 'READ_PERMISSION', '/api/permissions', 'GET', 'Permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_PERMISSION_BY_ID', '/api/permissions/*', 'GET', 'Permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_PERMISSION', '/api/permissions', 'POST', 'Permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_PERMISSION', '/api/permissions/*', 'PUT', 'Permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_PERMISSION', '/api/permissions/*', 'DELETE', 'Permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- File permissions
    (UUID(), 'DOWNLOAD_FILE', '/api/files/*', 'GET', 'File', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    
    -- Hello endpoint permission (public)
    (UUID(), 'ACCESS_HELLO', '/', 'GET', 'Public', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL);
```

#### Get Permission by ID
```sql
SELECT * FROM permission WHERE id = '<ID>';
```
> replace <ID> by permissionID

#### Delete All Sample Permissions
```sql
DELETE FROM permission WHERE name IN (
    'READ_ACCOUNT', 'READ_ACCOUNT_BY_ID', 'CREATE_ACCOUNT', 'UPDATE_ACCOUNT', 'DELETE_ACCOUNT', 'UPLOAD_AVATAR', 'DELETE_AVATAR',
    'READ_FLIGHT', 'READ_FLIGHT_BY_ID', 'CREATE_FLIGHT', 'UPDATE_FLIGHT', 'DELETE_FLIGHT', 'READ_FLIGHT_SEATS',
    'READ_TICKET', 'READ_TICKET_BY_ID', 'CREATE_TICKET', 'UPDATE_TICKET', 'DELETE_TICKET',
    'READ_SEAT', 'READ_SEAT_BY_ID', 'CREATE_SEAT', 'UPDATE_SEAT', 'DELETE_SEAT',
    'READ_AIRLINE', 'READ_AIRLINE_BY_ID', 'CREATE_AIRLINE', 'UPDATE_AIRLINE', 'DELETE_AIRLINE',
    'READ_AIRPORT', 'READ_AIRPORT_BY_ID', 'CREATE_AIRPORT', 'UPDATE_AIRPORT', 'DELETE_AIRPORT',
    'READ_CITY', 'READ_CITY_BY_ID', 'CREATE_CITY', 'UPDATE_CITY', 'DELETE_CITY',
    'READ_PLANE', 'READ_PLANE_BY_ID', 'CREATE_PLANE', 'UPDATE_PLANE', 'DELETE_PLANE',
    'READ_ROLE', 'READ_ROLE_BY_ID', 'CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE',
    'READ_PERMISSION', 'READ_PERMISSION_BY_ID', 'CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION',
    'DOWNLOAD_FILE', 'ACCESS_HELLO'
);
``` 