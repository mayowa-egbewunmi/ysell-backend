package com.ysell.jpa.entities.base;

import java.util.UUID;

/**
 * @author created by Tobenna
 * @since 17 January, 2021
 */
public interface NamedEntity {

    UUID getId();

    void setId(UUID id);

    String getName();

    void setName(String name);
}
