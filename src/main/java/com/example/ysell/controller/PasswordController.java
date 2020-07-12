package com.example.ysell.controller;

import com.example.ysell.mock.PasswordClient;
import com.example.ysell.response.Meta;
import com.example.ysell.response.Password;
import com.example.ysell.response.Response;
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
