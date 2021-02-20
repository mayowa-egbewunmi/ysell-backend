package com.ysell.modules.orders;

import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.orders.domain.OrderService;
import com.ysell.modules.orders.models.request.OrderCreateRequest;
import com.ysell.modules.orders.models.request.OrderUpdateRequest;
import com.ysell.modules.orders.models.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(OrderController.PATH)
@RequiredArgsConstructor
public class OrderController {

    public static final String PATH = ControllerConstants.VERSION_URL + "/orders";

	private final OrderService orderService;


    @PostMapping
    public OrderResponse postOrder(@RequestBody @Valid OrderCreateRequest request){
        return orderService.postOrder(request);
    }


    @PutMapping("/{orderId}")
    public OrderResponse updateOrder(@PathVariable UUID orderId, @RequestBody @Valid OrderUpdateRequest request) {
        return orderService.updateOrder(orderId, request);
    }


    @PostMapping("/{orderId}/approve")
    public OrderResponse approveOrder(@PathVariable UUID orderId) {
        return orderService.approveOrder(orderId);
    }


    @PostMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(@PathVariable UUID orderId) {
        return orderService.cancelOrder(orderId);
    }


    @GetMapping("/by-organisation")
    public List<OrderResponse> getOrdersByOrganisation(@RequestParam("organisationId") Set<UUID> organisationIds) {
        return orderService.getOrdersByOrganisationIds(organisationIds);
    }
}
