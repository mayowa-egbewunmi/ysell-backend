package com.example.ysell.mock;

import com.example.ysell.response.Code;
import com.example.ysell.response.Meta;
import com.example.ysell.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "codeClient", url="https://897c0dc8-edcb-4721-8c4e-a1960845d734.mock.pstmn.io/v1/mobile/code")
public interface CodeClient {

    @RequestMapping(method= RequestMethod.POST, value = "/initiate")
    Response<Meta, Code> initiateCode();

    @RequestMapping(method= RequestMethod.POST, value = "/verify")
    Response<Meta, Code> verifyCode();
}
