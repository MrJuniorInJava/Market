package ru.kolpakov.Market.App.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolpakov.Market.App.models.Product;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product,Integer> {
    List<Product> searchProductByNameStartingWith(String name);
}
