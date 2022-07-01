package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.LoginRequestDto;
import com.expense.ExpenseTracker.dto.LoginResponseDto;
import com.expense.ExpenseTracker.dto.UserRequestDto;
import com.expense.ExpenseTracker.dto.UserResponseDto;
import com.expense.ExpenseTracker.model.User;
import com.expense.ExpenseTracker.security.JwtTokenUtil;
import com.expense.ExpenseTracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/auth")
public class AuthController {

    private  final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final ModelMapper modelMapper = new ModelMapper();

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("register")
    @ResponseStatus(value = HttpStatus.OK)
    public UserResponseDto register(@RequestBody @Valid UserRequestDto request) {
        User savedUser = userService.register(request.getUsername(), request.getPassword());
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto request) {
        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(), request.getPassword()
                        )
                );

        User user = (User) authenticate.getPrincipal();

        return new LoginResponseDto(jwtTokenUtil.generateToken(user));
    }
}
