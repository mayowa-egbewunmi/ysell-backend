package com.ysell.jpa.entities.base;

import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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
public abstract class AuditableEntity<TAuditorId> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
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
	
	@Override
	public boolean equals(Object obj) {
		if(id == 0)
			return super.equals(obj);
		else if(obj == null || obj.getClass() != getClass())
			return false;
		
		@SuppressWarnings("unchecked")
		boolean isEqual = id == ((AuditableEntity<TAuditorId>)obj).id;
		
		return isEqual;
	}

	@Override
	public int hashCode() {
		return id == 0 ? super.hashCode() : new Long(id).intValue();
	}
}