package com.ysell.jpa.entities.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author created by Tobenna
 * @since 10 January, 2021
 */
@MappedSuperclass
@Getter
@Setter
public class ActiveAuditableEntity extends AuditableEntity {

    @Column(name="is_active", nullable = false)
    protected boolean active = true;
}
