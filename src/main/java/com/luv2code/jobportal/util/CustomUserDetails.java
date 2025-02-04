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

    private Users user;

    public CustomUserDetails(Users user) {
        this.user = user;
    }

    /**
     * Returns the authorities/roles granted to the user
     *
     * @return Collection of GrantedAuthority objects representing user's roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Get the user type (role) from the user object
        UsersType usersType = user.getUserTypeId();
        // Create list to store authorities
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // Add the user type name as a granted authority
        authorities.add(new SimpleGrantedAuthority(usersType.getUserTypeName()));
        return authorities;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

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
