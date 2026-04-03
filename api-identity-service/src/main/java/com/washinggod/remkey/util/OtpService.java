package com.washinggod.remkey.util;

import com.washinggod.remkey.configuration.properties.OtpConfig;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpService {

    PasswordEncoder passwordEncoder;

    RedisTemplate<String, String> redisTemplate;

    MailService mailService;

    OtpConfig  otpConfig;

    public void generateOtpCode(String email) throws MessagingException {

        String otp = String.format("%06d", (int) (Math.random() * 1000000));

        String otpKey = "otp:" + email;
        String cooldownKey = "otp:cooldown:" + email;
        String requestLimitKey = "otp:request-limit:" + email;

        if (redisTemplate.hasKey(cooldownKey)) {
            throw new AppException(ErrorCode.OTP_NOT_AVAILABLE);
        }

        Long currTimeLimit = otpConfig.getRequestLimit();
        if (redisTemplate.hasKey(requestLimitKey)) {
            currTimeLimit = Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(requestLimitKey)));
            currTimeLimit--;
            if (currTimeLimit <= 0) {
                throw new AppException(ErrorCode.OTP_REQUEST_LIMIT);
            }
        }

        redisTemplate.opsForValue().set(otpKey, passwordEncoder.encode(otp), Duration.ofMinutes(otpConfig.getValidDuration()));
        redisTemplate.opsForValue().set(cooldownKey, "1", Duration.ofMinutes(otpConfig.getRefreshable()));
        redisTemplate.opsForValue().set(requestLimitKey, String.valueOf(currTimeLimit), Duration.ofDays(1));

        String subject = "Account Verification from REMKEY";

        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                	<meta charset="UTF-8">
                </head>
                <body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, sans-serif;">
                
                	<table width="100%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:20px;">
                		<tr>
                			<td align="center">
                
                				<!-- Container -->
                				<table width="500" cellpadding="0" cellspacing="0" style="background:#ffffff; border-radius:10px; overflow:hidden;">
                
                					<!-- Header -->
                					<tr>
                						<td style="background:#4f46e5; padding:20px; text-align:center;">
                							<h1 style="color:#ffffff; margin:0; font-size:24px;">
                								REMKEY 🔐
                							</h1>
                						</td>
                					</tr>
                
                					<!-- Body -->
                					<tr>
                						<td style="padding:30px; color:#333333;">
                
                							<h2 style="margin-top:0;">
                								Account Verification
                							</h2>
                
                							<p style="font-size:15px; line-height:1.5;">
                								Hello,<br><br>
                								We received a request to verify your account. Please use the One-Time Password (OTP) below to complete the process:
                							</p>
                
                							<!-- OTP Box -->
                							<div style="text-align:center; margin:30px 0;">
                								<span style="
                									display:inline-block;
                									font-size:28px;
                									letter-spacing:8px;
                									font-weight:bold;
                									color:#4f46e5;
                									padding:15px 25px;
                									border:2px dashed #4f46e5;
                									border-radius:8px;
                								">
                									OTP
                								</span>
                							</div>
                
                							<p style="font-size:14px; color:#555;">
                								This code will expire in <b>TIME minutes</b>.
                							</p>
                
                							<p style="font-size:14px; color:#555;">
                								If you did not request this, please ignore this email or contact support.
                							</p>
                
                						</td>
                					</tr>
                
                					<!-- Footer -->
                					<tr>
                						<td style="background:#f9fafb; padding:20px; text-align:center; font-size:12px; color:#999;">
                							© 2026 Remkey. All rights reserved.
                						</td>
                					</tr>
                
                				</table>
                
                			</td>
                		</tr>
                	</table>
                
                </body>
                </html>
                """;
        String text = html.replace("OTP", otp).replace("TIME", String.valueOf(otpConfig.getRefreshable()));

        mailService.sendEmail(email, subject, text);

    }


    public boolean verifyOtp(String inputOtp, String email){

        String otpKey = "otp:" + email;
        String storeOtp  = redisTemplate.opsForValue().get(otpKey);

        if(storeOtp == null) return false;

        if( !passwordEncoder.matches(inputOtp, storeOtp)) return false;

        redisTemplate.delete(otpKey);

        return true;
    }
}


