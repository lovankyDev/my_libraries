package com.washinggod.remkey.configuration.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;

@Data
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class OtpConfig {

    @Value("${app.sec.otp-verifier.valid-duration}")
    Long validDuration;

    @Value("${app.sec.otp-verifier.refreshable}")
    Long refreshable;

    @Value("${app.sec.otp-verifier.request-limit}")
    Long requestLimit;
}
