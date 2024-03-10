package com.jobserve.reviewms.review;

import com.jobserve.reviewms.message.RabbitMQProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final IReviewService reviewService;
    private final RabbitMQProducer rabbitMQProducer;

    public ReviewController(IReviewService reviewService, RabbitMQProducer rabbitMQProducer) {
        this.reviewService = reviewService;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId) {
        return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
    }

    @GetMapping("/id/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<Review>> getReviewsByRating(@RequestParam Long companyId,
                                                           @PathVariable Double rating) {
        return new ResponseEntity<>(reviewService.getReviewsByRating(companyId, rating), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createNewReview(@RequestParam Long companyId,
                                                  @RequestBody Review review) {
        boolean created = reviewService.createReview(companyId, review);
        if (created) {
            this.rabbitMQProducer.sendJsonMessage(review);
            return new ResponseEntity<>("Created new review successfully.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Could not found any company has id=" + companyId, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,
                                               @RequestBody Review review) {
        reviewService.updateReview(reviewId, review);
        return new ResponseEntity<>("Updated the review successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>("Deleted the review successfully!", HttpStatus.OK);
    }

    @GetMapping("/rabbitmq/message")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        rabbitMQProducer.sendMessage(message);
        return new ResponseEntity<>(String.format("Sent message -> %s", message), HttpStatus.OK);
    }

    @GetMapping("/rating/average")
    public ResponseEntity<Double> calculateAverageRating(@RequestParam("companyId") Long companyId) {
        List<Review> reviews = reviewService.getAllReviews(companyId);
        double averageRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }
}
