package com.makemytrip.makemytrip.repositories;
import com.makemytrip.makemytrip.models.PriceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PriceHistoryRepository extends MongoRepository<PriceHistory, String> {
    List<PriceHistory> findByItemTypeAndItemId(String itemType, String itemId);
}