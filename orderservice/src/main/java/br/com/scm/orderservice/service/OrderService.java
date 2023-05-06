package br.com.scm.orderservice.service;

import br.com.scm.orderservice.model.OrderRequest;
import br.com.scm.orderservice.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
    OrderResponse getOrderDetails(long orderId);
}
