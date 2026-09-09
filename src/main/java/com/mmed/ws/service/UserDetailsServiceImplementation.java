package com.mmed.ws.service;

import com.mmed.ws.dto.UserDetailsDTO;
import com.mmed.ws.model.User;
import com.mmed.ws.repository.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImplementation(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException("Error occurs while processing!");
        return new UserDetailsDTO(user);
    }
}
