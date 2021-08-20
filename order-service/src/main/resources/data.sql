DELETE FROM ORDERS;
INSERT INTO orders (passenger_id, driver_id, origin, destination, departure, status)
VALUES (123, 321, 'X', 'Y', '1999-01-08 04:05:06'::timestamp, 'pending');