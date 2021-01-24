package com.ysell.modules.synchronisation.models.dto;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class StockResponseDto {

	private UUID id;

	private LookupDto product;

	private Integer quantity;
}
