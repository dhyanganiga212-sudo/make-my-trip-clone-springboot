package com.makemytrip.makemytrip.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "reviews")
public class Review {
    @Id
    private String _id;
    private String userId;
    private String itemType;
    private String itemId;
    private int rating;
    private String text;
    private List<String> photos = new ArrayList<>();
    private int helpfulCount = 0;
    private List<Reply> replies = new ArrayList<>();
    private boolean isFlagged = false;
    private String flagReason = "";
    private boolean isRemoved = false;
    private long createdAt;

    public Review() {
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
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }
    public int getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(int helpfulCount) { this.helpfulCount = helpfulCount; }
    public List<Reply> getReplies() { return replies; }
    public void setReplies(List<Reply> replies) { this.replies = replies; }
    public boolean getIsFlagged() { return isFlagged; }
    public void setIsFlagged(boolean isFlagged) { this.isFlagged = isFlagged; }
    public String getFlagReason() { return flagReason; }
    public void setFlagReason(String flagReason) { this.flagReason = flagReason; }
    public boolean getIsRemoved() { return isRemoved; }
    public void setIsRemoved(boolean isRemoved) { this.isRemoved = isRemoved; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public static class Reply {
        private String userId;
        private String text;
        private long createdAt;

        public Reply() {
            this.createdAt = System.currentTimeMillis();
        }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    }
}