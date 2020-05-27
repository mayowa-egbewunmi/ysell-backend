package com.example.ysell.controller;

import com.example.ysell.config.UserClient;
import com.example.ysell.model.Meta;
import com.example.ysell.model.Response;
import com.example.ysell.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/mobile")
public class UserController {
    private final UserClient userClient;

    public UserController( UserClient userClient) {

        this.userClient = userClient;
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
}
