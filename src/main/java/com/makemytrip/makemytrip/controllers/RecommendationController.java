package com.makemytrip.makemytrip.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.makemytrip.makemytrip.models.Hotel;
import com.makemytrip.makemytrip.models.Users;
import com.makemytrip.makemytrip.models.RecommendationFeedback;
import com.makemytrip.makemytrip.repositories.HotelRepository;
import com.makemytrip.makemytrip.repositories.UserRepository;
import com.makemytrip.makemytrip.repositories.RecommendationFeedbackRepository;

import java.util.*;

@RestController
@RequestMapping("/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecommendationFeedbackRepository feedbackRepository;

    @GetMapping("/hotels")
    public ResponseEntity<?> getRecommendations(@RequestParam String userId) {
        Optional<Users> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) return ResponseEntity.notFound().build();

        Users user = userOptional.get();
        List<String> bookedHotelIds = new ArrayList<>();
        Map<String, Integer> tagCounts = new HashMap<>();

        for (Users.Booking booking : user.getBookings()) {
            if (booking.getType().equals("Hotel")) {
                bookedHotelIds.add(booking.getBookingId());
                Optional<Hotel> hotelOptional = hotelRepository.findById(booking.getBookingId());
                if (hotelOptional.isPresent()) {
                    for (String tag : hotelOptional.get().getTags()) {
                        tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                    }
                }
            }
        }

        List<Hotel> allHotels = hotelRepository.findAll();
        List<Map<String, Object>> recommendations = new ArrayList<>();

        if (tagCounts.isEmpty()) {
            // Fallback: no history yet, suggest popular hotels
            for (Hotel hotel : allHotels) {
                if (recommendations.size() >= 3) break;
                Map<String, Object> rec = new HashMap<>();
                rec.put("hotel", hotel);
                rec.put("reason", "Popular pick - book a hotel to get personalized suggestions!");
                recommendations.add(rec);
            }
        } else {
            List<String> topTags = new ArrayList<>(tagCounts.keySet());
            topTags.sort((a, b) -> tagCounts.get(b) - tagCounts.get(a));

            for (Hotel hotel : allHotels) {
                if (bookedHotelIds.contains(hotel.getId())) continue;
                for (String tag : hotel.getTags()) {
                    if (topTags.contains(tag)) {
                        Map<String, Object> rec = new HashMap<>();
                        rec.put("hotel", hotel);
                        rec.put("reason", "You liked " + tag + "! Try " + hotel.gethotelName() + " in " + hotel.getLocation() + ".");
                        recommendations.add(rec);
                        break;
                    }
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("recommendations", recommendations);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> submitFeedback(
            @RequestParam String userId,
            @RequestParam String itemType,
            @RequestParam String itemId,
            @RequestParam boolean isHelpful) {

        RecommendationFeedback feedback = new RecommendationFeedback();
        feedback.setUserId(userId);
        feedback.setItemType(itemType);
        feedback.setItemId(itemId);
        feedback.setIsHelpful(isHelpful);
        feedbackRepository.save(feedback);

        return ResponseEntity.ok(feedback);
    }
}