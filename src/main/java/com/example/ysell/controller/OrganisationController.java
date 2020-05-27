package com.example.ysell.controller;

import com.example.ysell.config.OrganisationClient;
import com.example.ysell.model.Meta;
import com.example.ysell.model.Organisation;
import com.example.ysell.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/mobile/organisation")
public class OrganisationController {
    private final OrganisationClient organisationClient;

    public OrganisationController( OrganisationClient organisationClient) {

        this.organisationClient = organisationClient;
    }

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Meta, List<Organisation>> create() {

        return organisationClient.createOrganisation();

    }
}
