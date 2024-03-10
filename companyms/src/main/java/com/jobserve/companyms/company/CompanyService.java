package com.jobserve.companyms.company;

import com.jobserve.companyms.dto.ReviewDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService implements ICompanyService {

    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        return (optionalCompany.isPresent()) ? optionalCompany.get() : new Company();
    }

    @Override
    public void createCompany(Company company) {
        try {
            companyRepository.save(company);
        } catch (Exception e) {
            System.err.println("Unable to create new company.");
        }
    }

    @Override
    public void updateCompany(Long id, Company company) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        try {
            if (!optionalCompany.isPresent()) {
                throw new Exception("Could not found the company has id = " + id);
            }
            Company updatedCompany = optionalCompany.get();
            updatedCompany.setName(company.getName());
            updatedCompany.setDescription(company.getDescription());

            companyRepository.save(updatedCompany);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void deleteCompany(Long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        try {
            if (!optionalCompany.isPresent()) {
                throw new Exception("Could not found the company has id = " + id);
            }
            Company company = optionalCompany.get();
            companyRepository.delete(company);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void getReview(ReviewDTO reviewDTO) {
        System.out.println(reviewDTO);
    }
}
