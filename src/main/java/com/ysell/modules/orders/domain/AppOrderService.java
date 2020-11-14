package com.ysell.modules.orders.domain;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.orders.domain.abstractions.OrderDao;
import com.ysell.modules.orders.domain.abstractions.OrderService;
import com.ysell.modules.orders.enums.OrderStatus;
import com.ysell.modules.orders.models.dto.others.SaleRequestDto;
import com.ysell.modules.orders.models.dto.output.ProductDto;
import com.ysell.modules.orders.models.dto.output.SaleOutputDto;
import com.ysell.modules.orders.models.request.OrderByOrganisationRequest;
import com.ysell.modules.orders.models.request.OrderIdRequest;
import com.ysell.modules.orders.models.request.OrderRequest;
import com.ysell.modules.orders.models.request.OrderUpdateRequest;
import com.ysell.modules.orders.models.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AppOrderService implements OrderService {

	private final OrderDao orderDao;

	@Override
	@Transactional
	public OrderResponse postOrder(@Valid OrderRequest request) {
		validateProductAndStock(request.getSales(), request.getOrganisation().getId());

		updateTotalPrice(request);

		OrderResponse orderResponse = orderDao.saveOrder(request, OrderStatus.PENDING.getValue());

		request.getSales().forEach(sale -> {
			int quantityToRemove = sale.getQuantity() * -1;
			orderDao.updateProductStock(sale.getProduct().getId(), quantityToRemove);
		});

		return orderResponse;
	}

	@Override
	@Transactional
	public OrderResponse updateOrder(@Valid final OrderUpdateRequest request) {
		OrderResponse oldOrder = orderDao.getOrder(request.getId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("Order", request.getId()));

		addSaleProductsBackToStock(oldOrder.getSales());

		validateProductAndStock(request.getSales(), request.getOrganisation().getId());

		updateTotalPrice(request);

		OrderResponse orderResponse = orderDao.updateOrder(request, OrderStatus.PENDING.getValue());

		request.getSales().forEach(sale -> {
			int quantityToRemove = sale.getQuantity() * -1;
			orderDao.updateProductStock(sale.getProduct().getId(), quantityToRemove);
		});

		return orderResponse;
	}

	private void addSaleProductsBackToStock(Set<SaleOutputDto> oldSales) {
		oldSales.forEach(oldSale -> {
			orderDao.updateProductStock(oldSale.getProduct().getId(), oldSale.getQuantity());
		});
	}

	private <T extends SaleRequestDto> void validateProductAndStock(Set<T> sales, long organisationId) {
		for (T sale : sales) {
			ProductDto productDto = orderDao.getProduct(sale.getProduct().getId())
					.orElseThrow(() -> ServiceUtils.wrongIdException("Order", sale.getProduct().getId()));

			if (sale.getQuantity() < 0)
				throw new YSellRuntimeException(String.format("Cannot order for negative (%d) amount of %s", sale.getQuantity(), productDto.getName()));
			else if (productDto == null)
				throw ServiceUtils.wrongIdException("Product", sale.getProduct().getId());
			else if (productDto.getOrganisation().getId() != organisationId)
				throw new YSellRuntimeException(String.format("Product with id %d does not belong to Organisation with id %d", sale.getProduct().getId(), organisationId));
			else if (productDto.getCurrentStock() < sale.getQuantity())
				throw new YSellRuntimeException(String.format("%s has only %d items left. Cannot order %d", productDto.getName(), productDto.getCurrentStock(), sale.getQuantity()));
		}
	}

	private void updateTotalPrice(OrderRequest request) {
		request.getSales().forEach(sale -> sale = updateSalePrice(sale));

		BigDecimal totalDiscountPrice = getDiscountedPrice(
				request.getSales().stream().map(s -> s.getTotalPrice()),
				request.getPercentageDiscount());
		request.setTotalPrice(totalDiscountPrice);
	}

	private void updateTotalPrice(OrderUpdateRequest request) {
		request.getSales().forEach(sale -> sale = updateSalePrice(sale));

		BigDecimal totalDiscountPrice = getDiscountedPrice(
				request.getSales().stream().map(s -> s.getTotalPrice()),
				request.getPercentageDiscount());
		request.setTotalPrice(totalDiscountPrice);
	}

	private BigDecimal getDiscountedPrice(Stream<BigDecimal> prices, double percentageDiscount) {
		BigDecimal totalPrice = prices.parallel()
				.reduce(new BigDecimal(0),
						(a, b) -> a.add(b),
						(u, v) -> u.add(v));

		return applyDiscount(totalPrice, percentageDiscount);
	}

	private <T extends SaleRequestDto> T updateSalePrice(T sale) {
		ProductDto productDto = orderDao.getProduct(sale.getProduct().getId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("Product", sale.getProduct().getId()));

		BigDecimal quantity = new BigDecimal(sale.getQuantity());
		BigDecimal price = productDto.getPrice().multiply(quantity);
		BigDecimal discountPrice = applyDiscount(price, sale.getPercentageDiscount());
		sale.setTotalPrice(discountPrice);

		return sale;
	}

	private BigDecimal applyDiscount(BigDecimal price, double percentageDiscount) {
		BigDecimal decimalDiscount = new BigDecimal(percentageDiscount);
		BigDecimal discount = price.multiply(decimalDiscount).divide(new BigDecimal(100));
		return price.subtract(discount);
	}

	@Override
	public OrderResponse approveOrder(@Valid OrderIdRequest request) {
		return orderDao.updateOrderStatus(request.getId(), OrderStatus.CANCELLED);
	}

	@Override
	public OrderResponse cancelOrder(@Valid OrderIdRequest request) {
		return orderDao.updateOrderStatus(request.getId(), OrderStatus.CANCELLED);
	}

	private OrderResponse updateOrderStatus(UUID id, OrderStatus status) {
		if (!orderDao.getOrder(id).isPresent())
			throw ServiceUtils.wrongIdException("Order", id);

		return orderDao.updateOrderStatus(id, status);
	}

	@Override
	public List<OrderResponse> getOrdersByOrganisation(@Valid OrderByOrganisationRequest request) {
		for (LookupDto organisation : request.getOrganisations()) {
			if (!orderDao.hasOrganisation(organisation.getId()))
				throw ServiceUtils.wrongIdException("Order", organisation.getId());
		}

		return request.getOrganisations().stream()
				.flatMap(org -> orderDao.getOrderByOrganisation(org.getId()).stream())
				.collect(Collectors.toList());
	}
}
