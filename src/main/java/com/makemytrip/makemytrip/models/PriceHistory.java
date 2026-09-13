package com.makemytrip.makemytrip.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "price_history")
public class PriceHistory {
    @Id
    private String _id;
    private String itemType;
    private String itemId;
    private double oldPrice;
    private double newPrice;
    private String reason;
    private long createdAt;

    public PriceHistory() {
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public double getOldPrice() { return oldPrice; }
    public void setOldPrice(double oldPrice) { this.oldPrice = oldPrice; }
    public double getNewPrice() { return newPrice; }
    public void setNewPrice(double newPrice) { this.newPrice = newPrice; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}