package com.luddy.roomreservation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class Room {
    
    @Id
    private Long id;
    
    private String roomNumber;
    private Integer floor;
    private Integer capacity;
    
    // features
    private Boolean hasWhiteboard;
    private Boolean hasProjector;
    private Boolean hasComputer;
    private Boolean hasTv;
    
    // accessibility stuff
    private Boolean elevatorAccess;
    private Boolean wheelchairAccessible;
    
    private String building;

    // default constructor
    public Room() {
        this.building = "Luddy";
    }

    public Room(String roomNumber, Integer floor, Integer capacity) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.capacity = capacity;
        this.building = "Luddy";
        // set defaults for features
        this.hasWhiteboard = false;
        this.hasProjector = false;
        this.hasComputer = false;
        this.hasTv = false;
        this.elevatorAccess = false;
        this.wheelchairAccessible = false;
    }

    // getters and setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getRoomNumber() { 
        return roomNumber; 
    }
    
    public void setRoomNumber(String roomNumber) { 
        this.roomNumber = roomNumber; 
    }

    public Integer getFloor() { 
        return floor; 
    }
    
    public void setFloor(Integer floor) { 
        this.floor = floor; 
    }

    public Integer getCapacity() { 
        return capacity; 
    }
    
    public void setCapacity(Integer capacity) { 
        this.capacity = capacity; 
    }

    public Boolean getHasWhiteboard() { 
        return hasWhiteboard; 
    }
    
    public void setHasWhiteboard(Boolean hasWhiteboard) { 
        this.hasWhiteboard = hasWhiteboard; 
    }

    public Boolean getHasProjector() { 
        return hasProjector; 
    }
    
    public void setHasProjector(Boolean hasProjector) { 
        this.hasProjector = hasProjector; 
    }

    public Boolean getHasComputer() { 
        return hasComputer; 
    }
    
    public void setHasComputer(Boolean hasComputer) { 
        this.hasComputer = hasComputer; 
    }

    public Boolean getHasTv() { 
        return hasTv; 
    }
    
    public void setHasTv(Boolean hasTv) { 
        this.hasTv = hasTv; 
    }

    public Boolean getElevatorAccess() { 
        return elevatorAccess; 
    }
    
    public void setElevatorAccess(Boolean elevatorAccess) { 
        this.elevatorAccess = elevatorAccess; 
    }

    public Boolean getWheelchairAccessible() { 
        return wheelchairAccessible; 
    }
    
    public void setWheelchairAccessible(Boolean wheelchairAccessible) { 
        this.wheelchairAccessible = wheelchairAccessible; 
    }

    public String getBuilding() { 
        return building; 
    }
    
    public void setBuilding(String building) { 
        this.building = building; 
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", floor=" + floor +
                ", capacity=" + capacity +
                '}';
    }
}
