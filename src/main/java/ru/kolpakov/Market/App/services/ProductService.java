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

    @Transactional
    public void addProduct(Product product) {
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Person person = personDetails.returnPerson();
        person.addProductToPerson(product);
        product.setTime(LocalDateTime.now());
        productsRepository.save(product);
    }
    @Transactional
    public void deleteProductById(int id){
        productsRepository.deleteById(id);
    }
}
