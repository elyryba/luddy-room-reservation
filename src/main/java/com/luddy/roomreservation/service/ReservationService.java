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
