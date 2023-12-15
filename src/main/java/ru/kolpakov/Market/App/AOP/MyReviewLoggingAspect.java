package ru.kolpakov.Market.App.AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.kolpakov.Market.App.utils.GetPerson;
import ru.kolpakov.Market.App.utils.TimeRefactorClass;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect
public class MyReviewLoggingAspect {
    @After("execution(public * ru.kolpakov.Market.App.services.*.addReview*(..))")
    public void addReviewMethodsAdvice(JoinPoint joinPoint) {
        String namePerson = GetPerson.returnPersonFromContext().getUsername();
        System.out.println("Пользователь " + namePerson + " добавил отзыв к товару "+ "c id " + Arrays.stream(joinPoint.getArgs()).findFirst().get() + " в "
                + TimeRefactorClass.timeRefactor(LocalDateTime.now()));

    }
    @After("execution(public * ru.kolpakov.Market.App.services.*.deleteReview*(..))")
    public void deleteReviewMethodsAdvice(JoinPoint joinPoint) {
        String namePerson = GetPerson.returnPersonFromContext().getUsername();
        System.out.println("Пользователь " +namePerson + " удалил свой отзыв к товару "+ "c id " + Arrays.stream(joinPoint.getArgs()).findFirst().get() + " в "
                + TimeRefactorClass.timeRefactor(LocalDateTime.now()));

    }
}
