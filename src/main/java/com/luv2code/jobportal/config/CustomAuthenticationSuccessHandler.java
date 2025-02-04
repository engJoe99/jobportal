package com.luv2code.jobportal.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {



    /**
     * Handles successful authentication by determining user role and redirecting to appropriate page
     * @param request The HTTP request
     * @param response The HTTP response
     * @param authentication The authentication object containing user details and roles
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Extract user details from authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        System.out.println("The username " + username + " is logged in.");

        // Check if user has a Job Seeker role
        boolean hasJobSeekerRole = authentication.getAuthorities().stream().anyMatch(r->
                r.getAuthority().equals("Job Seeker"));

        // Check if user has a Recruiter role
        boolean hasRecruiterRole = authentication.getAuthorities().stream().anyMatch(r->
                r.getAuthority().equals("Recruiter"));

        // Redirect to dashboard if user has either Job Seeker or Recruiter role
        if (hasRecruiterRole || hasJobSeekerRole) {
            response.sendRedirect("/dashboard");
        }
    }




}
