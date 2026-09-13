package com.makemytrip.makemytrip.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "hotels")
public class Hotel {
    @Id
    private String _id;
    private String hotelName;
    private String location;
    private double pricePerNight;
    private int availableRooms;
    private String amenities;
    private List<String> tags = new ArrayList<>();

    public String getId() {
        return _id;
    }
    public void setId(String id) {
        this._id = id;
    }
    public void setamenities(String amenities) {
        this.amenities = amenities;
    }
    public String getamenities() {
        return amenities;
    }
    public String gethotelName() {
        return hotelName;
    }
    public void sethotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public int getAvailableRooms() {
        return availableRooms;
    }
    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }
    public double getPricePerNight() {
        return pricePerNight;
    }
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}