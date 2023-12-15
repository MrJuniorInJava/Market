package ru.kolpakov.Market.App.utils;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeRefactorClass {
    public static String timeRefactor(LocalDateTime time){
        Pattern pattern = Pattern.compile("([0-9]{4})" +
                "(-)" +
                "([0-9]{2})" +
                "(-)" +
                "([0-9]{2})" +
                "(T)" +
                "([0-9]{2})" +
                "(:)" +
                "([0-9]{2})" +
                "(:)" +
                "([0-9]{2})" +
                "[.0-9]+$");
        Matcher matcher = pattern.matcher(time.toString());
        return  matcher.replaceAll("$7$8$9$10$11 $1$2$3$4$5");
    }
}
