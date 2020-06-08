package com.example.ysell.controller;

import com.example.ysell.config.PasswordClient;
import com.example.ysell.model.Meta;
import com.example.ysell.model.Password;
import com.example.ysell.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/mobile")
public class PasswordController {
    private final PasswordClient passwordClient;

    public PasswordController( PasswordClient passwordClient) {

        this.passwordClient = passwordClient;
    }

    @PostMapping("/password/reset")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Meta, Password> reset() {

        return passwordClient.resetPassword();

    }

    @PostMapping("/password/change")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Meta, Password> change() {

        return passwordClient.changePassword();

    }

    @PostMapping("/sync")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Meta, Password> sync() {

        return passwordClient.sync();

    }



}
