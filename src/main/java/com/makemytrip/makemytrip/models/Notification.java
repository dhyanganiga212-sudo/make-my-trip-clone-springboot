package com.makemytrip.makemytrip.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
public class Notification {
    @Id
    private String _id;
    private String userId;
    private String flightId;
    private String message;
    private String previousStatus;
    private String newStatus;
    private long createdAt;
    private boolean isRead = false;

    public Notification() {
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return _id;
    }
    public void setId(String id) {
        this._id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getFlightId() {
        return flightId;
    }
    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getPreviousStatus() {
        return previousStatus;
    }
    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }
    public String getNewStatus() {
        return newStatus;
    }
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
    public long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    public boolean getIsRead() {
        return isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}