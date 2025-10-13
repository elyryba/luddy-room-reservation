-- Clean up existing tables
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS room;

-- Room table
CREATE TABLE room (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    room_number VARCHAR(10) NOT NULL,
    floor INTEGER NOT NULL,
    capacity INTEGER NOT NULL,
    has_whiteboard BOOLEAN DEFAULT 0,
    has_projector BOOLEAN DEFAULT 0,
    has_computer BOOLEAN DEFAULT 0,
    has_tv BOOLEAN DEFAULT 0,
    elevator_access BOOLEAN DEFAULT 0,
    wheelchair_accessible BOOLEAN DEFAULT 0,
    building VARCHAR(50) DEFAULT 'Luddy'
);

-- Reservation table for future use
CREATE TABLE reservation (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    room_id INTEGER NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    purpose TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES room(id)
);

-- Sample data - Floor 1
INSERT INTO room (room_number, floor, capacity, has_whiteboard, has_projector, has_computer, has_tv, elevator_access, wheelchair_accessible) 
VALUES 
('101', 1, 4, 1, 0, 0, 0, 1, 1),
('102', 1, 8, 1, 1, 0, 0, 1, 1),
('103', 1, 20, 1, 1, 1, 0, 1, 1),
('104', 1, 6, 1, 0, 0, 1, 1, 1),
('105', 1, 12, 1, 1, 0, 0, 1, 1);

-- Floor 2 
INSERT INTO room (room_number, floor, capacity, has_whiteboard, has_projector, has_computer, has_tv, elevator_access, wheelchair_accessible)
VALUES
('201', 2, 40, 1, 1, 1, 1, 1, 0),
('202', 2, 20, 1, 1, 0, 0, 1, 1),
('203', 2, 10, 1, 0, 1, 0, 1, 1),
('204', 2, 50, 1, 1, 1, 1, 1, 0),
('205', 2, 15, 1, 1, 0, 1, 1, 1);

-- Floor 3
INSERT INTO room (room_number, floor, capacity, has_whiteboard, has_projector, has_computer, has_tv, elevator_access, wheelchair_accessible)
VALUES
('301', 3, 6, 0, 1, 1, 0, 0, 0),
('302', 3, 50, 1, 1, 1, 1, 0, 0),
('303', 3, 15, 1, 1, 0, 1, 0, 0),
('304', 3, 8, 1, 0, 0, 0, 0, 0),
('305', 3, 25, 1, 1, 1, 0, 0, 0);

-- Floor 4
INSERT INTO room (room_number, floor, capacity, has_whiteboard, has_projector, has_computer, has_tv, elevator_access, wheelchair_accessible)
VALUES
('401', 4, 30, 1, 1, 1, 0, 1, 1),
('402', 4, 12, 1, 0, 1, 0, 1, 1),
('403', 4, 25, 1, 1, 0, 1, 1, 1),
('404', 4, 18, 1, 1, 1, 0, 1, 1);
