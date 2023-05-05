package br.com.scm.paymentservice.service;

import br.com.scm.paymentservice.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
}
