package com.washinggod.remkey.mapper;


import com.washinggod.remkey.dto.request.PermissionCreationRequest;
import com.washinggod.remkey.dto.response.PermissionResponse;
import com.washinggod.remkey.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionResponse( Permission permission);

    Permission toPermission(PermissionCreationRequest request);
}
