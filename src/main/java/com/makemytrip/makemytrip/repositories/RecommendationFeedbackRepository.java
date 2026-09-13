package com.makemytrip.makemytrip.repositories;
import com.makemytrip.makemytrip.models.RecommendationFeedback;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecommendationFeedbackRepository extends MongoRepository<RecommendationFeedback, String> {
}