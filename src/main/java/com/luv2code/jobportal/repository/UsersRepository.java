package com.luv2code.jobportal.repository;

import com.luv2code.jobportal.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    
    // Finds a user by their email address and returns an Optional containing the user if found, empty if not found
    Optional<Users> findByEmail(String email);
}
