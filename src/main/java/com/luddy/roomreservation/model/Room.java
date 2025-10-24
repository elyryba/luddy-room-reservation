
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

    private Integer hasWhiteboard;

    private Integer hasProjector;

    private Integer hasComputer;

    private Integer hasTV;

    

    // accessibility 

    private Integer wheelchairAccessible;

    private Integer hasElevatorAccess;

    

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

    

    public Integer isHasWhiteboard() { return hasWhiteboard; }

    public void setHasWhiteboard(Integer hasWhiteboard) { this.hasWhiteboard = hasWhiteboard; }

    

    public Integer isHasProjector() { return hasProjector; }

    public void setHasProjector(Integer hasProjector) { this.hasProjector = hasProjector; }

    

    public Integer isHasComputer() { return hasComputer; }

    public void setHasComputer(Integer hasComputer) { this.hasComputer = hasComputer; }

    

    public Integer isHasTV() { return hasTV; }

    public void setHasTV(Integer hasTV) { this.hasTV = hasTV; }

    

    public Integer isWheelchairAccessible() { return wheelchairAccessible; }

    public void setWheelchairAccessible(Integer wheelchairAccessible) { 

        this.wheelchairAccessible = wheelchairAccessible; 

    }

    

    public Integer isHasElevatorAccess() { return hasElevatorAccess; }

    public void setHasElevatorAccess(Integer hasElevatorAccess) { 

        this.hasElevatorAccess = hasElevatorAccess; 

    }

}

