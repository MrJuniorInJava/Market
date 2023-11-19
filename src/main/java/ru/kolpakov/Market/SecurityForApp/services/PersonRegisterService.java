package ru.kolpakov.Market.SecurityForApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolpakov.Market.App.models.Cart;
import ru.kolpakov.Market.App.repositories.CartRepository;
import ru.kolpakov.Market.SecurityForApp.models.Person;
import ru.kolpakov.Market.SecurityForApp.repositories.PersonRepository;

import java.time.LocalDateTime;

@Service
public class PersonRegisterService {

    private final PersonRepository personRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public PersonRegisterService(PersonRepository personRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.cartRepository = cartRepository;

        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));//шифруем пароль
        person.setRole("ROLE_USER");
        person.setDateOfRegistration(LocalDateTime.now());
        Cart cart = new Cart();
        cart.setOwner(person);
        person.setCart(cart);
        personRepository.save(person);
        cartRepository.save(cart);
    }
}
