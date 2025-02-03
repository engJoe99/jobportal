package com.luv2code.jobportal.config;


import com.luv2code.jobportal.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }


private final String[] publicUrl = {
            "/",                  // Homepage
            "/global-search/**",  // Search functionality
            "/register",          // Registration pages
            "/register/**",       // All registration-related URLs
            "/webjars/**",        // External libraries
            "/resources/**",      // Static resources
            "/assets/**",         // Assets like images
            "/css/**",            // CSS files
            "/summernote/**",     // Rich text editor resources
            "/js/**",             // JavaScript files
            "/*.css",             // Root-level CSS files
            "/*.js",              // Root-level JS files
            "/*.js.map",          // Source maps for JS
            "/fonts**",           // Font files
            "/favicon.ico",       // Browser favicon
            "/error",             // Error pages
    };


    /**
     * Configures the security filter chain for HTTP requests
     * This bean defines the authentication and authorization rules
     *
     * @param http The HttpSecurity object to configure
     * @return The configured SecurityFilterChain
     * @throws Exception if there is an error configuring security
     */
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Set the authentication provider that will validate credentials
        http.authenticationProvider(authenticationprovider());

        // Configure authorization rules for HTTP requests
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(publicUrl).permitAll();  // Allow unrestricted access to public URLs defined in publicUrl array
            auth.anyRequest().authenticated();            // Require authentication for all other URLs not in publicUrl
        });
        return http.build();
    }


    /**
     * Creates and configures the authentication provider bean
     * This provider is responsible for validating user credentials during authentication
     *
     * @return A configured DaoAuthenticationProvider that uses BCrypt password encoding
     */
    @Bean
    protected AuthenticationProvider authenticationprovider() {

        // Create a new DAO-based authentication provider
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        // Configure the password encoder to use BCrypt hashing
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        // Set the custom user details service for authentication
        authenticationProvider.setUserDetailsService(customUserDetailsService);

        return authenticationProvider;
    }



    /**
     * Creates a password encoder bean that uses BCrypt hashing algorithm
     * BCrypt automatically handles salt generation and storage
     *
     * @return A BCryptPasswordEncoder instance for secure password hashing
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
