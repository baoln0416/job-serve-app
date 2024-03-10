package com.jobserve.companyms.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewDTO {
    private Long id;
    private String title;
    private String description;
    private double rating;
    private Long companyId;
}
