package com.washinggod.remkey.service;

import com.washinggod.remkey.dto.request.PermissionCreationRequest;
import com.washinggod.remkey.dto.response.PermissionResponse;
import com.washinggod.remkey.entity.Permission;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.PermissionMapper;
import com.washinggod.remkey.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class PermissionService {

    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionCreationRequest request) {

        Permission permission = permissionMapper.toPermission(request);

        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public List<PermissionResponse> getAll(){
        return permissionRepository.findAll().stream()
                .map(this.permissionMapper::toPermissionResponse)
                .toList();
    }

    public void delete(Long id){

        permissionRepository.delete(this.getPermission(id));

    }

    private Permission getPermission(Long id){
        return permissionRepository.findById(id).orElseThrow(() -> {
            return new AppException(ErrorCode.PERMISSION_NOT_EXIST);
        });
    }

}
