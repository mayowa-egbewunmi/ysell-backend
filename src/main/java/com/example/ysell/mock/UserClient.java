package com.example.ysell.mock;

import com.example.ysell.response.Meta;
import com.example.ysell.response.Response;
import com.example.ysell.response.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "userclient", url="https://897c0dc8-edcb-4721-8c4e-a1960845d734.mock.pstmn.io/v1/mobile")
public interface UserClient {

    @RequestMapping(method = RequestMethod.POST, value = "/account/authenticate")
    Response<Meta, User> authenticateUser();

    @RequestMapping(method= RequestMethod.POST, value = "/account/create")
    Response<Meta, User> createUser();

    @RequestMapping(method= RequestMethod.PUT, value = "/user/update")
    Response<Meta, User> updateUser();
}
