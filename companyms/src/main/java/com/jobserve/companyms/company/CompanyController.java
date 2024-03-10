package com.jobserve.companyms.company;

import com.jobserve.companyms.message.RabbitMQProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final ICompanyService companyService;
    private final RabbitMQProducer rabbitMQProducer;

    public CompanyController(ICompanyService companyService, RabbitMQProducer rabbitMQProducer) {
        this.companyService = companyService;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PostMapping("/new-company")
    public ResponseEntity<String> createNewCompany(@RequestBody Company company) {
        companyService.createCompany(company);
        return new ResponseEntity<String>("Added new company successfully! ", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Company company) {
        companyService.updateCompany(id, company);
        return new ResponseEntity<>("Updated company id=" + id + " successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return new ResponseEntity<>("Deleted company id=" + id + " successfully!", HttpStatus.OK);
    }

    @GetMapping("/rabbitmq/message")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        rabbitMQProducer.sendMessage(message);
        return new ResponseEntity<>("Sent message to RabbitMQ", HttpStatus.OK);
    }
}
