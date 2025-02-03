package com.luv2code.jobportal.util;

import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.entity.UsersType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Users user;

    public CustomUserDetails(Users user) {
        this.user = user;
    }

    /**
     * Returns the authorities/roles granted to the user
     * This method is part of the UserDetails interface implementation
     *
     * @return Collection of GrantedAuthority objects representing user's roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Get the user type (role) associated with this user
        UsersType usersType = user.getUserTypeId();
        // Create a list to store the authorities
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // Add the user type name as a SimpleGrantedAuthority
        authorities.add(new SimpleGrantedAuthority(usersType.getUserTypeName()));
        return authorities;
    }


    /**
     * Returns the password of the user
     * This method is part of the UserDetails interface implementation
     *
     * @return String containing the user's password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }


    /**
     * Returns the email address of the user as their username
     * This method is part of the UserDetails interface implementation
     *
     * @return String containing the user's email address which serves as their username
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }



    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
