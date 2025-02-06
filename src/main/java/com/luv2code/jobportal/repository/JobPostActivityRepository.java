package com.luv2code.jobportal.repository;


import com.luv2code.jobportal.entity.IRecruiterJobs;
import com.luv2code.jobportal.entity.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, Integer> {

    // This query retrieves jobs posted by a recruiter along with candidate count and location details
    // It joins job_post_activity with job_location, job_company and job_seeker_apply tables
    // Returns: List of jobs with total candidates, job details, location info and company info
    // Parameters: recruiter - ID of the recruiter whose jobs need to be fetched
    @Query(value = " SELECT COUNT(s.user_id) as totalCandidates,j.job_post_id,j.job_title,l.id" +
            " as locationId,l.city,l.state,l.country,c.id as companyId,c.name FROM job_post_activity j " +
            " inner join job_location l " +
            " on j.job_location_id = l.id " +
            " INNER join job_company c  " +
            " on j.job_company_id = c.id " +
            " left join job_seeker_apply s " +
            " on s.job = j.job_post_id " +
            " where j.posted_by_id = :recruiter " +
            " GROUP By j.job_post_id" ,nativeQuery = true)
    List<IRecruiterJobs> getRecruiterJobs(int recruiter);





    @Query(value = "SELECT * FROM job_post_activity j INNER JOIN job_location l on j.job_location_id=l.id WHERE j.job_title LIKE CONCAT('%', :job, '%') " +
            "AND (l.city LIKE CONCAT('%', :location, '%') OR l.country LIKE CONCAT('%', :location, '%') OR l.state LIKE CONCAT('%', :location, '%')) " +
            "AND (j.job_type IN(:type)) " +
            "AND (j.remote IN(:remote))", nativeQuery = true)
    List<JobPostActivity> searchWithoutDate(@Param("job") String job,
                                            @Param("location") String location,
                                            @Param("remote") List<String> remote,
                                            @Param("type") List<String> type);

    @Query(value = "SELECT * FROM job_post_activity j INNER JOIN job_location l on j.job_location_id=l.id WHERE j.job_title LIKE CONCAT('%', :job, '%') " +
            "AND (l.city LIKE CONCAT('%', :location, '%') OR l.country LIKE CONCAT('%', :location, '%') OR l.state LIKE CONCAT('%', :location, '%')) " +
            "AND (j.job_type IN(:type)) " +
            "AND (j.remote IN(:remote)) " +
            "AND (posted_date >= :date)", nativeQuery = true)
    List<JobPostActivity> search(@Param("job") String job,
                                 @Param("location") String location,
                                 @Param("remote") List<String> remote,
                                 @Param("type") List<String> type,
                                 @Param("date") LocalDate searchDate);


}
