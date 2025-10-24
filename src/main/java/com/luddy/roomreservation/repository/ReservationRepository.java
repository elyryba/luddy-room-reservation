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
