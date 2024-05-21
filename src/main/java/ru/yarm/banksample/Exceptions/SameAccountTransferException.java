package ru.yarm.banksample.Exceptions;

public class SameAccountTransferException extends RuntimeException{
    public SameAccountTransferException(String message) {
        super(message);
    }

}
