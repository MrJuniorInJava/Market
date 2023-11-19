package ru.kolpakov.Market.App.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolpakov.Market.App.models.Cart;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.repositories.CartRepository;
import ru.kolpakov.Market.App.repositories.ProductsRepository;

@Service
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductsRepository productsRepository) {
        this.cartRepository = cartRepository;
        this.productsRepository = productsRepository;
    }

    @Transactional
    public void addProductToCart(int idProduct, int idCart) {

        Cart cart = cartRepository.findById(idCart).orElse(null);
        Product product = productsRepository.findById(idProduct).orElse(null);
        cart.addProductToCart(product);

    }
    @Transactional
    public void deleteProductFromCart(int idProduct, int idCart) {
        Cart cart = cartRepository.findById(idCart).orElse(null);
        Product product = productsRepository.findById(idProduct).orElse(null);
        cart.deleteProductFromCart(product);
    }
    public Cart findById(int id){
        return  cartRepository.findById(id).orElse(null);
    }

}
