package com.mmed.ws.service;

import com.mmed.ws.model.User;
import com.mmed.ws.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceDefault implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public UserServiceDefault(UserRepository repository, PasswordEncoder passwordEncoder,
                              AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }


    @Override
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public String login(String email, String password) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Bad Credentials!");
        }

        String scope = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet
                .builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(now.plus(20, ChronoUnit.MINUTES))
                .issuer("security-monitoring")
                .claim("scope", scope)
                .build();
        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwtClaimsSet))
                .getTokenValue();
    }
}
