package ru.kolpakov.Market.App.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kolpakov.Market.App.repositories.ProductsRepository;
import ru.kolpakov.Market.App.utils.GetPerson;
import ru.kolpakov.Market.App.utils.TimeRefactorClass;
import ru.kolpakov.Market.SecurityForApp.models.Person;
import ru.kolpakov.Market.SecurityForApp.utils.IncorrectUserIsDoingSomethingWithObjectException;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect
public class MyProductAspect {
    private final ProductsRepository productsRepository;

    @Autowired
    public MyProductAspect(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Before("execution(public * ru.kolpakov.Market.App.services.*.addProduct*(..))")
    public void beforeAddProductMethodAdvice(JoinPoint joinPoint) {
        if (!GetPerson.returnPersonFromContext().getRole().equals("ROLE_SELLER")) {
            throw new IncorrectUserIsDoingSomethingWithObjectException("Вы не можете добавлять товары");
        }
    }


    @After("execution(public * ru.kolpakov.Market.App.services.*.addProduct*(..))")
    public void afterAddProductMethodAdvice(JoinPoint joinPoint) {
        Person user = GetPerson.returnPersonFromContext();
        String namePerson = user.getUsername();
        System.out.println("Пользователь " + "'" + namePerson + "'" + " добавил товар в маркет"
                + " в "
                + TimeRefactorClass.timeRefactor(LocalDateTime.now()));

    }

    @Before("execution(public * ru.kolpakov.Market.App.services.*.updateProductField(..)) ||" +
            "execution(public * ru.kolpakov.Market.App.services.*.*Property*Product(..)) ||" +
            "execution(public * ru.kolpakov.Market.App.services.*.*Image*Product(..))")
    public void beforeUpdateProductMethodsAdvice(JoinPoint joinPoint) {
        Person user = GetPerson.returnPersonFromContext();
        Object[] args = joinPoint.getArgs();
        if (user.getId()
                != productsRepository.findById((Integer) args[0]).get().getOwner().getId()) {
            throw new IncorrectUserIsDoingSomethingWithObjectException("Вы не можете изменять даннный товар");
        } else {
            return;
        }

    }

    @Before("execution(public * ru.kolpakov.Market.App.services.*.deleteProduct*(..))")
    public void beforeDeleteProductMethodsAdvice(JoinPoint joinPoint) {
        Person user = GetPerson.returnPersonFromContext();
        Object[] fields = joinPoint.getArgs();
        if (user.getRole().equals("ROLE_ADMIN")) {
            return;
        } else {
            if (user.getId()
                    != productsRepository.findById((Integer) fields[0]).get().getOwner().getId()) {
                throw new IncorrectUserIsDoingSomethingWithObjectException("Вы не можете удалить этот товар");
            } else {
                return;
            }
        }
    }

    @After("execution(public * ru.kolpakov.Market.App.services.*.deleteProduct*(..))")
    public void afterDeleteProductMethodsAdvice(JoinPoint joinPoint) {
        Person user = GetPerson.returnPersonFromContext();
        String namePerson = user.getUsername();
        if (GetPerson.returnPersonFromContext().getRole().equals("ROLE_ADMIN")) {
            System.out.println("Администратор " + "'" + namePerson + "'" + " удалил товар c id " + joinPoint.getArgs()[1] + " к товару " + "c id " + joinPoint.getArgs()[0] + " в "
                    + TimeRefactorClass.timeRefactor(LocalDateTime.now()));
        } else {
            System.out.println("Пользователь " + "'" + namePerson + "'" + " удалил свой отзыв к товару " + "c id " + Arrays.stream(joinPoint.getArgs()).findFirst().get() + " в "
                    + TimeRefactorClass.timeRefactor(LocalDateTime.now()));
        }
    }
}
