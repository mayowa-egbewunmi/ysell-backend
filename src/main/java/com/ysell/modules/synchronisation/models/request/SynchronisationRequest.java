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
	@JsonProperty("product_data")
	private ProductData productData;

	@NotNull
	@Valid
	@JsonProperty("order_data")
	private OrderData orderData;

	@NotNull
	@Valid
	@JsonProperty("sales_data")
	private SalesData salesData;

	@NotNull
	@Valid
	@JsonProperty("payment_data")
	private PaymentData paymentData;


	@Getter
	@Setter
	public static class ProductData extends SyncData {

		@JsonProperty("unsynced_products")
		private Set<ProductSyncRequestDto> unsyncedProducts;
	}


	@Getter
	@Setter
	public static class OrderData extends SyncData {

		@JsonProperty("unsynced_orders")
		private Set<OrderSyncRequestDto> unsyncedOrders;
	}


	@Getter
	@Setter
	public static class SalesData extends SyncData {

		@JsonProperty("unsynced_sales")
		private Set<SaleSyncRequestDto> unsyncedSales;
	}


	@Getter
	@Setter
	public static class PaymentData extends SyncData {

		@JsonProperty("unsynced_payments")
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
