package ru.piom.payments.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.piom.payments.core.model.Payment;
import ru.piom.payments.core.repo.PaymentRepository;

import java.math.BigDecimal;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Pair<String, Long> getSize(String tenantId) {
        long size = paymentRepository.count();
        Pair<String, Long> result = new Pair<>(tenantId, size);
        return result;
    }

    @Override
    public BigDecimal getTotalAmountBySender(String sender) {
        return paymentRepository.totalAmount(sender);
    }
}
