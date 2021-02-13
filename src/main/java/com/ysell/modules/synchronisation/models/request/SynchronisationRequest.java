package com.ysell.modules.synchronisation.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.modules.synchronisation.models.dto.OrderSyncRequestDto;
import com.ysell.modules.synchronisation.models.dto.PaymentSyncRequestDto;
import com.ysell.modules.synchronisation.models.dto.ProductSyncRequestDto;
import com.ysell.modules.synchronisation.models.dto.SaleSyncRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Timestamp;
import java.util.Set;

@AllArgsConstructor
@Getter
public class SynchronisationRequest {

	@NotNull
	@Valid
	private OrderData orderData;

	@NotNull
	@Valid
	private PaymentData paymentData;

	@NotNull
	@Valid
	private SalesData salesData;

	@NotNull
	@Valid
	private ProductData productData;


	@Getter
	public static class OrderData extends SyncData {

		private Set<OrderSyncRequestDto> unsyncedOrders;

		public OrderData(Set<OrderSyncRequestDto> unsyncedOrders, Timestamp mostRecentlySyncedTimestamp) {
			super(unsyncedOrders.size(), mostRecentlySyncedTimestamp);
			this.unsyncedOrders = unsyncedOrders;
		}
	}


	@Getter
	public static class PaymentData extends SyncData {

		private Set<PaymentSyncRequestDto> unsyncedPayments;

		public PaymentData(Set<PaymentSyncRequestDto> unsyncedPayments, Timestamp mostRecentlySyncedTimestamp) {
			super(unsyncedPayments.size(), mostRecentlySyncedTimestamp);
			this.unsyncedPayments = unsyncedPayments;
		}
	}


	@Getter
	public static class SalesData extends SyncData {

		private Set<SaleSyncRequestDto> unsyncedSales;

		public SalesData(Set<SaleSyncRequestDto> unsyncedSales, Timestamp mostRecentlySyncedTimestamp) {
			super(unsyncedSales.size(), mostRecentlySyncedTimestamp);
			this.unsyncedSales = unsyncedSales;
		}
	}


	@Getter
	public static class ProductData extends SyncData {

		private Set<ProductSyncRequestDto> unsyncedProducts;

		public ProductData(Set<ProductSyncRequestDto> unsyncedProducts, Timestamp mostRecentlySyncedTimestamp) {
			super(unsyncedProducts.size(), mostRecentlySyncedTimestamp);
			this.unsyncedProducts = unsyncedProducts;
		}
	}


	@AllArgsConstructor
	@Getter
	public static class SyncData {

		private long count;

		@JsonProperty("most_recently_synced_timestamp")
		private Timestamp mostRecentlySyncedTimestamp;
	}
}
