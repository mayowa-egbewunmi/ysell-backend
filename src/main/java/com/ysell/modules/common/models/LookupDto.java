package com.ysell.modules.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class LookupDto {

	@NotNull
	private long id;

	private String name;

	public static LookupDto create(long id) {
		return new LookupDto(id, null);
	}

	public static LookupDto create(long id, String name) {
		return new LookupDto(id, name);
	}

	@Override
	public boolean equals(Object valueToCompare) {
		if (valueToCompare == null || !valueToCompare.getClass().isAssignableFrom(LookupDto.class))
			return false;

		return id == ((LookupDto) valueToCompare).id;
	}

	@Override
	public int hashCode() {
		return new Long(id).intValue();
	}
}
