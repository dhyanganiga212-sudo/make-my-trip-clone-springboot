package com.makemytrip.makemytrip.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.makemytrip.makemytrip.models.Flight;
import com.makemytrip.makemytrip.models.Users;
import com.makemytrip.makemytrip.models.Notification;
import com.makemytrip.makemytrip.repositories.FlightRepository;
import com.makemytrip.makemytrip.repositories.UserRepository;
import com.makemytrip.makemytrip.repositories.NotificationRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flights")
@CrossOrigin(origins = "*")
public class FlightController {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // Get live status of a single flight
    @GetMapping("/{id}/status")
    public ResponseEntity<Flight> getFlightStatus(@PathVariable String id) {
        Optional<Flight> flightOptional = flightRepository.findById(id);
        if (flightOptional.isPresent()) {
            return ResponseEntity.ok(flightOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    // MOCK API: simulate a status update on a flight, notify affected users
    @PatchMapping("/{id}/simulate-status")
    public ResponseEntity<Flight> simulateStatus(
            @PathVariable String id,
            @RequestParam String newStatus,
            @RequestParam(required = false) String delayReason,
            @RequestParam(required = false) String estimatedArrival) {

        Optional<Flight> flightOptional = flightRepository.findById(id);
        if (!flightOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Flight flight = flightOptional.get();
        String previousStatus = flight.getStatus();

        flight.setStatus(newStatus);
        if (delayReason != null) flight.setDelayReason(delayReason);
        if (estimatedArrival != null) flight.setEstimatedArrival(estimatedArrival);
        flightRepository.save(flight);

        // Notify every user who has an active (not cancelled) booking on this flight
        List<Users> allUsers = userRepository.findAll();
        String message = "Flight " + flight.getFlightName() + " status changed from " + previousStatus + " to " + newStatus + ".";
        if (delayReason != null) message += " Reason: " + delayReason + ".";

        for (Users user : allUsers) {
            for (Users.Booking booking : user.getBookings()) {
                if (booking.getType().equals("Flight") && booking.getBookingId().equals(id) && !booking.getIsCancelled()) {
                    Notification notification = new Notification();
                    notification.setUserId(user.getId());
                    notification.setFlightId(id);
                    notification.setMessage(message);
                    notification.setPreviousStatus(previousStatus);
                    notification.setNewStatus(newStatus);
                    notificationRepository.save(notification);
                }
            }
        }

        return ResponseEntity.ok(flight);
    }

    // Get all notifications for a user
    @GetMapping("/notifications")
    public List<Notification> getUserNotifications(@RequestParam String userId) {
        return notificationRepository.findByUserId(userId);
    }
}