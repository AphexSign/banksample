package ru.yarm.banksample.Exceptions;

public class ZeroContactInfoException extends RuntimeException {
    public ZeroContactInfoException(String message) {
        super(message);
    }

}
