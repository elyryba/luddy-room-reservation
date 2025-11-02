package com.luddy.roomreservation.service;
import com.luddy.roomreservation.model.Reservation;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
@Service
public class EmailService {
    public void sendBookingConfirmation(Reservation reservation, String roomNumber) {
        String email = String.format(
            "TO: %s\nSUBJECT: Room Booking Confirmation - Room %s\n\nDear %s,\n\nYour booking has been confirmed!\n\nDetails:\nRoom: %s\nStart: %s\nEnd: %s\nPurpose: %s\n\nThank you!",
            reservation.getUserEmail(), roomNumber, reservation.getUserName(), roomNumber,
            reservation.getStartTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")),
            reservation.getEndTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")),
            reservation.getPurpose()
        );
        System.out.println("\n========== EMAIL SENT ==========");
        System.out.println(email);
        System.out.println("================================\n");
    }
}
