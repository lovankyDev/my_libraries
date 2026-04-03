package com.washinggod.remkey.mapper;

import com.washinggod.remkey.dto.request.UserCreationRequest;
import com.washinggod.remkey.dto.request.UserUpdateRequest;
import com.washinggod.remkey.dto.response.UserResponse;
import com.washinggod.remkey.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
