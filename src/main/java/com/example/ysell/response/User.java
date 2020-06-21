package com.example.ysell.model;

import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String token;

    private List<Organisation> organisations;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public List<Organisation> getOrganisations() {
        return organisations;
    }


}
