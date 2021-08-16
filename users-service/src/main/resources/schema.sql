CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(50)  NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    document VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    rating INTEGER
);

CREATE UNIQUE INDEX document_i ON users (document);