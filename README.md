<!-- # Booking Flight Application

## Permission Management

### Quick Setup - Sample Permissions

#### Generate Sample Permissions
```sql
INSERT INTO permission (id, name, api_path, method, model, created_at, updated_at, is_deleted, deleted_at) 
VALUES 
    (UUID(), 'READ_USER', '/api/users', 'GET', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_USER', '/api/users', 'POST', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_USER', '/api/users/*', 'PUT', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_USER', '/api/users/*', 'DELETE', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_FLIGHT', '/api/flights', 'GET', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_FLIGHT', '/api/flights', 'POST', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_FLIGHT', '/api/flights/*', 'PUT', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_FLIGHT', '/api/flights/*', 'DELETE', 'Flight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_BOOKING', '/api/bookings', 'GET', 'Booking', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_BOOKING', '/api/bookings', 'POST', 'Booking', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_BOOKING', '/api/bookings/*', 'PUT', 'Booking', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_BOOKING', '/api/bookings/*', 'DELETE', 'Booking', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_PERMISSION', '/api/permissions', 'GET', 'Permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_PERMISSION', '/api/permissions', 'POST', 'Permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_PERMISSION', '/api/permissions/*', 'PUT', 'Permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_PERMISSION', '/api/permissions/*', 'DELETE', 'Permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'READ_ROLE', '/api/roles', 'GET', 'Role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'CREATE_ROLE', '/api/roles', 'POST', 'Role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'UPDATE_ROLE', '/api/roles/*', 'PUT', 'Role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL),
    (UUID(), 'DELETE_ROLE', '/api/roles/*', 'DELETE', 'Role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, NULL);
```

#### Get Permission by ID
```sql
SELECT * FROM permission WHERE id = '<ID>';
```
> replace <ID> by permissionID

#### Delete All Sample Permissions
```sql
DELETE FROM permission WHERE name IN (
    'READ_USER', 'CREATE_USER', 'UPDATE_USER', 'DELETE_USER',
    'READ_FLIGHT', 'CREATE_FLIGHT', 'UPDATE_FLIGHT', 'DELETE_FLIGHT',
    'READ_BOOKING', 'CREATE_BOOKING', 'UPDATE_BOOKING', 'DELETE_BOOKING',
    'READ_PERMISSION', 'CREATE_PERMISSION', 'UPDATE_PERMISSION', 'DELETE_PERMISSION',
    'READ_ROLE', 'CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE'
);
```  -->