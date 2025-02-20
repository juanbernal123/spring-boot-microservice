package com.example.SpringBootMicroservice.authentication;

import com.example.SpringBootMicroservice.entity.User;
import com.example.SpringBootMicroservice.exception.UserRegistrationException;
import com.example.SpringBootMicroservice.repository.IUserRepository;
import com.example.SpringBootMicroservice.configuration.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws Exception {
        List<String> errors = new ArrayList<>();

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            errors.add("El nombre de usuario ya está en uso.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            errors.add("El email ya está en uso.");
        }

        if (!errors.isEmpty()) {
            throw new UserRegistrationException(errors);
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
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
