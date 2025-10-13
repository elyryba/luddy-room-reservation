package com.luddy.roomreservation.service;

import com.luddy.roomreservation.model.Room;
import com.luddy.roomreservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {
    
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Get all rooms in the system
     */
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Find a specific room by ID
     */
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    /**
     * Get all rooms on a specific floor
     */
    public List<Room> getRoomsByFloor(Integer floor) {
        return roomRepository.findByFloor(floor);
    }

    /**
     * Find rooms with at least the specified capacity
     */
    public List<Room> getRoomsByMinCapacity(Integer minCapacity) {
        return roomRepository.findByCapacityGreaterThanEqual(minCapacity);
    }

    /**
     * This is the main filtering method
     * Takes various criteria and returns matching rooms
     */
    public List<Room> filterRooms(Integer minCapacity, 
                                   Boolean needsWhiteboard, 
                                   Boolean needsProjector,
                                   Boolean needsComputer,
                                   Boolean needsElevator, 
                                   Integer floor) {
        
        List<Room> rooms = getAllRooms();
        
        // filter by capacity if specified
        if(minCapacity != null) {
            rooms = rooms.stream()
                    .filter(room -> room.getCapacity() >= minCapacity)
                    .collect(Collectors.toList());
        }
        
        // check for whiteboard
        if(needsWhiteboard != null && needsWhiteboard) {
            rooms = rooms.stream()
                    .filter(room -> Boolean.TRUE.equals(room.getHasWhiteboard()))
                    .collect(Collectors.toList());
        }
        
        // check for projector
        if(needsProjector != null && needsProjector) {
            rooms = rooms.stream()
                    .filter(room -> Boolean.TRUE.equals(room.getHasProjector()))
                    .collect(Collectors.toList());
        }

        // check for computer
        if(needsComputer != null && needsComputer) {
            rooms = rooms.stream()
                    .filter(room -> Boolean.TRUE.equals(room.getHasComputer()))
                    .collect(Collectors.toList());
        }
        
        // elevator access check
        if(needsElevator != null && needsElevator) {
            rooms = rooms.stream()
                    .filter(room -> Boolean.TRUE.equals(room.getElevatorAccess()))
                    .collect(Collectors.toList());
        }
        
        // specific floor requested
        if(floor != null) {
            rooms = rooms.stream()
                    .filter(room -> room.getFloor().equals(floor))
                    .collect(Collectors.toList());
        }
        
        return rooms;
    }

    // Save a room
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    // Delete a room
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
    
    // Get count of rooms
    public long getTotalRoomCount() {
        return roomRepository.count();
    }
}
