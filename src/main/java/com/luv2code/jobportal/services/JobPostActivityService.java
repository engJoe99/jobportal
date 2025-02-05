package com.luv2code.jobportal.services;


import com.luv2code.jobportal.entity.*;
import com.luv2code.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }


    /**
     * Retrieves a list of jobs posted by a specific recruiter
     * @param recruiter The ID of the recruiter
     * @return List of RecruiterJobsDTO containing job details
     */
    public List<RecruiterJobsDTO> getRecruiterJobs(int recruiter) {

        // Get raw recruiter jobs data from repository
        List<IRecruiterJobs> recruiterJobsDtos = jobPostActivityRepository.getRecruiterJobs(recruiter);

        // Initialize list to store transformed job data
        List<RecruiterJobsDTO> recruiterJobsDtoList = new ArrayList<>();

        // Transform each raw job record into RecruiterJobsDTO objects
        for (IRecruiterJobs rec: recruiterJobsDtos) {
            // Create location object with job location details
            JobLocation loc = new JobLocation(rec.getLocationId(), rec.getCity(), rec.getState(), rec.getCountry());
            // Create company object with company details
            JobCompany comp = new JobCompany(rec.getCompanyId(), rec.getName(), "");
            // Add new DTO with job details, location and company info
            recruiterJobsDtoList.add(new RecruiterJobsDTO(rec.getTotalCandidates(), rec.getJob_post_id(),
                    rec.getJob_title(), loc, comp));
        }

        return recruiterJobsDtoList;
    }



    public JobPostActivity getOne(int id) {

        return jobPostActivityRepository.findById(id).orElseThrow(()->new RuntimeException("Job not found"));
    }










}
