CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    passenger_id INTEGER NOT NULL,
    driver_id INTEGER NOT NULL,
    origin VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_time TIMESTAMP
);