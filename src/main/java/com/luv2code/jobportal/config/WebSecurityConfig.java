package com.luv2code.jobportal.config;

import com.luv2code.jobportal.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    private final String[] publicUrl = {"/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**", "/favicon.ico", "/resources/**", "/error"};

    @Bean
    // Configure the security filter chain for HTTP requests
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Set the authentication provider
        http.authenticationProvider(authenticationProvider());

        // Configure authorization rules
        http.authorizeHttpRequests(auth -> {
            // Allow public access to URLs defined in publicUrl array
            auth.requestMatchers(publicUrl).permitAll();
            // Require authentication for all other requests
            auth.anyRequest().authenticated();
        });

        // Configure form login, logout, CORS and CSRF settings
        http.formLogin(form->form.loginPage("/login").permitAll()
                        .successHandler(customAuthenticationSuccessHandler))
                .logout(logout-> {
                    // Set logout URL and redirect URL after logout
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");
                }).cors(Customizer.withDefaults()) // Enable CORS with default settings
                .csrf(csrf->csrf.disable()); // Disable CSRF protection

        // Build and return the security filter chain
        return http.build();
    }




    /**
     * Creates and configures the authentication provider bean
     * This provider handles the authentication process using DAOs
     * @return Configured DaoAuthenticationProvider instance
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Create new DAO-based authentication provider
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // Set password encoder for secure password handling
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        // Set custom user details service for user lookup
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        return authenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}