package com.mmed.ws.dto;

import com.mmed.ws.model.User;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsDTO implements UserDetails {

    private User user;

    private static class UserAuthority implements GrantedAuthority {

        @Override
        public @Nullable String getAuthority() {
            return "USER";
        }
    };

    public UserDetailsDTO(User user) {
        this.user = user;
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new UserAuthority());
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return user.getEmail();
    }
}
