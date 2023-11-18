package ru.kolpakov.Market.App.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.models.Property;
import ru.kolpakov.Market.App.repositories.ProductsRepository;
import ru.kolpakov.Market.App.repositories.PropertiesRepository;
import ru.kolpakov.Market.App.utils.GetPerson;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductsRepository productsRepository;
    private final PropertiesRepository propertiesRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository, PropertiesRepository propertiesRepository) {
        this.productsRepository = productsRepository;
        this.propertiesRepository = propertiesRepository;
    }

    public List<Product> findAll() {
        return productsRepository.findAll();
    }

    public List<Product> findProductsForPersonCart() {
        return GetPerson.returnPersonFromContext().getCart().getProducts();
    }

    public Product findProductById(int id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Transactional
    public void addProduct(Product product) {
        GetPerson.returnPersonFromContext().addProductToPerson(product);
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
    @Transactional
    public void updateProductField(int id, String fieldName, String newValue) {
        Product currentProduct = productsRepository.findById(id).get();
        switch (fieldName) {
            case "name":
                currentProduct.setName(newValue);
                break;
            case "description":
                currentProduct.setDescription(newValue);
                break;
            case "price":
                currentProduct.setPrice(Integer.parseInt(newValue));
                break;

        }
        productsRepository.save(currentProduct);
    }
    @Transactional
    public void addPropertyToProduct(int idProduct, Property property){
        propertiesRepository.save(property);
        productsRepository.findById(idProduct).get().addPropertyToProduct(property);
    }


}
