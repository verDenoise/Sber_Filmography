package com.example.filmography.rest.controller;

import com.example.filmography.dto.UserDto;
import com.example.filmography.mapper.UserMapper;
import com.example.filmography.model.User;
import com.example.filmography.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rest/user")

public class UserController  extends GenericController<User, UserDto>{

    private final UserService service;

    protected UserController(UserService service, UserMapper mapper) {
        super(service, mapper);
        this.service = service;
    }

}
