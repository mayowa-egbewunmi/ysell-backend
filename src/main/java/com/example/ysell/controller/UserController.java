package com.example.ysell.controller;

import com.example.ysell.Entity.UserEntity;
import com.example.ysell.mock.UserClient;
import com.example.ysell.response.Meta;
import com.example.ysell.response.Response;
import com.example.ysell.response.User;
import com.example.ysell.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/mobile")
public class UserController {
	
    private final UserClient userClient;
    private final UserService userService;

    public UserController( UserClient userClient, UserService userService) {

        this.userClient = userClient;
        this.userService = userService;
    }

    @PostMapping("/account/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Meta, User> create() {

        return userClient.createUser();

    }

    @PostMapping("/account/authenticate")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<Meta, User> authenticate(){
        return userClient.authenticateUser();
    }

    @PutMapping("/user/update")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<Meta, User> update(){
        return userClient.updateUser();
    }
    
    @GetMapping("/user/get/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserEntity getUser(@PathVariable(value = "id") Long id) {
    	UserEntity user = new UserEntity();//userService.getUserByEmail(id);
    	return user;
    }
}
