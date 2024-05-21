package ru.yarm.banksample.Exceptions;

public class UserWrongCredentials extends RuntimeException {
    public UserWrongCredentials(String message) {
        super(message);
    }
}
