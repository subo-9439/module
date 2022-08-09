package com.project.userModule.user.mapper;

import com.project.userModule.user.dto.UserRequestDto;
import com.project.userModule.user.dto.UserResponseDto;
import com.project.userModule.user.entity.Users;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    Users UserRequestDtoToUserEntity(UserRequestDto.PostJoin userRequestDto);
}
