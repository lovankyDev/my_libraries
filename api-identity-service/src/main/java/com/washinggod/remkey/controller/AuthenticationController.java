package com.washinggod.remkey.controller;


import com.washinggod.remkey.dto.request.AuthenticationRequest;
import com.washinggod.remkey.dto.request.IntrospectRequest;
import com.washinggod.remkey.dto.request.InvalidateTokenRequest;
import com.washinggod.remkey.dto.request.RefreshTokenRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.AuthenticationResponse;
import com.washinggod.remkey.dto.response.IntrospectResponse;
import com.washinggod.remkey.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping()
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request){
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(authenticationService.authenticate(request));
        return apiResponse;
    }


    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(authenticationService.refreshToken(request));
        return apiResponse;
    }


    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody InvalidateTokenRequest request){
        authenticationService.invalidateToken(request);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Invalidate token successfully");
        return apiResponse;
    }
}
