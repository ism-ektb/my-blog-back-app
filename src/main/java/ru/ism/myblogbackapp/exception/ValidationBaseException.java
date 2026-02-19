package ru.ism.myblogbackapp.exception;

public class ValidationBaseException extends RuntimeException {
    public ValidationBaseException(String e) {
        super(e);
    }
}
