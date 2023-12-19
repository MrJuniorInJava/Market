package ru.kolpakov.Market.SecurityForApp.utils;

public class IncorrectUserIsDoingSomethingWithObjectException extends RuntimeException{
    public IncorrectUserIsDoingSomethingWithObjectException(String message) {
        super(message);
    }
}
