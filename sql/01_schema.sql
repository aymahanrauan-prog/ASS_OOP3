-- HotelReservationSystem schema

DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS guests;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN','STAFF')),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('STANDARD','DELUXE','SUITE')),
    price_per_night INT NOT NULL CHECK (price_per_night > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('FREE','RESERVED','BUSY','CLEANING'))
);

CREATE TABLE guests (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    phone VARCHAR(30),
    email VARCHAR(120)
);

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    room_id INT NOT NULL REFERENCES rooms(id) ON DELETE RESTRICT,
    guest_id INT NOT NULL REFERENCES guests(id) ON DELETE RESTRICT,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_price INT,
    status VARCHAR(20) NOT NULL CHECK (status IN ('BOOKED','CHECKED_IN','COMPLETED','CANCELED'))
);

CREATE INDEX idx_bookings_room ON bookings(room_id);
