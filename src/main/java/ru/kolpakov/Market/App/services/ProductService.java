package ru.kolpakov.Market.App.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.repositories.ProductsRepository;
import ru.kolpakov.Market.SecurityForApp.models.Person;
import ru.kolpakov.Market.SecurityForApp.security.PersonDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductsRepository productsRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Product> findAll() {
        return productsRepository.findAll();
    }

    public List<Product> findProductsForPersonCart() {
        return returnPersonFromContext().getCart().getProducts();
    }

    @Transactional
    public void addProduct(Product product) {
        returnPersonFromContext().addProductToPerson(product);
        product.setTime(LocalDateTime.now());
        productsRepository.save(product);
    }

    @Transactional
    public void deleteProductById(int id) {
        productsRepository.deleteById(id);
    }

    public List<Product> searchByFirstChars(String name) {
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        return productsRepository.findAll().stream()
                .filter(object -> pattern.matcher(object.getName()).find())
                .collect(Collectors.toList());
    }


    //Вспомогательные методы

    public static Person returnPersonFromContext() {
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return personDetails.returnPerson();
    }
}