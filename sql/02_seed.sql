-- Seed data

INSERT INTO users(username, password_hash, role, active) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN', TRUE),
('staff', '10176e7b7b24d317acfcf8d2064cfd2f24e154f7b5a96603077d5ef813d6a6b6', 'STAFF', TRUE);

INSERT INTO guests(first_name, last_name, phone, email) VALUES
('Ali','Nur','+77010000001','ali@mail.kz'),
('Dana','Khan','+77010000002','dana@mail.kz');

INSERT INTO rooms (room_number, type, price_per_night, status)
SELECT
    (100 + n)::text,
    'STANDARD',
    15000,
    CASE WHEN random() > 0.3 THEN 'FREE' ELSE 'BUSY' END
FROM generate_series(1, 40) AS n;

INSERT INTO rooms (room_number, type, price_per_night, status)
SELECT
    (200 + n)::text,
    'DELUXE',
    25000,
    CASE WHEN random() > 0.3 THEN 'FREE' ELSE 'BUSY' END
FROM generate_series(1, 40) AS n;

INSERT INTO rooms (room_number, type, price_per_night, status)
SELECT
    (300 + n)::text,
    'SUITE',
    40000,
    CASE WHEN random() > 0.3 THEN 'FREE' ELSE 'BUSY' END
FROM generate_series(1, 20) AS n;