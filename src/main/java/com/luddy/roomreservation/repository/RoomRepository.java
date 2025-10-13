package com.luddy.roomreservation.repository;

import com.luddy.roomreservation.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    
    // find by floor
    List<Room> findByFloor(Integer floor);
    
    // find rooms with enough capacity
    List<Room> findByCapacityGreaterThanEqual(Integer capacity);
    
    // find rooms with specific features
    List<Room> findByHasWhiteboard(Boolean hasWhiteboard);
    List<Room> findByHasProjector(Boolean hasProjector);
    
    // accessibility queries
    List<Room> findByElevatorAccess(Boolean elevatorAccess);
    List<Room> findByWheelchairAccessible(Boolean wheelchairAccessible);
    
    // get all rooms
    List<Room> findAll();
}
