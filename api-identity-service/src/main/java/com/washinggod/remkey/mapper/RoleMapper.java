package com.washinggod.remkey.mapper;


import com.washinggod.remkey.dto.request.RoleCreationRequest;
import com.washinggod.remkey.dto.request.RoleUpdateRequest;
import com.washinggod.remkey.dto.response.RoleResponse;
import com.washinggod.remkey.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);

    @Mapping(target = "permissions", ignore = true)
    RoleResponse toRoleResponse (Role role);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleUpdateRequest request);

}
