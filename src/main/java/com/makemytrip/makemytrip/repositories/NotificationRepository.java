package com.makemytrip.makemytrip.repositories;
import com.makemytrip.makemytrip.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
}