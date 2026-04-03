package com.washinggod.remkey.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private Long code = 1000L;
    private String message;
    private T body;

    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();
}
