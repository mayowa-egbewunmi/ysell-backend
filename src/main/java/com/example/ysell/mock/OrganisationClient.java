package com.example.ysell.mock;

import com.example.ysell.response.Meta;
import com.example.ysell.response.Organisation;
import com.example.ysell.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(value = "organisationClient", url="https://897c0dc8-edcb-4721-8c4e-a1960845d734.mock.pstmn.io/v1/mobile/organisation")
public interface OrganisationClient {

    @RequestMapping(method= RequestMethod.POST, value = "/create")
    Response<Meta, List<Organisation>> createOrganisation();

}
