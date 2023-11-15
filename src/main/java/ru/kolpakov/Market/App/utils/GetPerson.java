package ru.kolpakov.Market.App.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.kolpakov.Market.SecurityForApp.models.Person;
import ru.kolpakov.Market.SecurityForApp.security.PersonDetails;

public class GetPerson {
    public static Person returnPersonFromContext() {
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return personDetails.returnPerson();
    }
}
