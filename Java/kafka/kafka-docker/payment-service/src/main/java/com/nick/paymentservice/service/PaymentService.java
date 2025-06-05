package com.nick.paymentservice.service;

import com.nick.paymentservice.model.Payment;

public interface PaymentService {

    void sendPayment(Payment payment);
}
