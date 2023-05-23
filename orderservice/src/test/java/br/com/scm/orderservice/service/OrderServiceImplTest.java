package br.com.scm.orderservice.service;

import br.com.scm.orderservice.entity.Order;
import br.com.scm.orderservice.external.client.PaymentService;
import br.com.scm.orderservice.external.client.ProductService;
import br.com.scm.orderservice.external.response.PaymentResponse;
import br.com.scm.orderservice.model.OrderResponse;
import br.com.scm.orderservice.model.PaymentMode;
import br.com.scm.orderservice.repository.OrderRepository;
import br.com.scm.productservice.model.ProductResponse;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();
    @DisplayName("Get Order - Success Scenario")
    @Test
    public void test_When_Order_Success() {
        Order order = getMockOrder();

        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/products/" + order.getProductId(),
                ProductResponse.class)).thenReturn(getMockProductResponse());

        when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payments/order/" + order.getId(),
                PaymentResponse.class)).thenReturn(getMockPaymentResponse());

        // Actual
        OrderResponse orderResponse = orderService.getOrderDetails(1);

        // Verification
        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject(
                "http://PRODUCT-SERVICE/products/" + order.getProductId(),
                ProductResponse.class);
        verify(restTemplate, times(1)).getForObject(
                "http://PAYMENT-SERVICE/payments/order/" + order.getId(),
                PaymentResponse.class);

        // Assert
        assertNotNull(orderResponse);
        assertEquals(order.getId(), orderResponse.getOrderId());
    }

    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse
                .builder()
                .paymentId(1)
                .paymentDate(Instant.now())
                .paymentMode(PaymentMode.CASH)
                .amount(200)
                .orderId(1)
                .status("ACCEPTED")
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse
                .builder()
                .productId(2)
                .productName("iPhone")
                .price(1000)
                .quantity(200)
                .build();
    }

    private Order getMockOrder() {
        return Order
                .builder()
                .orderStatus("PLACED0")
                .orderDate(Instant.now())
                .id(1)
                .amount(100)
                .quantity(200)
                .productId(2)
                .build();
    }

}