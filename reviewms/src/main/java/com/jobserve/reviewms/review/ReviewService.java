package com.jobserve.reviewms.review;

import com.jobserve.reviewms.client.CompanyClient;
import com.jobserve.reviewms.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService {
    private final IReviewRepository reviewRepository;
    private final CompanyClient companyClient;
    @Autowired
    private RestTemplate restTemplate;

    public ReviewService(IReviewRepository reviewRepository, CompanyClient companyClient) {
        this.reviewRepository = reviewRepository;
        this.companyClient = companyClient;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findAllByCompanyId(companyId);
    }

    @Override
    public List<Review> getReviewsByRating(Long companyId, Double rating) {
        if (companyId == null) {
            return null;
        }
        List<Review> reviews = reviewRepository.findAllByCompanyId(companyId).stream()
                .filter(company -> company.getRating().equals(rating))
                .toList();
        return reviews;
    }

    @Override
    public boolean createReview(Long companyId, Review review) {
        CompanyDTO companyDTO = getCompany(companyId);
        if (companyDTO == null) {
            return false;
        }
        review.setCompanyId(companyId);
        try {
            reviewRepository.save(review);
        } catch (Exception e) {
            System.err.println("Unable to finish the creating process for new review");
        }
        return true;
    }

    @Override
    public void updateReview(Long reviewId, Review review) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        try {
            if (!optionalReview.isPresent()) {
                throw new Exception("Could not found review has id = " + reviewId);
            }
            Review updatedReview = optionalReview.get();
            updatedReview.setTitle(review.getTitle());
            updatedReview.setContent(review.getContent());
            updatedReview.setRating(review.getRating());

            reviewRepository.save(review);
        } catch (Exception e) {
            System.err.println("Could not updated the review id = " + reviewId);
        }
    }

    @Override
    public void deleteReview(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        try {
            if (!optionalReview.isPresent()) {
                throw new Exception("Could not found review has id = " + reviewId);
            }
            reviewRepository.delete(optionalReview.get());

        } catch (Exception e) {
            System.err.println("Could not deleted the review id = " + reviewId);
        }
    }

    @Override
    public Review getReviewById(Long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        return (reviewOptional.isPresent()) ? reviewOptional.get() : new Review();
    }

    private CompanyDTO getCompany(Long companyId) {
//        return restTemplate.getForObject("localhost:8081/companies/" + companyId, CompanyDTO.class);
        return this.restTemplate.getForObject("http://COMPANY-SERVICE:8081/companies/" + companyId, CompanyDTO.class);
    }
}
