package ru.piom.payments.core.service;

import ru.piom.payments.core.model.Payment;

import java.math.BigDecimal;

public interface PaymentService {
    Payment create(Payment payment);

    Pair<String, Long> getSize(String tenantId);

    BigDecimal getTotalAmountBySender(String sender);
}
