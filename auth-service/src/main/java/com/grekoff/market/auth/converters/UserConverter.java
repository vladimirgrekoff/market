package com.grekoff.market.auth.converters;


import com.grekoff.market.api.auth.UserDto;
import com.grekoff.market.auth.entities.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter {
    private final RoleConverter roleConverter;

    public UserConverter(RoleConverter roleConverter) {
        this.roleConverter = roleConverter;
    }

    public User dtoToEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getUsername(), userDto.getPassword(), userDto.getFirstname(), userDto.getLastname(), userDto.getEmail(), userDto.getRoles().stream().map(roleConverter::dtoToEntity).collect(Collectors.toList()));
    }

    public UserDto entityToDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), "PROTECTED", user.getFirstname(), user.getLastname(), user.getEmail(), user.getRoles().stream().map(roleConverter::entityToDto).collect(Collectors.toList()));
    }
}
