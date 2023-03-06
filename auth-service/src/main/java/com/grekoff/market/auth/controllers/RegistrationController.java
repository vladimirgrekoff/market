package com.grekoff.market.auth.controllers;

import com.grekoff.market.api.UserRegistrationDto;
import com.grekoff.market.auth.entities.User;
import com.grekoff.market.auth.exceptions.AppError;
import com.grekoff.market.auth.exceptions.EmailExistsException;
import com.grekoff.market.auth.exceptions.UserAlreadyExistException;
import com.grekoff.market.auth.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    private final UsersService usersService;



    @PostMapping
    public ResponseEntity<?> registerUserAccount(@RequestBody UserRegistrationDto userRegistrationDto) throws UserAlreadyExistException, EmailExistsException {
        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }

        User registered = usersService.registerNewUserAccount(userRegistrationDto);
        if (registered==null){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        userRegistrationDto.setUsername(null);
        userRegistrationDto.setEmail(null);
        userRegistrationDto.setFirstname(null);
        userRegistrationDto.setLastname(null);
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setConfirmPassword(null);
        return  ResponseEntity.ok(userRegistrationDto);
    }
}
