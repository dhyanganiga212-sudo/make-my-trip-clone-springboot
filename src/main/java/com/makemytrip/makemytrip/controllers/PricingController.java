package com.makemytrip.makemytrip.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.makemytrip.makemytrip.models.Flight;
import com.makemytrip.makemytrip.models.Hotel;
import com.makemytrip.makemytrip.models.PriceHistory;
import com.makemytrip.makemytrip.models.PriceFreeze;
import com.makemytrip.makemytrip.repositories.FlightRepository;
import com.makemytrip.makemytrip.repositories.HotelRepository;
import com.makemytrip.makemytrip.repositories.PriceHistoryRepository;
import com.makemytrip.makemytrip.repositories.PriceFreezeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pricing")
@CrossOrigin(origins = "*")
public class PricingController {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private PriceHistoryRepository priceHistoryRepository;
    @Autowired
    private PriceFreezeRepository priceFreezeRepository;

    // MOCK ENGINE: simulate a demand/seasonal price change
    @PatchMapping("/{itemType}/{id}/simulate-price-change")
    public ResponseEntity<?> simulatePriceChange(
            @PathVariable String itemType,
            @PathVariable String id,
            @RequestParam double percentageChange,
            @RequestParam String reason) {

        if (itemType.equals("flight")) {
            Optional<Flight> flightOptional = flightRepository.findById(id);
            if (!flightOptional.isPresent()) return ResponseEntity.notFound().build();

            Flight flight = flightOptional.get();
            double oldPrice = flight.getPrice();
            double newPrice = Math.round(oldPrice * (1 + percentageChange / 100.0) * 100.0) / 100.0;
            flight.setPrice(newPrice);
            flightRepository.save(flight);

            PriceHistory history = new PriceHistory();
            history.setItemType(itemType);
            history.setItemId(id);
            history.setOldPrice(oldPrice);
            history.setNewPrice(newPrice);
            history.setReason(reason);
            priceHistoryRepository.save(history);

            return ResponseEntity.ok(flight);

        } else if (itemType.equals("hotel")) {
            Optional<Hotel> hotelOptional = hotelRepository.findById(id);
            if (!hotelOptional.isPresent()) return ResponseEntity.notFound().build();

            Hotel hotel = hotelOptional.get();
            double oldPrice = hotel.getPricePerNight();
            double newPrice = Math.round(oldPrice * (1 + percentageChange / 100.0) * 100.0) / 100.0;
            hotel.setPricePerNight(newPrice);
            hotelRepository.save(hotel);

            PriceHistory history = new PriceHistory();
            history.setItemType(itemType);
            history.setItemId(id);
            history.setOldPrice(oldPrice);
            history.setNewPrice(newPrice);
            history.setReason(reason);
            priceHistoryRepository.save(history);

            return ResponseEntity.ok(hotel);
        }

        return ResponseEntity.badRequest().body("itemType must be 'flight' or 'hotel'");
    }

    // Get price history for graphing
    @GetMapping("/{itemType}/{id}/history")
    public List<PriceHistory> getPriceHistory(@PathVariable String itemType, @PathVariable String id) {
        return priceHistoryRepository.findByItemTypeAndItemId(itemType, id);
    }

    // Freeze the current price for a user for N minutes
    @PostMapping("/{itemType}/{id}/freeze")
    public ResponseEntity<?> freezePrice(
            @PathVariable String itemType,
            @PathVariable String id,
            @RequestParam String userId,
            @RequestParam(defaultValue = "15") int freezeMinutes) {

        double currentPrice;
        if (itemType.equals("flight")) {
            Optional<Flight> flightOptional = flightRepository.findById(id);
            if (!flightOptional.isPresent()) return ResponseEntity.notFound().build();
            currentPrice = flightOptional.get().getPrice();
        } else if (itemType.equals("hotel")) {
            Optional<Hotel> hotelOptional = hotelRepository.findById(id);
            if (!hotelOptional.isPresent()) return ResponseEntity.notFound().build();
            currentPrice = hotelOptional.get().getPricePerNight();
        } else {
            return ResponseEntity.badRequest().body("itemType must be 'flight' or 'hotel'");
        }

        PriceFreeze freeze = new PriceFreeze();
        freeze.setUserId(userId);
        freeze.setItemType(itemType);
        freeze.setItemId(id);
        freeze.setFrozenPrice(currentPrice);
        freeze.setExpiresAt(System.currentTimeMillis() + (freezeMinutes * 60L * 1000L));
        priceFreezeRepository.save(freeze);

        return ResponseEntity.ok(freeze);
    }

    // Check if user has an active freeze on an item
    @GetMapping("/{itemType}/{id}/freeze-status")
    public ResponseEntity<?> getFreezeStatus(
            @PathVariable String itemType,
            @PathVariable String id,
            @RequestParam String userId) {

        List<PriceFreeze> freezes = priceFreezeRepository.findByUserIdAndItemTypeAndItemId(userId, itemType, id);
        long now = System.currentTimeMillis();

        for (PriceFreeze freeze : freezes) {
            if (freeze.getExpiresAt() > now) {
                return ResponseEntity.ok(freeze);
            }
        }
        return ResponseEntity.ok().body("No active freeze");
    }
}