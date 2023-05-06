package br.com.scm.paymentservice.service;

import br.com.scm.paymentservice.model.PaymentRequest;
import br.com.scm.paymentservice.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
    PaymentResponse getPaymentDetailsByOrderId(long orderId);
}
