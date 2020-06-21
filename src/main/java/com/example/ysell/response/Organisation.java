package com.example.ysell.model;

import java.util.UUID;

public class Organisation {
    private UUID id;
    private String name;
    private String address;
    private String logo;
    private String role;


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLogo() {
        return logo;
    }

    public String getRole() {
        return role;
    }
}
