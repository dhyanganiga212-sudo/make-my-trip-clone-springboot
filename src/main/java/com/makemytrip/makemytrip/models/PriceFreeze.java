package com.makemytrip.makemytrip.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "price_freeze")
public class PriceFreeze {
    @Id
    private String _id;
    private String userId;
    private String itemType;
    private String itemId;
    private double frozenPrice;
    private long expiresAt;

    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public double getFrozenPrice() { return frozenPrice; }
    public void setFrozenPrice(double frozenPrice) { this.frozenPrice = frozenPrice; }
    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
}