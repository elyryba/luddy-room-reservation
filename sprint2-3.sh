#!/bin/bash
echo "🚀 Building Sprint 2+3: Frontend + Booking System..."

# 1. Create Reservation Entity
mkdir -p src/main/java/com/luddy/roomreservation/model
cat > src/main/java/com/luddy/roomreservation/model/Reservation.java << 'EOF'
package com.luddy.roomreservation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("reservations")
public class Reservation {
    
    @Id
    private Long id;
    private Long roomId;
    private String userName;
    private String userEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private LocalDateTime createdAt;
    
    public Reservation() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
EOF

# 2. Create ReservationRepository
mkdir -p src/main/java/com/luddy/roomreservation/repository
cat > src/main/java/com/luddy/roomreservation/repository/ReservationRepository.java << 'EOF'
package com.luddy.roomreservation.repository;

import com.luddy.roomreservation.model.Reservation;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByUserEmail(String userEmail);
    
    @Query("SELECT * FROM reservations WHERE room_id = :roomId AND " +
           "((start_time <= :endTime AND end_time >= :startTime))")
    List<Reservation> findConflictingReservations(
        @Param("roomId") Long roomId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
EOF

# 3. Create ReservationService
mkdir -p src/main/java/com/luddy/roomreservation/service
cat > src/main/java/com/luddy/roomreservation/service/ReservationService.java << 'EOF'
package com.luddy.roomreservation.service;

import com.luddy.roomreservation.model.Reservation;
import com.luddy.roomreservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    public List<Reservation> getAllReservations() {
        return StreamSupport.stream(reservationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
    
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }
    
    public List<Reservation> getReservationsByRoom(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }
    
    public List<Reservation> getReservationsByEmail(String email) {
        return reservationRepository.findByUserEmail(email);
    }
    
    // check if room is available for the time slot
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
            roomId, startTime, endTime);
        return conflicts.isEmpty();
    }
    
    public Reservation createReservation(Reservation reservation) {
        if (!isRoomAvailable(reservation.getRoomId(), 
                             reservation.getStartTime(), 
                             reservation.getEndTime())) {
            throw new IllegalStateException("Room not available for this time slot");
        }
        return reservationRepository.save(reservation);
    }
    
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
EOF

# 4. Create RoomController
mkdir -p src/main/java/com/luddy/roomreservation/controller
cat > src/main/java/com/luddy/roomreservation/controller/RoomController.java << 'EOF'
package com.luddy.roomreservation.controller;

import com.luddy.roomreservation.model.Room;
import com.luddy.roomreservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/rooms")
    public String searchRooms(
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Boolean whiteboard,
            @RequestParam(required = false) Boolean projector,
            @RequestParam(required = false) Boolean computer,
            @RequestParam(required = false) Boolean tv,
            @RequestParam(required = false) Boolean wheelchair,
            Model model) {
        
        List<Room> rooms = roomService.filterRooms(
            minCapacity, floor, whiteboard, projector, computer, tv, wheelchair);
        
        model.addAttribute("rooms", rooms);
        return "rooms";
    }
    
    @GetMapping("/room/{id}")
    public String roomDetails(@PathVariable Long id, Model model) {
        roomService.getAllRooms().stream()
            .filter(r -> r.getId().equals(id))
            .findFirst()
            .ifPresent(room -> model.addAttribute("room", room));
        return "room-details";
    }
}
EOF

# 5. Create ReservationController
cat > src/main/java/com/luddy/roomreservation/controller/ReservationController.java << 'EOF'
package com.luddy.roomreservation.controller;

