package ru.kolpakov.Market.App.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolpakov.Market.App.models.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
}
