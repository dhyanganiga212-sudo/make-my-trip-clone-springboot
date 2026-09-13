package com.makemytrip.makemytrip.services;
import com.makemytrip.makemytrip.models.Users;
import com.makemytrip.makemytrip.models.Users.Booking;
import com.makemytrip.makemytrip.models.Flight;
import com.makemytrip.makemytrip.models.Hotel;
import com.makemytrip.makemytrip.repositories.UserRepository;
import com.makemytrip.makemytrip.repositories.FlightRepository;
import com.makemytrip.makemytrip.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public Booking bookFlight(String userId,String flightId,int seats,double price){
        Optional<Users> usersOptional =userRepository.findById(userId);
        Optional<Flight> flightOptional =flightRepository.findById(flightId);
        if(usersOptional.isPresent() && flightOptional.isPresent()){
            Users user=usersOptional.get();
            Flight flight=flightOptional.get();
            if(flight.getAvailableSeats() >= seats){
                flight.setAvailableSeats(flight.getAvailableSeats()- seats);
                flightRepository.save(flight);

                Booking booking=new Booking();
                booking.setType("Flight");
                booking.setBookingId(flightId);
                booking.setDate(LocalDate.now().toString());
                booking.setQuantity(seats);
                booking.setTotalPrice(price);
                user.getBookings().add(booking);
                userRepository.save(user);
                return booking;
            }else {
                throw new RuntimeException("Not enough seats available");
            }
        }
        throw new RuntimeException("User or flight not found");
    }
    public Booking bookhotel(String userId,String hotelId,int rooms,double price){
        Optional<Users> usersOptional =userRepository.findById(userId);
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
        if(usersOptional.isPresent() && hotelOptional.isPresent()){
            Users user=usersOptional.get();
            Hotel hotel=hotelOptional.get();
            if(hotel.getAvailableRooms() >= rooms){
                hotel.setAvailableRooms(hotel.getAvailableRooms()- rooms);
                hotelRepository.save(hotel);

                Booking booking=new Booking();
                booking.setType("Hotel");
                booking.setBookingId(hotelId);
                booking.setDate(LocalDate.now().toString());
                booking.setQuantity(rooms);
                booking.setTotalPrice(price);
                user.getBookings().add(booking);
                userRepository.save(user);
                return booking;
            }else {
                throw new RuntimeException("Not enough rooms available");
            }
        }
        throw new RuntimeException("User or flight not found");
    }

    public Booking cancelBooking(String userId, String bookingInternalId, String reason){
        Optional<Users> usersOptional = userRepository.findById(userId);
        if(!usersOptional.isPresent()){
            throw new RuntimeException("User not found");
        }
        Users user = usersOptional.get();

        Booking targetBooking = null;
        for(Booking b : user.getBookings()){
            if(b.getId().equals(bookingInternalId)){
                targetBooking = b;
                break;
            }
        }

        if(targetBooking == null){
            throw new RuntimeException("Booking not found");
        }
        if(targetBooking.getIsCancelled()){
            throw new RuntimeException("Booking is already cancelled");
        }

        long hoursSinceBooking = (System.currentTimeMillis() - targetBooking.getCreatedAt()) / (1000 * 60 * 60);
        double refundPercentage = (hoursSinceBooking <= 24) ? 0.5 : 1.0;
        double refundAmount = targetBooking.getTotalPrice() * refundPercentage;

        targetBooking.setIsCancelled(true);
        targetBooking.setStatus("cancelled");
        targetBooking.setCancelledAt(LocalDate.now().toString());
        targetBooking.setCancellationReason(reason);
        targetBooking.setRefundAmount(refundAmount);
        targetBooking.setRefundStatus("pending");

        // Restore availability
        if(targetBooking.getType().equals("Flight")){
            Optional<Flight> flightOptional = flightRepository.findById(targetBooking.getBookingId());
            if(flightOptional.isPresent()){
                Flight flight = flightOptional.get();
                flight.setAvailableSeats(flight.getAvailableSeats() + targetBooking.getQuantity());
                flightRepository.save(flight);
            }
        } else if(targetBooking.getType().equals("Hotel")){
            Optional<Hotel> hotelOptional = hotelRepository.findById(targetBooking.getBookingId());
            if(hotelOptional.isPresent()){
                Hotel hotel = hotelOptional.get();
                hotel.setAvailableRooms(hotel.getAvailableRooms() + targetBooking.getQuantity());
                hotelRepository.save(hotel);
            }
        }

        userRepository.save(user);
        return targetBooking;
    }

}