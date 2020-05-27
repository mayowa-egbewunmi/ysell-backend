package com.example.ysell.config;

import com.example.ysell.model.Meta;
import com.example.ysell.model.Password;
import com.example.ysell.model.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "passwordClient", url="https://897c0dc8-edcb-4721-8c4e-a1960845d734.mock.pstmn.io/v1/mobile")
public interface PasswordClient {

    @RequestMapping(method= RequestMethod.POST, value = "/password/reset")
    Response<Meta, Password> resetPassword();

    @RequestMapping(method= RequestMethod.POST, value = "/password/change")
    Response<Meta, Password> changePassword();

    @RequestMapping(method= RequestMethod.POST, value = "/sync")
    Response<Meta, Password> syncPassword();
}
