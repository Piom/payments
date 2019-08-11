package ru.piom.payments.core.config;

public class TenantResolvingException extends Exception {
    public TenantResolvingException(Throwable throwable, String message) {
        super(message, throwable);
    }
}
