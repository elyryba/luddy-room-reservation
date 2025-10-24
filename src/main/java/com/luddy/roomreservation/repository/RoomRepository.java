package com.luddy.roomreservation.repository;

import com.luddy.roomreservation.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    // spring data will generate this method automatically
    List<Room> findByFloor(int floor);
}
