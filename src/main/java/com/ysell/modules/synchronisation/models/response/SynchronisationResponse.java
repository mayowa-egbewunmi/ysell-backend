package com.ysell.modules.synchronisation.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.modules.synchronisation.models.dto.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class SynchronisationResponse {

	private ProductData productData;
	
	private OrderData orderData;

	private SalesData salesData;

	private PaymentData paymentData;


	@Builder
	@Getter
	public static class ProductData {

		@JsonProperty("synced_products")
		private Set<UpdatedSyncResponseDto> syncedProducts;

		@JsonProperty("synced_products_count")
		public int getSyncedProductsCount() {
			return syncedProducts.size();
		}

		@JsonProperty("new_products")
		private Set<ProductSyncResponseDto> newProducts;

		@JsonProperty("new_products_count")
		public int getNewProductsCount() {
			return newProducts.size();
		}

		@JsonProperty("updated_products")
		private Set<ProductSyncResponseDto> updatedProducts;

		@JsonProperty("updated_products_count")
		public int getUpdatedProductsCount() {
			return updatedProducts.size();
		}
	}


	@Builder
	@Getter
	public static class OrderData {

		@JsonProperty("synced_orders")
		private Set<UpdatedSyncResponseDto> syncedOrders;

		@JsonProperty("synced_orders_count")
		public int getSyncedOrdersCount() {
			return syncedOrders.size();
		}

		@JsonProperty("new_orders")
		private Set<OrderSyncResponseDto> newOrders;

		@JsonProperty("new_orders_count")
		public int getNewOrdersCount() {
			return newOrders.size();
		}

		@JsonProperty("updated_orders")
		private Set<OrderSyncResponseDto> updatedOrders;

		@JsonProperty("updated_orders_count")
		public int getUpdatedOrdersCount() {
			return updatedOrders.size();
		}
	}


	@Builder
	@Getter
	public static class SalesData {

		@JsonProperty("synced_sales")
		private Set<UpdatedSyncResponseDto> syncedSales;

		@JsonProperty("synced_sales_count")
		public int getSyncedSalesCount() {
			return syncedSales.size();
		}

		@JsonProperty("new_sales")
		private Set<SaleSyncResponseDto> newSales;

		@JsonProperty("new_sales_count")
		public int getNewSalesCount() {
			return newSales.size();
		}

		@JsonProperty("updated_sales")
		private Set<SaleSyncResponseDto> updatedSales;

		@JsonProperty("updated_sales_count")
		public int getUpdatedSalesCount() {
			return updatedSales.size();
		}
	}


	@Builder
	@Getter
	public static class PaymentData {

		@JsonProperty("synced_payments")
		private Set<UpdatedSyncResponseDto> syncedPayments;

		@JsonProperty("synced_payments_count")
		public int getSyncedPaymentsCount() {
			return syncedPayments.size();
		}

		@JsonProperty("new_payments")
		private Set<PaymentSyncResponseDto> newPayments;

		@JsonProperty("new_payments_count")
		public int getNewPaymentsCount() {
			return newPayments.size();
		}

		@JsonProperty("updated_payments")
		private Set<PaymentSyncResponseDto> updatedPayments;

		@JsonProperty("updated_payments_count")
		public int getUpdatedPaymentsCount() {
			return updatedPayments.size();
		}
	}
}
