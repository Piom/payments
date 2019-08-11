package ru.piom.payments.core.config;

public class TenantNotFoundException extends Exception {
    public TenantNotFoundException(String message) {
        super(message);
    }
}
