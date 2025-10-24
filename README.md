# Luddy Room Reservation System

room booking app for luddy building at IU

## what it does
- search for available rooms
- filter by capacity, floor, and equipment
- shows room features (projector, whiteboard, etc)

## tech stack
- java 17
- spring boot
- sqlite database
- maven

## setup
just open in codespace and run:
cat > src/main/java/com/luddy/roomreservation/model/Room.java << 'EOF'
package com.luddy.roomreservation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("rooms")
public class Room {
    
    @Id
    private Long id;
    private String roomNumber;
    private int floor;
    private int capacity;
    
    // features
    private boolean hasWhiteboard;
    private boolean hasProjector;
    private boolean hasComputer;
    private boolean hasTV;
    
    // accessibility 
    private boolean wheelchairAccessible;
    private boolean hasElevatorAccess;
    
    public Room() {}
    
    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public boolean isHasWhiteboard() { return hasWhiteboard; }
    public void setHasWhiteboard(boolean hasWhiteboard) { this.hasWhiteboard = hasWhiteboard; }
    
    public boolean isHasProjector() { return hasProjector; }
    public void setHasProjector(boolean hasProjector) { this.hasProjector = hasProjector; }
    
    public boolean isHasComputer() { return hasComputer; }
    public void setHasComputer(boolean hasComputer) { this.hasComputer = hasComputer; }
    
    public boolean isHasTV() { return hasTV; }
    public void setHasTV(boolean hasTV) { this.hasTV = hasTV; }
    
    public boolean isWheelchairAccessible() { return wheelchairAccessible; }
    public void setWheelchairAccessible(boolean wheelchairAccessible) { 
        this.wheelchairAccessible = wheelchairAccessible; 
    }
    
    public boolean isHasElevatorAccess() { return hasElevatorAccess; }
    public void setHasElevatorAccess(boolean hasElevatorAccess) { 
        this.hasElevatorAccess = hasElevatorAccess; 
    }
}
