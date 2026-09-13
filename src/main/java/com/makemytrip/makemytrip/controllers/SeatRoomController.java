package com.makemytrip.makemytrip.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.makemytrip.makemytrip.models.Flight;
import com.makemytrip.makemytrip.models.Hotel;
import com.makemytrip.makemytrip.models.Users;
import com.makemytrip.makemytrip.repositories.FlightRepository;
import com.makemytrip.makemytrip.repositories.HotelRepository;
import com.makemytrip.makemytrip.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/select")
@CrossOrigin(origins = "*")
public class SeatRoomController {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private UserRepository userRepository;

    // Get the seat map for a flight (generates one on the fly if empty)
    @GetMapping("/flights/{id}/seatmap")
    public ResponseEntity<?> getSeatMap(@PathVariable String id) {
        Optional<Flight> flightOptional = flightRepository.findById(id);
        if (!flightOptional.isPresent()) return ResponseEntity.notFound().build();

        Flight flight = flightOptional.get();
        if (flight.getSeatMap().isEmpty()) {
            List<Flight.Seat> seats = new java.util.ArrayList<>();
            String[] letters = {"A", "B", "C", "D"};
            for (int row = 1; row <= 5; row++) {
                String seatClass = (row == 1) ? "Business" : (row <= 3) ? "Premium" : "Economy";
                double extraPrice = (row == 1) ? 2000 : (row <= 3) ? 800 : 0;
                for (String letter : letters) {
                    seats.add(new Flight.Seat(row + letter, seatClass, extraPrice));
                }
            }
            flight.setSeatMap(seats);
            flightRepository.save(flight);
        }
        return ResponseEntity.ok(flight.getSeatMap());
    }

    // Select/reserve a specific seat
    @PatchMapping("/flights/{id}/select-seat")
    public ResponseEntity<?> selectSeat(@PathVariable String id, @RequestParam String seatNumber) {
        Optional<Flight> flightOptional = flightRepository.findById(id);
        if (!flightOptional.isPresent()) return ResponseEntity.notFound().build();

        Flight flight = flightOptional.get();
        for (Flight.Seat seat : flight.getSeatMap()) {
            if (seat.getSeatNumber().equals(seatNumber)) {
                if (seat.getIsBooked()) {
                    return ResponseEntity.status(409).body("Seat already booked");
                }
                seat.setIsBooked(true);
                flightRepository.save(flight);
                return ResponseEntity.ok(seat);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Get the room-type grid for a hotel (single room type per hotel in this model)
    @GetMapping("/hotels/{id}/rooms")
    public ResponseEntity<?> getRoomGrid(@PathVariable String id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (!hotelOptional.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(hotelOptional.get());
    }

    // Save the logged-in user's seat/room preferences
    @PatchMapping("/preferences")
    public ResponseEntity<?> savePreferences(
            @RequestParam String userId,
            @RequestParam(required = false) String preferredSeatClass,
            @RequestParam(required = false) String preferredRoomType) {

        Optional<Users> usersOptional = userRepository.findById(userId);
        if (!usersOptional.isPresent()) return ResponseEntity.notFound().build();

        Users user = usersOptional.get();
        if (preferredSeatClass != null) user.setPreferredSeatClass(preferredSeatClass);
        if (preferredRoomType != null) user.setPreferredRoomType(preferredRoomType);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}