package com.jobserve.companyms.company;

import com.jobserve.companyms.dto.ReviewDTO;

import java.util.List;

public interface ICompanyService {
    List<Company> getAllCompanies();
    Company getCompanyById(Long id);
    void createCompany(Company company);
    void updateCompany(Long id, Company company);
    void deleteCompany(Long id);
    void getReview(ReviewDTO reviewDTO);
}
