package com.makemytrip.makemytrip.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "flight")
public class Flight {
    @Id
    private String _id;
    private String flightName;
    private String from;
    private String to;
    private String departureTime;
    private String arrivalTime;
    private double price;
    private int availableSeats;
    private String status = "On Time";
    private String delayReason = "";
    private String estimatedArrival;
    private List<Seat> seatMap = new ArrayList<>();

    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }
    public String getFlightName() { return flightName; }
    public void setFlightName(String flightName) { this.flightName = flightName; }
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDelayReason() { return delayReason; }
    public void setDelayReason(String delayReason) { this.delayReason = delayReason; }
    public String getEstimatedArrival() { return estimatedArrival; }
    public void setEstimatedArrival(String estimatedArrival) { this.estimatedArrival = estimatedArrival; }
    public List<Seat> getSeatMap() { return seatMap; }
    public void setSeatMap(List<Seat> seatMap) { this.seatMap = seatMap; }

    public static class Seat {
        private String seatNumber;
        private String seatClass;
        private double extraPrice;
        private boolean isBooked = false;

        public Seat() {}
        public Seat(String seatNumber, String seatClass, double extraPrice) {
            this.seatNumber = seatNumber;
            this.seatClass = seatClass;
            this.extraPrice = extraPrice;
        }

        public String getSeatNumber() { return seatNumber; }
        public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
        public String getSeatClass() { return seatClass; }
        public void setSeatClass(String seatClass) { this.seatClass = seatClass; }
        public double getExtraPrice() { return extraPrice; }
        public void setExtraPrice(double extraPrice) { this.extraPrice = extraPrice; }
        public boolean getIsBooked() { return isBooked; }
        public void setIsBooked(boolean isBooked) { this.isBooked = isBooked; }
    }
}