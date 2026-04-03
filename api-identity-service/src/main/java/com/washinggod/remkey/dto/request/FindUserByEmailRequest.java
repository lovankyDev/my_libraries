package com.washinggod.remkey.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindUserByEmailRequest {

    @Email(message = "EMAIL_INVALID")
    String email;

}
