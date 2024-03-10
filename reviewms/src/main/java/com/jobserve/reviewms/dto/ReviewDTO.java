package com.jobserve.reviewms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private String title;
    private String description;
    private double rating;
    private Long companyId;
}
