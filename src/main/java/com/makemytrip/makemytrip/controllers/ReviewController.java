package com.makemytrip.makemytrip.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.makemytrip.makemytrip.models.Review;
import com.makemytrip.makemytrip.repositories.ReviewRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

    // Create a review
    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestParam String userId,
            @RequestParam String itemType,
            @RequestParam String itemId,
            @RequestParam int rating,
            @RequestParam String text,
            @RequestParam(required = false) List<String> photos) {

        if (rating < 1 || rating > 5) {
            return ResponseEntity.badRequest().body("rating must be between 1 and 5");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setItemType(itemType);
        review.setItemId(itemId);
        review.setRating(rating);
        review.setText(text);
        if (photos != null) review.setPhotos(photos);

        reviewRepository.save(review);
        return ResponseEntity.ok(review);
    }

    // Get reviews for an item, sorted/filtered
    @GetMapping("/{itemType}/{itemId}")
    public List<Review> getReviews(
            @PathVariable String itemType,
            @PathVariable String itemId,
            @RequestParam(defaultValue = "newest") String sortBy) {

        List<Review> reviews = reviewRepository.findByItemTypeAndItemIdAndIsRemovedFalse(itemType, itemId);

        if (sortBy.equals("highest")) {
            reviews.sort(Comparator.comparingInt(Review::getRating).reversed());
        } else if (sortBy.equals("helpful")) {
            reviews.sort(Comparator.comparingInt(Review::getHelpfulCount).reversed());
        } else {
            reviews.sort(Comparator.comparingLong(Review::getCreatedAt).reversed());
        }

        return reviews;
    }

    // Reply to a review
    @PostMapping("/{id}/reply")
    public ResponseEntity<?> replyToReview(@PathVariable String id, @RequestParam String userId, @RequestParam String text) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (!reviewOptional.isPresent()) return ResponseEntity.notFound().build();

        Review review = reviewOptional.get();
        Review.Reply reply = new Review.Reply();
        reply.setUserId(userId);
        reply.setText(text);
        review.getReplies().add(reply);
        reviewRepository.save(review);

        return ResponseEntity.ok(review);
    }

    // Mark a review as helpful
    @PatchMapping("/{id}/helpful")
    public ResponseEntity<?> markHelpful(@PathVariable String id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (!reviewOptional.isPresent()) return ResponseEntity.notFound().build();

        Review review = reviewOptional.get();
        review.setHelpfulCount(review.getHelpfulCount() + 1);
        reviewRepository.save(review);

        return ResponseEntity.ok(review);
    }

    // Flag a review as inappropriate
    @PatchMapping("/{id}/flag")
    public ResponseEntity<?> flagReview(@PathVariable String id, @RequestParam String reason) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (!reviewOptional.isPresent()) return ResponseEntity.notFound().build();

        Review review = reviewOptional.get();
        review.setIsFlagged(true);
        review.setFlagReason(reason);
        reviewRepository.save(review);

        return ResponseEntity.ok(review);
    }

    // MODERATOR: get all flagged reviews
    @GetMapping("/flagged/all")
    public List<Review> getFlaggedReviews() {
        return reviewRepository.findByIsFlaggedTrueAndIsRemovedFalse();
    }

    // MODERATOR: remove a flagged review
    @PatchMapping("/{id}/remove")
    public ResponseEntity<?> removeReview(@PathVariable String id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (!reviewOptional.isPresent()) return ResponseEntity.notFound().build();

        Review review = reviewOptional.get();
        review.setIsRemoved(true);
        reviewRepository.save(review);

        return ResponseEntity.ok(review);
    }
}