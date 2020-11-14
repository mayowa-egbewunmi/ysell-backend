package com.ysell.modules.common.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClientLookupDto {

	private UUID id;
	
	private String name;
		
	public static ClientLookupDto create(UUID id) {
		return new ClientLookupDto(id, null);
	}
	
	public static ClientLookupDto create(UUID id, String name) {
		return new ClientLookupDto(id, name);
	}

	@Override
	public boolean equals(Object valueToCompare) {
		if(valueToCompare == null || valueToCompare.getClass() != ClientLookupDto.class)
			return false;
		
		return id == ((ClientLookupDto)valueToCompare).id;
	}
	
	@Override	
	public int hashCode() {
		if(id == null)
			return super.hashCode();
		
		long bitSum = id.getMostSignificantBits() + id.getLeastSignificantBits();
		return new Long(bitSum).intValue();
	}
}
