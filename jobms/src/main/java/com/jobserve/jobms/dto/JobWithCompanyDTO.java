package com.jobserve.jobms.dto;

import com.jobserve.jobms.job.Job;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobWithCompanyDTO {
    private Job job;
    private CompanyDTO companyDTO;
}
