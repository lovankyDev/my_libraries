package com.washinggod.remkey.service;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.washinggod.remkey.configuration.properties.JwtConfig;
import com.washinggod.remkey.dto.request.AuthenticationRequest;
import com.washinggod.remkey.dto.request.InvalidateTokenRequest;
import com.washinggod.remkey.dto.request.RefreshTokenRequest;
import com.washinggod.remkey.dto.response.AuthenticationResponse;
import com.washinggod.remkey.entity.InvalidToken;
import com.washinggod.remkey.entity.User;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.repository.InvalidTokenRepository;
import com.washinggod.remkey.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    InvalidTokenRepository invalidTokenRepository;

    JwtConfig jwtConfig;

    IntrospectTokenService introspectTokenService;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
            return new AppException(ErrorCode.USER_NOT_EXIST);
        });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        return AuthenticationResponse.builder()
                .token(this.generateToken(user))
                .build();
    }



    public void invalidateToken(InvalidateTokenRequest request) {
        try {
            SignedJWT signedJWT = introspectTokenService.verifyToken(request.getToken(), false);
            InvalidToken token = InvalidToken.builder()
                    .id(signedJWT.getJWTClaimsSet().getJWTID())
                    .expiredTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                    .build();
            invalidTokenRepository.save(token);
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {

        try {

            SignedJWT signedJWT = introspectTokenService.verifyToken(request.getToken(), true);
            String userId = signedJWT.getJWTClaimsSet().getSubject();

            log.info("INFO: getUserId success with userId: {}", userId);
            User user = userRepository.findById(userId).orElseThrow(() -> {
                return new AppException(ErrorCode.USER_NOT_EXIST);
            });

            InvalidToken invalidToken = new InvalidToken();
            invalidToken.setId(signedJWT.getJWTClaimsSet().getJWTID());
            invalidToken.setExpiredTime(signedJWT.getJWTClaimsSet().getExpirationTime());

            return AuthenticationResponse.builder()
                    .token(generateToken(user))
                    .build();
        } catch (AppException | ParseException | JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }

    private String generateToken(User user) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("com.lovanky.remkey")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getId())
                .expirationTime(new Date(Instant.now().plus(jwtConfig.getValidDuration(), ChronoUnit.DAYS).toEpochMilli()))
                .claim("scope", buildRole(user))
                .claim("username", user.getUsername())
                .build();

        JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(jwtClaimsSet.toJSONObject()));

        try {
            jwsObject.sign(new MACSigner(jwtConfig.getSecretKey().getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.GENERATE_TOKEN_FAILED);
        }
    }



    private String buildRole(User user) {

        StringJoiner stringJoiner = new StringJoiner(" ");

        user.getRoles().forEach(role -> {
            stringJoiner.add("ROLE_" + role.getName());
            role.getPermissions().forEach(permission -> {
                stringJoiner.add(permission.getName());
            });
        });

        return stringJoiner.toString();
    }
}
