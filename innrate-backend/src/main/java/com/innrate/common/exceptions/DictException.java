package com.innrate.common.exceptions;

public class DictException extends RuntimeException {

    private final String id;

    public DictException(String id, String message) {
        super(message + ": " + id);
        this.id = id;
    }

    public DictException(String id, String message, Throwable t) {
        super(message + ": " + id, t);
        this.id = id;
    }
}
