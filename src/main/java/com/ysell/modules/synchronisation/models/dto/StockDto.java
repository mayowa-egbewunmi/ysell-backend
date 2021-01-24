package com.ysell.modules.synchronisation.models.dto;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class StockDto {

	@NotNull
	private UUID id;

	@NotNull
	@Valid
	private LookupDto product;

	@NotNull
	private Integer quantity;

	@NotNull
	private LocalDate clientCreatedAt;

	private LocalDate clientUpdatedAt;
}
