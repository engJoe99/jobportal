package com.luv2code.jobportal.services;


import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.RecruiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.JobSeekerProfileRepository;
import com.luv2code.jobportal.repository.RecruiterProfileRepository;
import com.luv2code.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository,
                        JobSeekerProfileRepository jobSeekerProfileRepository,
                        RecruiterProfileRepository recruiterProfileRepository) {
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }




    /**
     * Adds a new user and creates the corresponding profile based on a user type
     * @param users The user object to be added
     * @return The saved user object
     */
    public Users addNew(Users users) {

        // Set user as active by default
        users.setActive(true);

        // Set registration date to current timestamp
        users.setRegistrationDate(new Date(System.currentTimeMillis()));

        // Save the user to database
        Users savedUser = usersRepository.save(users);
        System.out.println("===>>> savedUser" + savedUser);

        // Get user type ID to determine profile type
        int userTypeId = users.getUserTypeId().getUserTypeId();

        // User type 1 is recruiter - create recruiter profile
        if(userTypeId == 1) {
            recruiterProfileRepository.save(new RecruiterProfile(savedUser));

        // Other user types are job seekers - create job seeker profile
        } else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
        }

        return savedUser;
    }



    public Optional<Users> getUserByEmail(String email) {
        System.out.println("==> Searching for email: " + email);
        Optional<Users> result = usersRepository.findByEmail(email);
        System.out.println("==> Found User: " + result.isPresent());
        return result;
    }

}
