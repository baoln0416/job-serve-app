package com.jobserve.jobms.job;

import com.jobserve.jobms.dto.JobWithCompanyDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private IJobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/all")
    @CircuitBreaker(name = "jobBreaker")
    public ResponseEntity<List<Job>> findAllJobs() {
        List<Job> jobs = jobService.findAllJobs();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable("id") Long id) {
        Job job = jobService.getJobById(id);
        return new ResponseEntity<>(job,HttpStatus.OK);
    }

    @PostMapping("/new-job")
    public ResponseEntity<String> addNewJob(@RequestBody Job job) {
        jobService.addNewJob(job);
        return new ResponseEntity<String>("Added new job successfully! ", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job job) {
        jobService.updateJob(id, job);
        return new ResponseEntity<>("Updated job successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable("id") Long id) {
        boolean deleted = jobService.deleteJob(id);
        if (deleted) {
            return new ResponseEntity<>("Deleted job successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not found jod has id = " + id, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/job-with-company/{id}")
    public ResponseEntity<JobWithCompanyDTO> getJobWithCompany(@PathVariable("id") Long id) {
        return new ResponseEntity<>(jobService.getJobWithRelatedCompany(id), HttpStatus.OK);
    }

    @GetMapping("/job-with-company/feign-client/{id}")
    public ResponseEntity<JobWithCompanyDTO> getJobWithCompanyByFeignClient(@PathVariable("id") Long id) {
        return new ResponseEntity<>(jobService.getJobWithRelatedCompanyByFeignClient(id), HttpStatus.OK);
    }
}
