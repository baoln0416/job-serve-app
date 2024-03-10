package com.jobserve.reviewms.review;

import java.util.List;

public interface IReviewService {
    List<Review> getAllReviews(Long companyId);
    List<Review> getReviewsByRating(Long companyId, Double rating);
    boolean createReview(Long companyId, Review review);
    void updateReview(Long reviewId, Review review);
    void deleteReview(Long reviewId);
    Review getReviewById(Long reviewId);
}
