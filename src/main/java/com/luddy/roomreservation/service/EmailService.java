package com.luddy.roomreservation.service;
import com.luddy.roomreservation.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
@Service
public class EmailService {
    @Autowired(required = false)
    private JavaMailSender mailSender;
    public void sendBookingConfirmation(Reservation reservation, String roomNumber) {
        String body = String.format(
            "Dear %s,\n\nYour booking has been confirmed!\n\nDetails:\nRoom: %s\nStart: %s\nEnd: %s\nPurpose: %s\n\nThank you!",
            reservation.getUserName(), roomNumber,
            reservation.getStartTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")),
            reservation.getEndTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")),
            reservation.getPurpose()
        );
        System.out.println("\n========== EMAIL ==========");
        System.out.println("TO: " + reservation.getUserEmail());
        System.out.println(body);
        System.out.println("===========================\n");
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(reservation.getUserEmail());
                message.setSubject("Room Booking Confirmation - Room " + roomNumber);
                message.setText(body);
                mailSender.send(message);
                System.out.println("Email sent successfully!");
            } catch (Exception e) {
                System.out.println("Email send failed: " + e.getMessage());
            }
        }
    }
}