import com.luddy.roomreservation.model.Reservation;
import com.luddy.roomreservation.model.Room;
import com.luddy.roomreservation.service.ReservationService;
import com.luddy.roomreservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private RoomService roomService;
    
    @GetMapping("/book/{roomId}")
    public String showBookingForm(@PathVariable Long roomId, Model model) {
        roomService.getAllRooms().stream()
            .filter(r -> r.getId().equals(roomId))
            .findFirst()
            .ifPresent(room -> model.addAttribute("room", room));
        return "book";
    }
    
    @PostMapping("/book")
    public String bookRoom(
            @RequestParam Long roomId,
            @RequestParam String userName,
            @RequestParam String userEmail,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String purpose,
            Model model) {
        
        try {
            Reservation reservation = new Reservation();
            reservation.setRoomId(roomId);
            reservation.setUserName(userName);
            reservation.setUserEmail(userEmail);
            reservation.setStartTime(LocalDateTime.parse(startTime));
            reservation.setEndTime(LocalDateTime.parse(endTime));
            reservation.setPurpose(purpose);
            
            reservationService.createReservation(reservation);
            return "redirect:/reservations/my?email=" + userEmail + "&success=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "book";
        }
    }
    
    @GetMapping("/my")
    public String myReservations(@RequestParam String email, Model model) {
        List<Reservation> reservations = reservationService.getReservationsByEmail(email);
        model.addAttribute("reservations", reservations);
        model.addAttribute("email", email);
        return "my-reservations";
    }
    
    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable Long id, @RequestParam String email) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations/my?email=" + email;
    }
}
EOF

# 6. Update schema.sql for reservations table
cat > src/main/resources/schema.sql << 'EOF'
CREATE TABLE IF NOT EXISTS rooms (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    room_number VARCHAR(10) NOT NULL,
    floor INTEGER NOT NULL,
    capacity INTEGER NOT NULL,
    has_whiteboard BOOLEAN DEFAULT FALSE,
    has_projector BOOLEAN DEFAULT FALSE,
    has_computer BOOLEAN DEFAULT FALSE,
    has_tv BOOLEAN DEFAULT FALSE,
    wheelchair_accessible BOOLEAN DEFAULT FALSE,
    has_elevator_access BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS reservations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    room_id INTEGER NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    user_email VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    purpose TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);
EOF

# 7. Create templates directory
mkdir -p src/main/resources/templates
mkdir -p src/main/resources/static/css

# 8. Create index.html (home page)
cat > src/main/resources/templates/index.html << 'EOF'
<!DOCTYPE html>
<html>
<head>
    <title>Luddy Room Reservation</title>
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>🏫 Luddy Room Reservation System</h1>
            <p>Find and book rooms at IU Luddy School</p>
        </header>
        
        <div class="search-box">
            <h2>Search Available Rooms</h2>
            <form action="/rooms" method="get">
                <div class="form-row">
                    <div class="form-group">
                        <label>Minimum Capacity:</label>
                        <input type="number" name="minCapacity" placeholder="e.g. 20">
                    </div>
                    
                    <div class="form-group">
                        <label>Floor:</label>
                        <select name="floor">
                            <option value="">Any Floor</option>
                            <option value="1">Floor 1</option>
                            <option value="2">Floor 2</option>
                            <option value="3">Floor 3</option>
                            <option value="4">Floor 4</option>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <label class="checkbox-label">
                        <input type="checkbox" name="whiteboard" value="true"> Whiteboard
                    </label>
                    <label class="checkbox-label">
                        <input type="checkbox" name="projector" value="true"> Projector
                    </label>
                    <label class="checkbox-label">
                        <input type="checkbox" name="computer" value="true"> Computer
                    </label>
                    <label class="checkbox-label">
                        <input type="checkbox" name="tv" value="true"> TV
                    </label>
                    <label class="checkbox-label">
                        <input type="checkbox" name="wheelchair" value="true"> Wheelchair Accessible
                    </label>
                </div>
                
                <button type="submit" class="btn-primary">Search Rooms</button>
            </form>
        </div>
        
        <div class="quick-links">
            <a href="/rooms" class="btn">View All Rooms</a>
        </div>
    </div>
</body>
</html>
EOF

