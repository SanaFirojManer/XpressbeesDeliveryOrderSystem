package com.dev.sana.xpressbees.delivery.delivery_order_system.exception;

public class InvalidOrderStatusException extends RuntimeException{
    public InvalidOrderStatusException(String message) {
        super(message);
    }
}
