package com.makemytrip.makemytrip.repositories;
import com.makemytrip.makemytrip.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByItemTypeAndItemIdAndIsRemovedFalse(String itemType, String itemId);
    List<Review> findByIsFlaggedTrueAndIsRemovedFalse();
}