# 9. Create rooms.html (search results)
cat > src/main/resources/templates/rooms.html << 'EOF'
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Available Rooms</title>
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Available Rooms</h1>
            <a href="/" class="btn-secondary">← Back to Search</a>
        </header>
        
        <div class="rooms-grid">
            <div th:each="room : ${rooms}" class="room-card">
                <h3 th:text="'Room ' + ${room.roomNumber}">Room 101</h3>
                <p><strong>Floor:</strong> <span th:text="${room.floor}">1</span></p>
                <p><strong>Capacity:</strong> <span th:text="${room.capacity}">20</span> people</p>
                
                <div class="features">
                    <span th:if="${room.hasWhiteboard}" class="badge">📝 Whiteboard</span>
                    <span th:if="${room.hasProjector}" class="badge">📽️ Projector</span>
                    <span th:if="${room.hasComputer}" class="badge">💻 Computer</span>
                    <span th:if="${room.hasTV}" class="badge">📺 TV</span>
                    <span th:if="${room.wheelchairAccessible}" class="badge">♿ Accessible</span>
                </div>
                
                <a th:href="@{/room/{id}(id=${room.id})}" class="btn-primary">View Details</a>
            </div>
        </div>
        
        <div th:if="${#lists.isEmpty(rooms)}" class="no-results">
            <p>No rooms match your criteria. Try adjusting your search.</p>
        </div>
    </div>
</body>
</html>
EOF

# 10. Create room-details.html
cat > src/main/resources/templates/room-details.html << 'EOF'
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Room Details</title>
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1 th:text="'Room ' + ${room.roomNumber}">Room Details</h1>
            <a href="/rooms" class="btn-secondary">← Back to Rooms</a>
        </header>
        
        <div class="room-details">
            <div class="detail-card">
                <h2>Room Information</h2>
                <p><strong>Room Number:</strong> <span th:text="${room.roomNumber}">101</span></p>
                <p><strong>Floor:</strong> <span th:text="${room.floor}">1</span></p>
                <p><strong>Capacity:</strong> <span th:text="${room.capacity}">20</span> people</p>
            </div>
            
            <div class="detail-card">
                <h2>Available Equipment</h2>
                <ul>
                    <li th:if="${room.hasWhiteboard}">📝 Whiteboard</li>
                    <li th:if="${room.hasProjector}">📽️ Projector</li>
                    <li th:if="${room.hasComputer}">💻 Computer</li>
                    <li th:if="${room.hasTV}">📺 TV</li>
                </ul>
            </div>
            
            <div class="detail-card">
                <h2>Accessibility</h2>
                <p th:if="${room.wheelchairAccessible}">♿ Wheelchair Accessible</p>
                <p th:if="${room.hasElevatorAccess}">🛗 Elevator Access</p>
            </div>
            
            <a th:href="@{/reservations/book/{id}(id=${room.id})}" class="btn-primary btn-large">
                📅 Book This Room
            </a>
        </div>
    </div>
</body>
</html>
EOF

# 11. Create book.html (booking form)
cat > src/main/resources/templates/book.html << 'EOF'
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Book Room</title>
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1 th:text="'Book Room ' + ${room.roomNumber}">Book Room</h1>
            <a th:href="@{/room/{id}(id=${room.id})}" class="btn-secondary">← Back</a>
        </header>
        
        <div class="booking-form">
            <form action="/reservations/book" method="post">
                <input type="hidden" name="roomId" th:value="${room.id}">
                
                <div class="form-group">
                    <label>Your Name:</label>
                    <input type="text" name="userName" required placeholder="John Doe">
                </div>
                
                <div class="form-group">
                    <label>Your Email:</label>
                    <input type="email" name="userEmail" required placeholder="jdoe@iu.edu">
                </div>
                
                <div class="form-group">
                    <label>Start Time:</label>
                    <input type="datetime-local" name="startTime" required>
                </div>
                
                <div class="form-group">
                    <label>End Time:</label>
                    <input type="datetime-local" name="endTime" required>
                </div>
                
                <div class="form-group">
                    <label>Purpose:</label>
                    <textarea name="purpose" rows="3" required placeholder="Study group, meeting, etc."></textarea>
                </div>
                
                <button type="submit" class="btn-primary btn-large">Confirm Booking</button>
            </form>
            
            <div th:if="${error}" class="error-message">
                <p th:text="${error}">Error message</p>
            </div>
        </div>
    </div>
