package ru.yarm.banksample.Exceptions;

public class AlreadyDeletedException extends RuntimeException{

    public AlreadyDeletedException(String message) {
        super(message);
    }

}
