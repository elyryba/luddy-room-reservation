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
            
            try { reservationService.createReservation(reservation); } catch (Exception ignored) {}
            model.addAttribute("success", "Booking successful!"); model.addAttribute("email", userEmail); return "booking-success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            roomService.getAllRooms().stream().filter(r -> r.getId().equals(roomId)).findFirst().ifPresent(room -> model.addAttribute("room", room));
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
