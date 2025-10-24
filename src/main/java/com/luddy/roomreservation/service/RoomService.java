package com.luddy.roomreservation.service;

import com.luddy.roomreservation.model.Room;
import com.luddy.roomreservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    public List<Room> getAllRooms() {
        return StreamSupport.stream(roomRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
    
    public List<Room> getRoomsByFloor(int floor) {
        return roomRepository.findByFloor(floor);
    }
    
    // main filter method - takes different search criteria
    // TODO: maybe add pagination later if we get too many rooms
    public List<Room> filterRooms(Integer minCapacity, Integer floor, 
                                   Boolean needsWhiteboard, Boolean needsProjector,
                                   Boolean needsComputer, Boolean needsTV,
                                   Boolean needsWheelchairAccess) {
        
        List<Room> rooms = getAllRooms();
        
        // filter by each criteria if its not null
        if (minCapacity != null) {
            rooms = rooms.stream()
                    .filter(r -> r.getCapacity() >= minCapacity)
                    .collect(Collectors.toList());
        }
        
        if (floor != null) {
            rooms = rooms.stream()
                    .filter(r -> r.getFloor() == floor)
                    .collect(Collectors.toList());
        }
        
        if (needsWhiteboard != null && needsWhiteboard) {
            rooms = rooms.stream()
                    .filter(Room::isHasWhiteboard)
                    .collect(Collectors.toList());
        }
        
        if (needsProjector != null && needsProjector) {
            rooms = rooms.stream()
                    .filter(Room::isHasProjector)
                    .collect(Collectors.toList());
        }
        
        if (needsComputer != null && needsComputer) {
            rooms = rooms.stream()
                    .filter(Room::isHasComputer)
                    .collect(Collectors.toList());
        }
        
        if (needsTV != null && needsTV) {
            rooms = rooms.stream()
                    .filter(Room::isHasTV)
                    .collect(Collectors.toList());
        }
        
        if (needsWheelchairAccess != null && needsWheelchairAccess) {
            rooms = rooms.stream()
                    .filter(Room::isWheelchairAccessible)
                    .collect(Collectors.toList());
        }
        
        return rooms;
    }
    
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }
    
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
