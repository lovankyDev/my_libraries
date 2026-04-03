package com.washinggod.remkey.dto.request;

import com.washinggod.remkey.validation.UsernameConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindUserByUsernameRequest {

    @NotBlank(message = "USERNAME_INVALID")
    @Size(min = 4, message = "USERNAME_INVALID")
    @UsernameConstraint(message = "USERNAME_INVALID")
    String username;

}
