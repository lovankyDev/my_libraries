package com.washinggod.remkey.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EMAIL_ALREADY_EXISTED(1001L, "Email has been registered with another user", HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTED(1001L, "Username already existed", HttpStatus.CONFLICT),
    PERMISSION_NOT_EXIST(1002L, "Permission does not exist", HttpStatus.BAD_REQUEST),
    SAVE_USER_FAILED(1003L, "Save user failed", HttpStatus.CONFLICT),
    USER_NOT_EXIST(1004L, "User not exist", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXIST(1005L, "Role does not exist", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1006L, "Password is incorrect", HttpStatus.BAD_REQUEST),
    OTP_NOT_AVAILABLE(1007L, "Please wait for some seconds", HttpStatus.BAD_REQUEST),
    OTP_REQUEST_LIMIT(1008L, "Your email has been blocked, please try again tomorrow", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1009L, "Your otp code is invalid", HttpStatus.BAD_REQUEST),
    GENERATE_TOKEN_FAILED(1010L, "Generate token failed", HttpStatus.CONFLICT),
    TOKEN_INVALID(1011L, "Token is invalid", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1012L, "Please enter a username with at least 4 characters. Special characters are not allowed.", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1013L, "Password requires at least 8 characters, including uppercase, lowercase, numeric, and special characters.", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1014L, "Incorrect email format ", HttpStatus.BAD_REQUEST),


    UNCATEGORIZED_EXCEPTION(9999L, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR);


    private final Long code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(Long code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
