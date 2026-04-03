package com.washinggod.remkey.dto.request;

import com.washinggod.remkey.validation.PasswordConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, message = "PASSWORD_INVALID")
    @PasswordConstraint(message = "PASSWORD_INVALID")
    String currPassword;


    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, message = "PASSWORD_INVALID")
    @PasswordConstraint(message = "PASSWORD_INVALID")
    String newPassword;


    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, message = "PASSWORD_INVALID")
    @PasswordConstraint(message = "PASSWORD_INVALID")
    String confirmPassword;

}
