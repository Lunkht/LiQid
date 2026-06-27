package com.iBiliuminc.liqid.data.mapper;

import com.iBiliuminc.liqid.data.model.UserDto;
import com.iBiliuminc.liqid.domain.model.User;

public class UserMapper {
    public static User toDomain(UserDto dto) {
        return new User(dto.getName(), dto.getInitials(), dto.getPhone(), dto.getPlan());
    }
}
