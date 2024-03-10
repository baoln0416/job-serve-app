package com.jobserve.companyms.message;

import com.jobserve.companyms.client.ReviewClient;
import com.jobserve.companyms.company.Company;
import com.jobserve.companyms.company.ICompanyRepository;
import com.jobserve.companyms.company.ICompanyService;
import com.jobserve.companyms.dto.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class RabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final ICompanyRepository companyRepository;
    private final RestTemplate restTemplate;

    public RabbitMQConsumer(ICompanyRepository companyRepository, RestTemplate restTemplate) {
        this.companyRepository = companyRepository;
        this.restTemplate = restTemplate;
    }

    @RabbitListener(queues = {"${rabbitmq.review-to-company.queue.name}"})
    public void getMessageFromReviewQueue(ReviewDTO reviewDTO) {
        if (reviewDTO == null) {
            LOGGER.error("Message is empty!");
            return;
        }
        LOGGER.info(String.format("Received message -> %s", reviewDTO.toString()));
        Optional<Company> companyOptional = companyRepository.findById(reviewDTO.getCompanyId());
        if (!companyOptional.isPresent()) {
            LOGGER.info(String.format("Could not found any company has id = %s", reviewDTO.getCompanyId()));
            return;
        }
        Company company = companyOptional.get();
        LOGGER.info(String.format("Company info -> %s", company.toString()));
        Double averageRating = restTemplate.getForObject(
                String.format("http://REVIEW-SERVICE/reviews/rating/average?companyId=%d", company.getId()),
                Double.class);
        company.setAverageRating(averageRating);

        try {
            companyRepository.save(company);
            LOGGER.info(String.format("Updated company(id=%d) set new average rating = %f", averageRating));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
