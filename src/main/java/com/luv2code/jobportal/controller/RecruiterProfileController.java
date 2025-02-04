package com.luv2code.jobportal.controller;


import com.luv2code.jobportal.entity.RecruiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.UsersRepository;
import com.luv2code.jobportal.services.RecruiterProfileService;
import com.luv2code.jobportal.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UsersRepository usersRepository;
    private final RecruiterProfileService recruiterProfileService;


    @Autowired
    public RecruiterProfileController(UsersRepository usersRepository,
                                      RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileService = recruiterProfileService;
    }


    /**
     * Handles GET request for displaying recruiter profile page
     * @param model Spring MVC Model object to pass data to view
     * @return String view name for recruiter profile page
     */
    @GetMapping("/")
        public String recruiterProfile(Model model) {

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated (not anonymous)
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            // Get username (email) of logged in user
            String currentUserName = authentication.getName();
            // Find user by email or throw exception if not found
            Users users = usersRepository.findByEmail(currentUserName).orElseThrow(() ->
                    new UsernameNotFoundException("Couldn't found user!!"));

            // Get recruiter profile for the user
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());
            // If profile exists, add it to model for view rendering
            recruiterProfile.ifPresent(profile -> model.addAttribute("profile", profile));
        }
        return "recruiter_profile";
    }



    /**
     * Handles the POST request to add a new recruiter profile with profile photo upload functionality.
     * This method processes the form submission for creating a new recruiter profile, including:
     * - Authenticating the current user and linking their details to the profile
     * - Handling the profile photo upload if provided
     * - Saving the profile data to the database
     * - Storing the uploaded photo in the file system
     *
     * @param recruiterProfile The recruiter profile object containing all the profile details to be saved
     * @param multipartFile The profile photo file uploaded by user through the form
     * @param model The Spring MVC model for passing data to the view
     * @return Redirects to dashboard page after successfully creating the profile
     */
    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile,
                         @RequestParam("image") MultipartFile multipartFile, Model model) {

        // Get the currently authenticated user from Spring Security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            // Find user by email in database, throw exception if user doesn't exist
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new
                    UsernameNotFoundException("Couldn't Found The user"));

            // Associate the authenticated user's details with the recruiter profile
            recruiterProfile.setUserId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }

        // Add the recruiter profile object to model for displaying in view if needed
        model.addAttribute("profile", recruiterProfile);

        // Process the uploaded profile photo if one was provided
        String fileName = "";
        if (!multipartFile.getOriginalFilename().equals("")) {
            // Clean the filename to prevent any path traversal security issues
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileName);
        }

        // Persist the recruiter profile data to the database
        RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile);

        // Construct the directory path where profile photos will be stored
        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();
        try {
            // Save the uploaded photo file to the specified directory on the server
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (IOException ex) {
            // Log any file handling errors that occur during upload
            ex.printStackTrace();
        }

        // Redirect to the dashboard page after successful profile creation
        return "redirect:/dashboard";
    }


}
