package ru.kolpakov.Market.SecurityForApp.utils;

public class IncorrectUserIsDeletingReviewException extends RuntimeException{
    public IncorrectUserIsDeletingReviewException(String message) {
        super(message);
    }
}
