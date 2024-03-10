package com.jobserve.jobms.job;

import com.jobserve.jobms.clients.CompanyClient;
import com.jobserve.jobms.dto.CompanyDTO;
import com.jobserve.jobms.dto.JobWithCompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class JobService implements IJobService {
    private final IJobRepository jobRepository;
    private final CompanyClient companyClient;
    @Autowired
    private RestTemplate restTemplate;

    public JobService(IJobRepository jobRepository, CompanyClient companyClient)
    {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
    }

    @Override
    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()) {
            return optionalJob.get();
        } else {
            return null;
        }
    }

    @Override
    public JobWithCompanyDTO getJobWithRelatedCompany(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            return convert(job);
        }
        return null;
    }

    @Override
    public JobWithCompanyDTO getJobWithRelatedCompanyByFeignClient(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (!optionalJob.isPresent()) {
            return  null;
        }
        Job job = optionalJob.get();
        CompanyDTO companyDTO = this.companyClient.getCompany(job.getCompanyId());
        return new JobWithCompanyDTO(job, companyDTO);
    }

    @Override
    public void addNewJob(Job job) {
        try {
            jobRepository.save(job);
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void updateJob(Long id, Job job) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        try {
            if (!optionalJob.isPresent()) {
                throw new Exception("Could not found any job has id = " + id);
            }
            Job updatedJob = optionalJob.get();
            updatedJob.setTitle(job.getTitle());
            updatedJob.setDescription(job.getDescription());
            updatedJob.setMinSalary(job.getMinSalary());
            updatedJob.setMaxSalary(job.getMaxSalary());
            updatedJob.setLocation(job.getLocation());
            updatedJob.setDueDate(job.getDueDate());
            updatedJob.setCompanyId(job.getCompanyId());

            jobRepository.save(updatedJob);
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    @Override
    public boolean deleteJob(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        try {
            if (!optionalJob.isPresent()) {

            }
            jobRepository.deleteById(id);
        } catch (IllegalArgumentException illegalArgumentException) {
            return false;
        }
        return true;
    }

    private JobWithCompanyDTO convert(Job job) {
        CompanyDTO companyDTO = this.restTemplate.getForObject("http://COMPANY-SERVICE:8081/companies/" + job.getCompanyId(), CompanyDTO.class);
        return new JobWithCompanyDTO(job, companyDTO);
    }
}
