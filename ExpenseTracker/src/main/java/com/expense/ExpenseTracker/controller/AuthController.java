package com.expense.ExpenseTracker.controller;


import com.expense.ExpenseTracker.dto.LoginRequestDto;
import com.expense.ExpenseTracker.dto.LoginResponseDto;
import com.expense.ExpenseTracker.model.User;
<<<<<<< HEAD
import com.expense.ExpenseTracker.security.JwtTokenUtil;
=======
>>>>>>> b24a87f46c2cd0570f7a2fa2b83babe4d96afb9b
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
<<<<<<< HEAD
@RequestMapping(path = "api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
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
=======
@RequestMapping(path = "api/v1/public")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    //private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper = new ModelMapper();

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        //this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );

            User user = (User) authenticate.getPrincipal();

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION//,
                            //jwtTokenUtil.generateAccessToken(user)
                    )
                    .body(modelMapper.map(user, LoginResponseDto.class));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
>>>>>>> b24a87f46c2cd0570f7a2fa2b83babe4d96afb9b
    }
}
