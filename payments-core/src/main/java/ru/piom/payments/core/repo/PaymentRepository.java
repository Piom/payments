package ru.piom.payments.core.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.piom.payments.core.model.Payment;

import java.math.BigDecimal;

public interface PaymentRepository extends CrudRepository<Payment, Long> {


    @Query(value = "SELECT sum(amount) from Payment where sender = ?1", nativeQuery = true)
    BigDecimal totalAmount(String sender);
}
