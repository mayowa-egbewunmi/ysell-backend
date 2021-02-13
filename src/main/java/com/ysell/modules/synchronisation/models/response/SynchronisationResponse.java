package com.ysell.modules.synchronisation.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.modules.synchronisation.models.dto.OrderSyncResponseDto;
import com.ysell.modules.synchronisation.models.dto.PaymentSyncResponseDto;
import com.ysell.modules.synchronisation.models.dto.UpdatedSyncResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class SynchronisationResponse {
	
	private OrderData orderData;

	private PaymentData paymentData;

	private SalesData salesData;

	private ProductData productData;


	@AllArgsConstructor
	@Getter
	public static class OrderData {

		@JsonProperty("updated_orders")
		private Set<UpdatedSyncResponseDto> updatedOrders;

		@JsonProperty("new_orders")
		private Set<OrderSyncResponseDto> newOrders;

		@JsonProperty("new_orders_count")
		public int getNewOrdersCount() {
			return newOrders.size();
		}

		@JsonProperty("updated_orders_count")
		public int getUpdatedOrdersCount() {
			return updatedOrders.size();
		}
	}


	public static class PaymentData {

		@JsonProperty("updated_payments")
		private Set<UpdatedSyncResponseDto> updatedPayments;

		@JsonProperty("new_payments")
		private Set<PaymentSyncResponseDto> newPayments;

		@JsonProperty("new_payments_count")
		public int getNewPaymentsCount() {
			return newPayments.size();
		}

		@JsonProperty("updated_payments_count")
		public int getUpdatedPaymentsCount() {
			return updatedPayments.size();
		}
	}


	public static class SalesData {

		@JsonProperty("updated_sales")
		private Set<UpdatedSyncResponseDto> updatedSales;

		@JsonProperty("new_sales")
		private Set<OrderSyncResponseDto> newSales;

		@JsonProperty("new_sales_count")
		public int getNewSalesCount() {
			return newSales.size();
		}

		@JsonProperty("updated_sales_count")
		public int getUpdatedSalesCount() {
			return updatedSales.size();
		}
	}


	public static class ProductData {

		@JsonProperty("updated_products")
		private Set<UpdatedSyncResponseDto> updatedProducts;

		@JsonProperty("new_products")
		private Set<OrderSyncResponseDto> newProducts;

		@JsonProperty("new_products_count")
		public int getNewProductsCount() {
			return newProducts.size();
		}

		@JsonProperty("updated_products_count")
		public int getUpdatedProductsCount() {
			return updatedProducts.size();
		}
	}
}
