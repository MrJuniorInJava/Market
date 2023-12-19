package ru.kolpakov.Market.App.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kolpakov.Market.App.repositories.ReviewsRepository;
import ru.kolpakov.Market.App.utils.GetPerson;
import ru.kolpakov.Market.App.utils.TimeRefactorClass;
import ru.kolpakov.Market.SecurityForApp.models.Person;
import ru.kolpakov.Market.SecurityForApp.utils.IncorrectUserIsDoingSomethingWithObjectException;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect
public class MyReviewAspect {
    private final ReviewsRepository reviewsRepository;

    @Autowired
    public MyReviewAspect(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    @After("execution(public * ru.kolpakov.Market.App.services.*.addReview*(..))")
    public void afterAddReviewMethodsAdvice(JoinPoint joinPoint) {
        String namePerson = GetPerson.returnPersonFromContext().getUsername();
        System.out.println("Пользователь " + "'" + namePerson + "'" + " добавил отзыв к товару " + "c id " + joinPoint.getArgs()[0] + " в "
                + TimeRefactorClass.timeRefactor(LocalDateTime.now()));

    }

    @Before("execution(public * ru.kolpakov.Market.App.services.*.deleteReview*(..))")
    public void beforeDeleteReviewMethodsAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Person user = GetPerson.returnPersonFromContext();
        if (user.getRole().equals("ROLE_ADMIN")) {
            return;
        } else {
            if (user.getId()
                    != reviewsRepository.findById((Integer) args[1]).get().getOwner().getId()) {
                throw new IncorrectUserIsDoingSomethingWithObjectException("Вы не можете удалять чужие отзывы");
            } else {
                return;
            }
        }


    }

    @After("execution(public * ru.kolpakov.Market.App.services.*.deleteReview*(..))")
    public void afterDeleteReviewMethodsAdvice(JoinPoint joinPoint) {
        String namePerson = GetPerson.returnPersonFromContext().getUsername();
        if (GetPerson.returnPersonFromContext().getRole().equals("ROLE_ADMIN")) {
            System.out.println("Администратор " + "'" + namePerson + "'" + " удалил отзыв c id " + joinPoint.getArgs()[1] + " к товару " + "c id " + joinPoint.getArgs()[0] + " в "
                    + TimeRefactorClass.timeRefactor(LocalDateTime.now()));
        } else {
            System.out.println("Пользователь " + "'" + namePerson + "'" + " удалил свой отзыв к товару " + "c id " + joinPoint.getArgs()[0] + " в "
                    + TimeRefactorClass.timeRefactor(LocalDateTime.now()));
        }
    }
}
