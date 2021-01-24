package com.ysell.modules.synchronisation.models.request;

import com.ysell.modules.common.dto.LookupDto;
import com.ysell.modules.synchronisation.models.dto.OrderDto;
import com.ysell.modules.synchronisation.models.dto.ProductDto;
import com.ysell.modules.synchronisation.models.dto.StockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Getter
public class SynchronisationRequest {

	@NotNull
	@Valid
	private Set<LookupDto> userOrganisations;

	@NotNull
	private LocalDate lastSyncTime;

	@Valid
	private Set<StockDto> newClientStocks;

	@Valid
	private Set<ProductDto> newClientProducts;

	@Valid
	private Set<OrderDto> newClientOrders;
}
