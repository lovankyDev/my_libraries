package com.washinggod.remkey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidateTokenRequest {

    @NotBlank(message = "TOKEN_INVALID")
    @Size(min = 10, max = 1000, message = "TOKEN_INVALID")
    String token;
}
