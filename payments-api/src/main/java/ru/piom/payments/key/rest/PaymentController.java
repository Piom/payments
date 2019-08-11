package ru.piom.payments.key.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import ru.piom.payments.core.config.MultiTenantManager;
import ru.piom.payments.core.config.TenantNotFoundException;
import ru.piom.payments.core.config.TenantResolvingException;
import ru.piom.payments.core.exception.InvalidDbPropertiesException;
import ru.piom.payments.core.exception.InvalidTenantIdException;
import ru.piom.payments.core.model.Payment;
import ru.piom.payments.core.service.Pair;
import ru.piom.payments.core.service.PaymentService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/payments")
public class PaymentController {
    private static final String MSG_INVALID_TENANT_ID = "[!] DataSource not found for given tenant Id '{}'!";
    private static final String MSG_INVALID_DB_PROPERTIES_ID = "[!] DataSource properties related to the given tenant ('{}') is invalid!";
    private static final String MSG_RESOLVING_TENANT_ID = "[!] Could not resolve tenant ID '{}'!";

    private final PaymentService service;
    private final MultiTenantManager tenantManager;

    public PaymentController(PaymentService service, MultiTenantManager tenantManager) {
        this.service = service;
        this.tenantManager = tenantManager;
    }

    @GetMapping("/sum/bySender")
    public BigDecimal sumBySender(@RequestParam("sender") String sender) {
        setTenant(sender.hashCode());
        return service.getTotalAmountBySender(sender);
    }


    @GetMapping("/size")
    public Map<String, Long> getSizeOfAllStorage() throws ExecutionException, InterruptedException {
        Map<String, Long> collect = tenantManager.getTenantList().stream()
                .map(tenant -> getSize(tenant.toString())).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        Map<String, Long> sortedByKey = collect.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return sortedByKey;
    }

    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        setTenant(payment.getSender().hashCode());
        return service.create(payment);
    }

    private Pair<String, Long> getSize(String tenantId) {
        setTenant(tenantId);
        return service.getSize(tenantId);
    }

    private void setTenant(int sender) {
        int tenant = sender % tenantManager.getTenantList().size();
        String tenantId = "tenant" + ++tenant;
        setTenant(tenantId);
    }

    private void setTenant(String tenantId) {
        try {
            tenantManager.setCurrentTenant(tenantId);
        } catch (SQLException e) {
            log.error(MSG_INVALID_DB_PROPERTIES_ID, tenantId);
            throw new InvalidDbPropertiesException();
        } catch (TenantNotFoundException e) {
            log.error(MSG_INVALID_TENANT_ID, tenantId);
            throw new InvalidTenantIdException();
        } catch (TenantResolvingException e) {
            log.error(MSG_RESOLVING_TENANT_ID, tenantId);
            throw new InvalidTenantIdException();
        }
    }
}
