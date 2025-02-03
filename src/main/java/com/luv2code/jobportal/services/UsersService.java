package com.luv2code.jobportal.services;


import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void addNew(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        usersRepository.save(users);
    }

    public Optional<Users> getUserByEmail(String email) {
        System.out.println("==> Searching for email: " + email);
        Optional<Users> result = usersRepository.findByEmail(email);
        System.out.println("==> Found User: " + result.isPresent());
        return result;
    }

}
