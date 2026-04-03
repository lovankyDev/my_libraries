package com.washinggod.remkey.controller;


import com.washinggod.remkey.dto.request.RoleCreationRequest;
import com.washinggod.remkey.dto.request.RoleUpdateRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.RoleResponse;
import com.washinggod.remkey.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/role")
public class RoleController {

    RoleService roleService;

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setBody(roleService.getAll());
        return apiResponse;
    }


    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleCreationRequest request){

        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(roleService.create(request));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete( @PathVariable Long id){

        roleService.delete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Delete role successfully");
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRoleById(@PathVariable Long id){

        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(roleService.getRoleResponseById(id));
        return apiResponse;
    }


    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> update(@PathVariable Long id, @RequestBody RoleUpdateRequest request){

        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(roleService.update(id, request));
        return apiResponse;
    }
}
