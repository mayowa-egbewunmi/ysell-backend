package com.ysell.jpa.entities.base;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditableClientEntity<TAuditorId> {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	
	@CreatedBy
    protected TAuditorId createdBy;

	@CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

	@LastModifiedBy
    protected TAuditorId updatedBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
    protected Date updatedAt;
	
    private Date clientCreatedAt;

    private Date clientUpdatedAt; 
	
	@PrePersist
	public void validateUuid() {
		if(id == null) 
			id = UUID.randomUUID();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(id == null)
			return super.equals(obj);
		else if(obj == null || obj.getClass() != getClass())
			return false;

		@SuppressWarnings("unchecked")
		boolean isEqual = id == ((AuditableClientEntity<TAuditorId>)obj).id;
		
		return isEqual;
	}

	@Override
	public int hashCode() {
		if(id == null)
			return super.hashCode();
		
		long bitSum = id.getMostSignificantBits() + id.getLeastSignificantBits();
		return new Long(bitSum).intValue();
	}
}
