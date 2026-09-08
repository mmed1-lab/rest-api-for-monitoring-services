package com.mmed.ws.exception;

public class UniqueUrlException extends RuntimeException {
    public UniqueUrlException(String message) {
        super(message);
    }

    public UniqueUrlException() {
        this("Url exists in the system");
    }
}
