package com.makemytrip.makemytrip.repositories;
import com.makemytrip.makemytrip.models.PriceFreeze;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PriceFreezeRepository extends MongoRepository<PriceFreeze, String> {
    List<PriceFreeze> findByUserIdAndItemTypeAndItemId(String userId, String itemType, String itemId);
}