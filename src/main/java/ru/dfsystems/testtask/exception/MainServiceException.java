package ru.dfsystems.testtask.exception;

public class MainServiceException extends RuntimeException{
    public MainServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
