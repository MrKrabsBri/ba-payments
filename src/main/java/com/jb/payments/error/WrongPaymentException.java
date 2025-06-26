package com.jb.payments.error;

public class WrongPaymentException extends RuntimeException {
    public WrongPaymentException(String message) {
        super(message);
    }

    public WrongPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
