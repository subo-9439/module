package com.project.userModule.user.mapper;


import com.project.userModule.user.dto.UserRequestDto;
import com.project.userModule.user.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User UserRequestDtoToUserEntity(UserRequestDto.PostJoin userRequestDto);
}
