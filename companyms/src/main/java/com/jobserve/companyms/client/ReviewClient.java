package com.jobserve.companyms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "REVIEW-SERVICE")
public interface ReviewClient {

    @GetMapping("/rating/average")
    double calculateAverageRating(@RequestParam("companyId") Long companyId);
}
