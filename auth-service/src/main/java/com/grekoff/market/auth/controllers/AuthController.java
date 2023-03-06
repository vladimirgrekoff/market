package com.grekoff.market.auth.controllers;


import com.grekoff.market.api.JwtRequest;
import com.grekoff.market.api.JwtResponse;
import com.grekoff.market.auth.exceptions.AppError;
import com.grekoff.market.auth.services.UsersService;
import com.grekoff.market.auth.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
//@CrossOrigin("*")
public class AuthController {

    private final UsersService usersService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    // http://localhost:8187/market-auth/api/v1
    @PostMapping("/token")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Имя пользователя или пароль не найдены"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = usersService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    @RequestMapping("/user")
    public Principal user(HttpServletRequest request) {
        System.out.println("вызов user");///////////////////////////////////////////////
        String authToken = request.getHeader("Authorization").substring("Basic ".length()).trim();
        System.out.println("authToken " + authToken);////////////////////////////////////////
        return () ->  new String(Base64.getDecoder().decode(authToken)).split(":")[0];
    }

}
