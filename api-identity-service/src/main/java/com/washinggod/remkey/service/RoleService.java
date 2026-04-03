package com.washinggod.remkey.service;


import com.washinggod.remkey.dto.request.RoleCreationRequest;
import com.washinggod.remkey.dto.request.RoleUpdateRequest;
import com.washinggod.remkey.dto.response.RoleResponse;
import com.washinggod.remkey.entity.Permission;
import com.washinggod.remkey.entity.Role;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.RoleMapper;
import com.washinggod.remkey.repository.PermissionRepository;
import com.washinggod.remkey.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class RoleService {

    RoleRepository roleRepository;

    RoleMapper roleMapper;

    PermissionRepository permissionRepository;


    public RoleResponse create(RoleCreationRequest request) {

        Role role = roleMapper.toRole(request);

        this.updatePermissionOfRole(role, request.getPermissions());

        return generateRoleResponse(roleRepository.save(role));

    }

    public RoleResponse update(Long id, RoleUpdateRequest request) {

        Role role = this.getRoleById(id);

        roleMapper.updateRole(role, request);

        this.updatePermissionOfRole(role, request.getPermissions());

        return generateRoleResponse(roleRepository.save(role));
    }

    public void delete( Long id){

        Role role = this.getRoleById(id);
        roleRepository.delete(role);
    }

    private void updatePermissionOfRole(Role role, List<Long> permissionIds) {

        if (!permissionIds.isEmpty()) {
            Set<Permission> permissions =
                    new HashSet<>(permissionRepository.findAllById(permissionIds));
            if (!permissions.isEmpty()) {
                role.setPermissions(permissions);
            }
        }
    }


    public RoleResponse getRoleResponseById(Long id) {
        return generateRoleResponse(this.getRoleById(id));
    }

    public RoleResponse getRoleResponseByName(String name) {
        return generateRoleResponse(this.getRoleByName(name));
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(this::generateRoleResponse).toList();
    }

    private Role getRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> {
            return new AppException(ErrorCode.ROLE_NOT_EXIST);
        });
    }

    private Role getRoleById(Long id) {

        return roleRepository.findById(id).orElseThrow(() -> {
            return new AppException(ErrorCode.ROLE_NOT_EXIST);
        });
    }

    private RoleResponse generateRoleResponse(Role role) {

        RoleResponse roleResponse = roleMapper.toRoleResponse(role);

        if (role.getPermissions() != null) {
            List<String> permissionResponse = role.getPermissions().stream()
                    .map(Permission::getName)
                    .toList();
            roleResponse.setPermissions(permissionResponse);
        }
        return roleResponse;
    }
}
