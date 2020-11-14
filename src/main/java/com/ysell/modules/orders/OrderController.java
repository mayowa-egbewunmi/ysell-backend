package com.ysell.modules.orders;

import com.ysell.common.annotations.WrapResponse;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.orders.domain.abstractions.OrderService;
import com.ysell.modules.orders.models.request.OrderByOrganisationRequest;
import com.ysell.modules.orders.models.request.OrderIdRequest;
import com.ysell.modules.orders.models.request.OrderRequest;
import com.ysell.modules.orders.models.request.OrderUpdateRequest;
import com.ysell.modules.orders.models.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ControllerConstants.VERSION_URL + "/orders")
@RequiredArgsConstructor
@WrapResponse
public class OrderController {
	
	private final OrderService orderService;

    @PostMapping("")
    @ResponseBody
    public OrderResponse postOrder(@RequestBody OrderRequest request){
        return orderService.postOrder(request);
    }

    @PutMapping("")
    @ResponseBody
    public OrderResponse updateOrder(@RequestBody OrderUpdateRequest request) {
        return orderService.updateOrder(request);
    }

    @PostMapping("/approve")
    @ResponseBody
    public OrderResponse approveOrder(@RequestBody OrderIdRequest request) {
        return orderService.approveOrder(request);
    }

    @PostMapping("/cancel")
    @ResponseBody
    public OrderResponse cancelOrder(@RequestBody OrderIdRequest request) {
        return orderService.cancelOrder(request);
    }
            
    @GetMapping("/by_organisation")
    @ResponseBody
    public List<OrderResponse> getOrdersByOrganisation(@RequestParam("id") Set<Long> ids) {
    	Set<LookupDto> orgLookups = ids.stream()
    			.map(id -> LookupDto.create(id))
    			.collect(Collectors.toSet());
    	OrderByOrganisationRequest request = new OrderByOrganisationRequest(orgLookups);
    	
        return orderService.getOrdersByOrganisation(request);
    }
}
