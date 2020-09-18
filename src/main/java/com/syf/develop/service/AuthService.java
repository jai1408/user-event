package com.syf.develop.service;


import com.syf.develop.entity.User;
import com.syf.develop.entity.VerificationToken;
import com.syf.develop.exception.UserEventError;
import com.syf.develop.exception.UserEventException;
import com.syf.develop.mapper.UserMapper;
import com.syf.develop.model.*;
import com.syf.develop.repository.UserRepository;
import com.syf.develop.repository.VerificationTokenRepository;
import com.syf.develop.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {


    private final VerificationTokenRepository verficationTokenRepository;
    private final MailService mailService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    public Optional<UserDTO> signup(UserDTO request) throws UserEventException {

        if (!(userRepository.findByEmail(request.getEmail()).isPresent())) {
            generateUniqueUserId(request);
            encryptUserPassword(request);
            User user = userMapper.userDtoToUser(request);
            User savedUser = userRepository.save(user);
            log.trace("saved user with id[{}] to repository", savedUser.getId());
            String token = generateVerificationToken(user);
            mailService.sendMail(new NotificationEmail("Please activate your account", user.getEmail(),
                    "Thank you for signing up to Spring Reddit, "
                            + "please click on the below url to activate your account : "
                            + "http://localhost:8080/api/auth/accountVerification/" + token));
            return Optional.of(userMapper.userToUserDTO(savedUser));
        } else {
            throw new UserEventException("User already registered with email "+request.getEmail(), UserEventError.USER_ALREADY_REGISTERED);
        }
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verficationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) throws UserEventException {
        Optional<VerificationToken> verificationToken = verficationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new UserEventException("Invalid Token", UserEventError.INVALID_TOKEN));
        fetchUserAndEnable(verificationToken.get());
    }


    private void fetchUserAndEnable(VerificationToken verificationToken) throws UserEventException {
        String userId = verificationToken.getUser().getUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserEventException("User Not Found with email " + userId,UserEventError.USER_NOT_FOUND));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) throws UserEventException {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userRepository.findByName(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    private void generateUniqueUserId(UserDTO request) {
        request.setUserId(UUID.randomUUID().toString());
    }

    private void encryptUserPassword(UserDTO request) {
        request.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws UserEventException {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

}
