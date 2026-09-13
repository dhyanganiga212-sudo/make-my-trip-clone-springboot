package com.makemytrip.makemytrip.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recommendation_feedback")
public class RecommendationFeedback {
    @Id
    private String _id;
    private String userId;
    private String itemType;
    private String itemId;
    private boolean isHelpful;
    private long createdAt;

    public RecommendationFeedback() {
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public boolean getIsHelpful() { return isHelpful; }
    public void setIsHelpful(boolean isHelpful) { this.isHelpful = isHelpful; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}