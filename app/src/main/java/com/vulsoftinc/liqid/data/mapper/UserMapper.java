package com.vulsoftinc.liqid.data.mapper;

import com.vulsoftinc.liqid.data.model.UserDto;
import com.vulsoftinc.liqid.domain.model.User;

public class UserMapper {
    public static User toDomain(UserDto dto) {
        return new User(dto.getName(), dto.getInitials(), dto.getPhone(), dto.getPlan());
    }
}
