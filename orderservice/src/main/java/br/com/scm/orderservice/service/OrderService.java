package br.com.scm.orderservice.service;

import br.com.scm.orderservice.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
