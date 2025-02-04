package com.luv2code.jobportal.services;


import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.RecruiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.JobSeekerProfileRepository;
import com.luv2code.jobportal.repository.RecruiterProfileRepository;
import com.luv2code.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository,
                        JobSeekerProfileRepository jobSeekerProfileRepository,
                        RecruiterProfileRepository recruiterProfileRepository,
                        PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // Method to add a new user to the system
    public Users addNew(Users users) {
        // Set user as active and set registration date to current time
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));

        // Encode the password before saving
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        // Save the user to the database
        Users savedUser = usersRepository.save(users);

        // Get the user type ID to determine if recruiter or job seeker
        int userTypeId = users.getUserTypeId().getUserTypeId();

        // If userTypeId is 1, create recruiter profile
        if (userTypeId == 1) {
            recruiterProfileRepository.save(new RecruiterProfile(savedUser));
        }
        // Otherwise create job seeker profile
        else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
        }

        // Return the saved user object
        return savedUser;
    }


    /**
     * Gets the profile of the currently authenticated user
     * @return RecruiterProfile or JobSeekerProfile object if user is authenticated, null otherwise
     */
    public Object getCurrentUserProfile() {

        // Get the current authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated (not anonymous)
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            // Get username (email) from authentication
            String username = authentication.getName();
            // Find user by email or throw exception if not found
            Users users = usersRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Could not found " + "user"));
            int userId = users.getUserId();

            // Check if user has Recruiter role
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                // Return recruiter profile for the user ID
                RecruiterProfile recruiterProfile = recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
                return recruiterProfile;
            } else {
                // Return job seeker profile for the user ID
                JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
                return jobSeekerProfile;
            }
        }

        // Return null if user is not authenticated
        return null;
    }




    public Optional<Users> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

}
