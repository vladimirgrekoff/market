package com.grekoff.market.auth.controllers;

import com.grekoff.market.auth.converters.UserConverter;
import com.grekoff.market.api.UserDto;
import com.grekoff.market.auth.entities.User;
import com.grekoff.market.auth.exceptions.ResourceNotFoundException;
import com.grekoff.market.auth.services.UsersService;
import com.grekoff.market.auth.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    @Autowired
    private UserConverter userConverter;

    private final UserValidator userValidator;


    // GET http://localhost:8189/market-auth/api/v1/users

    @GetMapping
    public List<UserDto> getAllUsers() {
        return usersService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User user = usersService.findById(id).orElseThrow(()-> new ResourceNotFoundException("Пользователь отсутствует в списке, id: " + id));
        UserDto userDto = userConverter.entityToDto(user);
        userDto.setPassword("PROTECTED");
        return userDto;
    }

    @PostMapping
    public UserDto addNewUser(@RequestBody UserDto userDto) {
        userValidator.validate(userDto);
        User user = userConverter.dtoToEntity(userDto);
        user = usersService.save(user);
        return userConverter.entityToDto(user);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto) {
        userValidator.validate(userDto);
        User user = usersService.update(userDto);
        return userConverter.entityToDto(user);
    }


    @DeleteMapping ("/{id}")
    public void deleteById(@PathVariable Long id){
        usersService.deleteById(id);
    }

}
