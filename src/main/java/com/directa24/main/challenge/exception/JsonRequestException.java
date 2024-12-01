package com.directa24.main.challenge.exception;

public class JsonRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public JsonRequestException(String message) {
        super(message);
    }
}
