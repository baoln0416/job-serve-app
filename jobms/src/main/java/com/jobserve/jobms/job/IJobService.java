package com.jobserve.jobms.job;

import com.jobserve.jobms.dto.JobWithCompanyDTO;

import java.util.List;

public interface IJobService {
    List<Job> findAllJobs();
    Job getJobById(Long id);
    JobWithCompanyDTO getJobWithRelatedCompany(Long id);
    JobWithCompanyDTO getJobWithRelatedCompanyByFeignClient(Long id);
    void addNewJob(Job job);
    void updateJob(Long id, Job job);
    boolean deleteJob(Long id);
}
