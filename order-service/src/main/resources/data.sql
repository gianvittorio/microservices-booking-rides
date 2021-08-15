DELETE FROM ORDERS;
INSERT INTO orders (passenger_id, driver_id, origin, destination, departure_time)
VALUES (123, 321, 'X', 'Y', CURRENT_TIMESTAMP);