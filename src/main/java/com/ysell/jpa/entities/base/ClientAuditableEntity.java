package com.ysell.jpa.entities.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class ClientAuditableEntity extends AuditableEntity {

	private Instant clientCreatedAt;

	private Instant clientUpdatedAt;
}