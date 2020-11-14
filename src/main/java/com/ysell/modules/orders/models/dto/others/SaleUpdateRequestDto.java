package com.ysell.modules.orders.models.dto.others;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleUpdateRequestDto extends SaleRequestDto{

	@NotNull
	private UUID id;
}
