package com.example.SpringBootMicroservice.authentication;

import com.example.SpringBootMicroservice.entity.User;
import com.example.SpringBootMicroservice.repository.IUserRepository;
import com.example.SpringBootMicroservice.configuration.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
