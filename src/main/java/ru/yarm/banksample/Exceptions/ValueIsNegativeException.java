package ru.yarm.banksample.Exceptions;

public class ValueIsNegativeException extends RuntimeException{
    public ValueIsNegativeException(String message) {
        super(message);
    }
}
