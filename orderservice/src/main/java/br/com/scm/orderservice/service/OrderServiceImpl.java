package br.com.scm.orderservice.service;

import br.com.scm.orderservice.entity.Order;
import br.com.scm.orderservice.exception.CustomException;
import br.com.scm.orderservice.external.client.PaymentService;
import br.com.scm.orderservice.external.client.ProductService;
import br.com.scm.orderservice.external.request.PaymentRequest;
import br.com.scm.orderservice.external.response.PaymentResponse;
import br.com.scm.orderservice.model.OrderRequest;
import br.com.scm.orderservice.model.OrderResponse;
import br.com.scm.orderservice.repository.OrderRepository;
import br.com.scm.productservice.model.ProductResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        // Order Entity -> Save the data with status order created
        // Product Service - Block Products (Reduce the Quantity)
        // Payment Service -> Payments -> Success -> COMPLETE, Else

        log.info("Placing Order Request {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order with Status CREATED");

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        orderRepository.save(order);

        long orderId = order.getId();

        log.info("Calling PaymentService to complete the payment ");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(orderId)
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;

        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfully. Changing the Order status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception ex) {
            log.info("Error occurred in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Placed Successfully with Order Id {}", orderId);

        return orderId;
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get Order Details for Order Id {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found for id: " + orderId, "NOT_FOUND", 404));

        log.info("Invoking Product service to fetch the product for id {}", order.getProductId());

        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/products/" + order.getProductId(),
                ProductResponse.class
        );

        log.info("Getting payment information from the payment service");

        PaymentResponse paymentResponse = null;

        try {
            paymentResponse = restTemplate.getForObject(
                    "http://PAYMENT-SERVICE/payments/order/" + order.getId(),
                    PaymentResponse.class
            );
        } catch (Exception ex) {
            log.info("Payment not found with Order id: {}", orderId);
            throw new CustomException("Payment not found with Order id: " + orderId, "PAYMENT_NOT_FOUND", 404);
        }

        OrderResponse.ProductDetails productDetails =
                OrderResponse.ProductDetails.builder()
                        .productName(productResponse.getProductName())
                        .productId(productResponse.getProductId())
                        .quantity(productResponse.getQuantity())
                        .price(productResponse.getPrice())
                        .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .status(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .amount(paymentResponse.getAmount())
                .orderId(paymentResponse.getOrderId())
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();

        return orderResponse;
    }
}
