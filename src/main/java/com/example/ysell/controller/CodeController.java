package com.example.ysell.controller;

import com.example.ysell.config.CodeClient;
import com.example.ysell.model.Code;
import com.example.ysell.model.Meta;
import com.example.ysell.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/v1/mobile/code")
    public class CodeController {
        private final CodeClient codeClient;

        public CodeController( CodeClient codeClient) {

            this.codeClient = codeClient;
        }

        @PostMapping("/initiate")
        @ResponseBody
        @ResponseStatus(HttpStatus.CREATED)
        public Response<Meta, Code> initiate() {
            return codeClient.initiateCode();
        }

        @PostMapping("/verify")
        @ResponseBody
        @ResponseStatus(HttpStatus.CREATED)
        public Response<Meta, Code> verify() {
            return codeClient.verifyCode();
        }
    }
