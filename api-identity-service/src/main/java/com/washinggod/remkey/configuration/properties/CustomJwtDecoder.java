package com.washinggod.remkey.configuration.properties;

import com.washinggod.remkey.dto.request.IntrospectRequest;
import com.washinggod.remkey.service.IntrospectTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Autowired
    private IntrospectTokenService introspectTokenService;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public Jwt decode(String token) throws JwtException {

        boolean response = introspectTokenService.introspect(IntrospectRequest.builder().token(token).build()).isValid();

        if (!response)
            throw new JwtException("Token is invalid");

        if (Objects.isNull(nimbusJwtDecoder)) {

            SecretKeySpec secretKeySpec = new SecretKeySpec(jwtConfig.getSecretKey().getBytes(), "HS512");

            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();

        }

        return nimbusJwtDecoder.decode(token);
    }
}
