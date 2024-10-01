package com.estacionamento.park.api.demo.parkapi.exception;

public class CpfUniqueViolationException extends RuntimeException {
    public CpfUniqueViolationException(String s) {
        super(s);
    }
}
