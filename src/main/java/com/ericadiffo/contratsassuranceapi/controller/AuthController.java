package com.ericadiffo.contratsassuranceapi.controller;

import com.ericadiffo.contratsassuranceapi.dto.AuthResponseDTO;
import com.ericadiffo.contratsassuranceapi.dto.LoginRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.RegisterRequestDTO;
import com.ericadiffo.contratsassuranceapi.models.Utilisateur;
import com.ericadiffo.contratsassuranceapi.repository.UtilisateurRepository;
import com.ericadiffo.contratsassuranceapi.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        if (utilisateurRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .email(requestDTO.getEmail())
                .motDePasse(passwordEncoder.encode(requestDTO.getMotDePasse()))
                .role(requestDTO.getRole())
                .build();

        utilisateurRepository.save(utilisateur);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .authorities("ROLE_" + utilisateur.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(token)
                .email(utilisateur.getEmail())
                .role(utilisateur.getRole().name())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Identifiants invalides"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .authorities("ROLE_" + utilisateur.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(token)
                .email(utilisateur.getEmail())
                .role(utilisateur.getRole().name())
                .build());
    }
}