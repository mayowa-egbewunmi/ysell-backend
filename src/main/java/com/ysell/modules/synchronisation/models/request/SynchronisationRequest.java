package com.ysell.modules.synchronisation.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.modules.synchronisation.models.dto.OrderSyncRequestDto;
import com.ysell.modules.synchronisation.models.dto.PaymentSyncRequestDto;
import com.ysell.modules.synchronisation.models.dto.ProductSyncRequestDto;
import com.ysell.modules.synchronisation.models.dto.SaleSyncRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

@AllArgsConstructor
@Getter
public class SynchronisationRequest {

	@NotNull
	@Valid
	private ProductData productData;

	@NotNull
	@Valid
	private OrderData orderData;

	@NotNull
	@Valid
	private SalesData salesData;

	@NotNull
	@Valid
	private PaymentData paymentData;


	@Getter
	@Setter
	public static class ProductData extends SyncData {

		private Set<ProductSyncRequestDto> unsyncedProducts;
	}


	@Getter
	@Setter
	public static class OrderData extends SyncData {

		private Set<OrderSyncRequestDto> unsyncedOrders;
	}


	@Getter
	@Setter
	public static class SalesData extends SyncData {

		private Set<SaleSyncRequestDto> unsyncedSales;
	}


	@Getter
	@Setter
	public static class PaymentData extends SyncData {

		private Set<PaymentSyncRequestDto> unsyncedPayments;
	}


	@Getter
	@Setter
	public static class SyncData {

		private long count;

		@JsonProperty("most_recently_synced_timestamp")
		private Instant mostRecentlySyncedTimestamp;
	}
}
