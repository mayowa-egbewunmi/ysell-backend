package com.ysell.jpa.entities.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditableEntity {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	
	@CreatedBy
	@Column(columnDefinition = "BINARY(16)")
	private UUID createdBy;

	@CreatedDate
	private Timestamp createdAt;

	@LastModifiedBy
	@Column(columnDefinition = "BINARY(16)")
	private UUID updatedBy;

	@LastModifiedDate
	private Timestamp updatedAt;

	@Version
	private int version;
	
    private Timestamp clientCreatedAt;

    private Timestamp clientUpdatedAt;


	@PrePersist
	public void validateUuid() {
		if(id == null) 
			id = UUID.randomUUID();
	}


	public String getTableName() {
		return getTableName(getClass());
	}


	public static <T extends AuditableEntity> String getTableName(Class<T> entityClass) {
		Table table = entityClass.getAnnotation(Table.class);
		return table == null ? null : table.name();
	}

	
	@Override
	public boolean equals(Object obj) {
		if(id == null)
			return super.equals(obj);
		else if(obj == null || obj.getClass() != getClass())
			return false;

		@SuppressWarnings("unchecked")
		boolean isEqual = id == ((AuditableEntity)obj).id;
		
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
