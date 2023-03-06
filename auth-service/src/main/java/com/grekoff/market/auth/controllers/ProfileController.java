package com.grekoff.market.auth.controllers;

import com.grekoff.market.api.RoleDto;
import com.grekoff.market.api.UserDto;
import com.grekoff.market.auth.converters.RoleConverter;
import com.grekoff.market.auth.entities.Role;
import com.grekoff.market.auth.entities.User;
import com.grekoff.market.auth.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
//@CrossOrigin("*")
public class ProfileController {
    private final UsersService usersService;
    private final RoleConverter roleConverter;

    // http://localhost:8187/market-auth/api/v1/profile
    @GetMapping("/get")
    public UserDto getCurrentUserInfo(@RequestHeader String username) {
         User user = usersService.findByUsername(username).get();
        return new UserDto(user.getId(), user.getUsername(), "PROTECTED", user.getFirstname(), user.getLastname(), user.getEmail(), user.getRoles().stream().map(roleConverter::entityToDto).collect(Collectors.toList()));
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getCurrentRoles(@RequestHeader String username) {
        User user = usersService.findByUsername(username).get();
//        return ResponseEntity.ok(new RoleDto("", user.getUsername(), "", "", "", user.getRoles().stream().map(roleConverter::entityToDto).collect(Collectors.toList())));
        List<RoleDto> roles = user.getRoles().stream().map(roleConverter::entityToDto).collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }
}