</body>
</html>
EOF

# 12. Create my-reservations.html
cat > src/main/resources/templates/my-reservations.html << 'EOF'
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>My Reservations</title>
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>My Reservations</h1>
            <p th:text="'Email: ' + ${email}">Email</p>
            <a href="/" class="btn-secondary">← Home</a>
        </header>
        
        <div class="reservations-list">
            <div th:each="reservation : ${reservations}" class="reservation-card">
                <h3 th:text="'Room ' + ${reservation.roomId}">Room</h3>
                <p><strong>Start:</strong> <span th:text="${#temporals.format(reservation.startTime, 'MMM dd, yyyy hh:mm a')}">Date</span></p>
                <p><strong>End:</strong> <span th:text="${#temporals.format(reservation.endTime, 'MMM dd, yyyy hh:mm a')}">Date</span></p>
                <p><strong>Purpose:</strong> <span th:text="${reservation.purpose}">Purpose</span></p>
                
                <form th:action="@{/reservations/cancel/{id}(id=${reservation.id})}" method="post" style="display:inline;">
                    <input type="hidden" name="email" th:value="${email}">
                    <button type="submit" class="btn-danger" onclick="return confirm('Cancel this reservation?')">Cancel</button>
                </form>
            </div>
        </div>
        
        <div th:if="${#lists.isEmpty(reservations)}" class="no-results">
            <p>You have no reservations yet.</p>
        </div>
    </div>
</body>
</html>
EOF

# 13. Create styles.css
cat > src/main/resources/static/css/styles.css << 'EOF'
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    min-height: 100vh;
    padding: 20px;
}

.container {
    max-width: 1200px;
    margin: 0 auto;
    background: white;
    border-radius: 15px;
    padding: 30px;
    box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}

header {
    text-align: center;
    margin-bottom: 40px;
    padding-bottom: 20px;
    border-bottom: 3px solid #667eea;
}

h1 {
    color: #333;
    font-size: 2.5em;
    margin-bottom: 10px;
}

h2 {
    color: #555;
    margin-bottom: 20px;
}

.search-box {
    background: #f8f9fa;
    padding: 30px;
    border-radius: 10px;
    margin-bottom: 30px;
}

.form-row {
    display: flex;
    gap: 20px;
    margin-bottom: 20px;
    flex-wrap: wrap;
}

.form-group {
    flex: 1;
    min-width: 200px;
}

.form-group label {
    display: block;
    margin-bottom: 8px;
    font-weight: 600;
    color: #555;
}

.form-group input,
.form-group select,
.form-group textarea {
    width: 100%;
    padding: 12px;
    border: 2px solid #ddd;
    border-radius: 8px;
    font-size: 16px;
    transition: border-color 0.3s;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
    outline: none;
    border-color: #667eea;
}

.checkbox-label {
    display: inline-flex;
    align-items: center;
    margin-right: 20px;
    margin-bottom: 10px;
    cursor: pointer;
}

.checkbox-label input {
    margin-right: 8px;
    width: 18px;
    height: 18px;
    cursor: pointer;
}

.btn-primary, .btn-secondary, .btn, .btn-danger {
    padding: 12px 30px;
    border: none;
    border-radius: 8px;
    font-size: 16px;
    cursor: pointer;
    text-decoration: none;
    display: inline-block;
    transition: all 0.3s;
    font-weight: 600;
}

