package com.ysell.modules.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ysell.jpa.entities.base.AuditableEntity;
import com.ysell.jpa.entities.base.NamedEntity;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class LookupDto {

	@NotNull(message = "id is required")
	private UUID id;

	private String name;


	public static LookupDto create(UUID id) {
		return new LookupDto(id, null);
	}


	public static LookupDto create(UUID id, String name) {
		return new LookupDto(id, name);
	}


	public static LookupDto create(NamedEntity namedEntity) {
		return new LookupDto(namedEntity.getId(), namedEntity.getName());
	}


	public <TEntity extends AuditableEntity> TEntity toEntity(Class<TEntity> entityClass) {
		try {
			TEntity entity = entityClass.newInstance();
			entity.setId(id);
			if (NamedEntity.class.isAssignableFrom(entityClass))
				((NamedEntity) entity).setName(name);

			return entity;
		} catch (Exception ex) {
			log.error("Error occurred while converting lookup dto to " + entityClass.getSimpleName(), ex);
			throw new YSellRuntimeException("Could not convert LookupDto to " + entityClass.getSimpleName());
		}
	}


	@Override
	public boolean equals(Object valueToCompare) {
		if(valueToCompare == null || valueToCompare.getClass() != LookupDto.class)
			return false;

		return id == ((LookupDto)valueToCompare).id;
	}


	@Override
	public int hashCode() {
		if(id == null)
			return super.hashCode();

		long bitSum = id.getMostSignificantBits() + id.getLeastSignificantBits();
		return new Long(bitSum).intValue();
	}
}
