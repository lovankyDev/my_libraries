package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.*;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.UserResponse;
import com.washinggod.remkey.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user")
public class UserController {


    UserService userService;


    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid UserCreationRequest request){

        userService.create(request);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Please check your email");
        return apiResponse;
    }

    @PostMapping("/create/verify")
    public ApiResponse<UserResponse> verifyUserCreationRequest(@RequestBody @Valid VerifyUserCreationRequest request){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(userService.verifyUserCreationRequest(request));
        return apiResponse;
    }


    @GetMapping
    public ApiResponse<List<UserResponse>> getAll(){
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setBody(userService.getAll());
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable String id, @RequestBody @Valid  UserUpdateRequest request){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(userService.update(id, request));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id){
        userService.delete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Delete user successfully");
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String id){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(userService.getUserResponseById(id));
        return apiResponse;
    }

    @GetMapping("/find-by-username")
    public ApiResponse<UserResponse> getUserByUsername(@RequestBody @Valid  FindUserByUsernameRequest request){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(userService.getUserResponseByUsername(request));
        return apiResponse;
    }


    @GetMapping("/find-by-email")
    public ApiResponse<UserResponse> getUserByEmail(@RequestBody @Valid FindUserByEmailRequest request){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(userService.getUserResponseByEmail(request));
        return apiResponse;
    }

    @PostMapping("/{id}/change-password")
    public ApiResponse<UserResponse> changePassword(@PathVariable String id,@RequestBody @Valid  ChangePasswordRequest request){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(userService.changePassword(id, request));
        return apiResponse;
    }


    @PostMapping("/{id}/provide-admin-role")
    public ApiResponse<UserResponse> provideAdmin(@PathVariable String id){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(userService.provideAdminRole(id));
        return apiResponse;
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo(){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(userService.getMyInfo());
        return apiResponse;
    }

}
