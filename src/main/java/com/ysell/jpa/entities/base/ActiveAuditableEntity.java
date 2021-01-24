package com.ysell.jpa.entities.base;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author created by Tobenna
 * @since 10 January, 2021
 */
@MappedSuperclass
@Getter
@Setter
@Where(clause = "is_active=1")
public class ActiveAuditableEntity extends AuditableEntity {

    @Column(name="is_active", nullable = false, columnDefinition = "tinyint(1) default 1")
    protected boolean active = true;
}
