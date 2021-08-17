CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    passenger_id INTEGER NOT NULL,
    driver_id INTEGER NOT NULL,
    origin VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE INDEX IF NOT EXISTS passenger_id_i ON orders (passenger_id);
CREATE INDEX IF NOT EXISTS driver_id_i ON orders (driver_id);
CREATE INDEX IF NOT EXISTS departure_time_i ON orders (departure_time);