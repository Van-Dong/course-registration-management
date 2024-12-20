package com.dongnv.courseregistrationmanagement.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongnv.courseregistrationmanagement.dto.mapper.UserMapper;
import com.dongnv.courseregistrationmanagement.dto.request.AuthenticationRequest;
import com.dongnv.courseregistrationmanagement.dto.request.UserCreationRequest;
import com.dongnv.courseregistrationmanagement.dto.response.AuthenticationResponse;
import com.dongnv.courseregistrationmanagement.dto.response.UserResponse;
import com.dongnv.courseregistrationmanagement.exception.AppException;
import com.dongnv.courseregistrationmanagement.exception.ErrorCode;
import com.dongnv.courseregistrationmanagement.model.User;
import com.dongnv.courseregistrationmanagement.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @NonFinal
    @Value("${jwt.signer-key}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.issuer}")
    String ISSUER;

    @NonFinal
    @Value("${jwt.expired}")
    int EXPIRED;

    @NonFinal
    @Value("${jwt.expired-with-remember-me}")
    int EXPIRED_WITH_REMEMBER_ME;

    @Transactional
    public UserResponse register(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        User user = userRepository
                .findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.AUTHENTICATION_FAILED));

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        }

        String accessToken = generateToken(user, authenticationRequest.isRememberMe());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .validate(true)
                .build();
    }

    private String generateToken(User user, boolean rememberMe) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(rememberMe ? EXPIRED_WITH_REMEMBER_ME : EXPIRED, ChronoUnit.MINUTES)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        JWSObject jwsObject = new JWSObject(header, jwtClaimsSet.toPayload());

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error when create jwt: ", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        return user.getRole() == null ? "" : user.getRole().toString();
    }
}
