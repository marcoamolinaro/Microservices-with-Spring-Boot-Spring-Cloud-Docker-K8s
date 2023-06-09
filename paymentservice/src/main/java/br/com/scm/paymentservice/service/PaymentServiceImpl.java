package br.com.scm.paymentservice.service;

import br.com.scm.paymentservice.entity.TransactionDetails;
import br.com.scm.paymentservice.model.PaymentMode;
import br.com.scm.paymentservice.model.PaymentRequest;
import br.com.scm.paymentservice.model.PaymentResponse;
import br.com.scm.paymentservice.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TransactionDetailsRepository detailsRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {

        log.info("Recording Payment Details {}", paymentRequest);

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();

        detailsRepository.save(transactionDetails);

        long transactionId = transactionDetails.getId();

        log.info("Transaction Completed with Id {}", transactionId);

        return transactionId;
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {
        log.info("Getting payment details by order id {} ", orderId);

        TransactionDetails transactionDetails = detailsRepository.findByOrderId(orderId);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .paymentDate(transactionDetails.getPaymentDate())
                .orderId(transactionDetails.getOrderId())
                .status(transactionDetails.getPaymentStatus())
                .amount(transactionDetails.getAmount())
                .build();

        return paymentResponse;
    }
}