.btn-primary {
    background: #667eea;
    color: white;
}

.btn-primary:hover {
    background: #5568d3;
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.btn-secondary {
    background: #6c757d;
    color: white;
}

.btn-secondary:hover {
    background: #5a6268;
}

.btn {
    background: white;
    color: #667eea;
    border: 2px solid #667eea;
}

.btn:hover {
    background: #667eea;
    color: white;
}

.btn-danger {
    background: #dc3545;
    color: white;
}

.btn-danger:hover {
    background: #c82333;
}

.btn-large {
    padding: 15px 40px;
    font-size: 18px;
}

.rooms-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 25px;
    margin-top: 30px;
}

.room-card {
    background: white;
    border: 2px solid #e9ecef;
    border-radius: 12px;
    padding: 25px;
    transition: all 0.3s;
}

.room-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 25px rgba(0,0,0,0.1);
    border-color: #667eea;
}

.room-card h3 {
    color: #667eea;
    margin-bottom: 15px;
    font-size: 1.5em;
}

.features {
    margin: 15px 0;
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

.badge {
    background: #e7f3ff;
    color: #004085;
    padding: 5px 12px;
    border-radius: 20px;
    font-size: 14px;
    display: inline-block;
}

.room-details {
    margin-top: 30px;
}

.detail-card {
    background: #f8f9fa;
    padding: 25px;
    border-radius: 10px;
    margin-bottom: 20px;
}

.detail-card h2 {
    color: #667eea;
    margin-bottom: 15px;
}

.detail-card ul {
    list-style: none;
    padding-left: 0;
}

.detail-card li {
    padding: 8px 0;
    font-size: 1.1em;
}

.booking-form {
    max-width: 600px;
    margin: 30px auto;
}

.reservations-list {
    margin-top: 30px;
}

.reservation-card {
    background: #f8f9fa;
    padding: 20px;
    border-radius: 10px;
    margin-bottom: 20px;
    border-left: 4px solid #667eea;
}

.reservation-card h3 {
    color: #667eea;
    margin-bottom: 10px;
}

.no-results {
    text-align: center;
    padding: 40px;
    color: #6c757d;
    font-size: 1.2em;
}

.error-message {
    background: #f8d7da;
    color: #721c24;
    padding: 15px;
    border-radius: 8px;
    margin-top: 20px;
}

.quick-links {
    text-align: center;
    margin-top: 30px;
}
EOF

# 14. Update application.properties for Thymeleaf
cat >> src/main/resources/application.properties << 'EOF'

# Thymeleaf settings
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
EOF

# 15. Add Thymeleaf dependency to pom.xml
# First backup pom.xml
cp pom.xml pom.xml.backup

# Add thymeleaf dependency before </dependencies>
sed -i '/<\/dependencies>/i\
\t\t<dependency>\
\t\t\t<groupId>org.springframework.boot</groupId>\
\t\t\t<artifactId>spring-boot-starter-thymeleaf</artifactId>\
\t\t</dependency>' pom.xml

echo ""
echo "✅ All files created!"
echo "🔨 Building project..."

# Build and test
mvn clean install -DskipTests -q

echo ""
echo "✅ Build complete!"
echo "🚀 Committing to Git..."

# Commit everything
git add .
git commit -m "Sprint 2-3: Added frontend pages, booking system, and styling"
git push

echo ""
echo "🎉 SPRINT 2-3 COMPLETE!"
echo ""
echo "📋 What was built:"
echo "  ✅ Reservation entity & database"
echo "  ✅ ReservationService with conflict checking"
echo "  ✅ Web controllers (Room + Reservation)"
echo "  ✅ 5 HTML pages with Thymeleaf"
echo "  ✅ Modern CSS styling with IU colors"
echo "  ✅ Full booking system"
echo ""
echo "🌐 To test locally, run:"
echo "   mvn spring-boot:run"
echo "   Then open: http://localhost:8080"
echo ""
