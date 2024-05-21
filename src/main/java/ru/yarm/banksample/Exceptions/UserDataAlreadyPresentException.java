package ru.yarm.banksample.Exceptions;

public class UserDataAlreadyPresentException extends RuntimeException {
    public UserDataAlreadyPresentException(String message) {
        super(message);
    }

}
