package com.jobserve.reviewms.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {
    @Query(value = "SELECT * FROM Review r WHERE r.company_id = ?1", nativeQuery = true)
    List<Review> findAllByCompanyId(Long companyId);
}
