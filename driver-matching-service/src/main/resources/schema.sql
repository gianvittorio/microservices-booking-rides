CREATE TABLE IF NOT EXISTS drivers (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(50)  NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    document VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    location VARCHAR(50) NOT NULL,
    is_available BOOLEAN NOT NULL,
    rating INTEGER NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS document_i ON drivers (document);