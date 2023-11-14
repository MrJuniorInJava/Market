package ru.kolpakov.Market.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.services.CartService;
import ru.kolpakov.Market.App.services.ProductService;

import java.util.Collections;

@Controller
@RequestMapping("/market")
public class ProductController {
    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping()
    public String mainPage(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("cart_id",ProductService.returnPersonFromContext()
                .getCart().getId());
        return "market/main_page";
    }
    @PostMapping("/search")
    public String searchProduct(@RequestParam(value = "name", required = false) String name,
                                Model model) {
        model.addAttribute("products", productService.searchByFirstChars(name));
        return "market/main_page";
    }

    @GetMapping("/add_product")
    public String addProductPage(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("searchProducts", Collections.emptyList());
        return "market/add_product";
    }

    @PostMapping("/add_product")
    public String addProduct(@ModelAttribute("product") Product product) {
        productService.addProduct(product);
        return "redirect:/market";
    }

    @PostMapping("/delete_product")
    public String deleteProduct(@RequestParam("id") int id) {
        productService.deleteProductById(id);
        return "redirect:/market";
    }
    @GetMapping("/cart/{id}")
    public String cartPage(@PathVariable("id") int id, Model model){
        model.addAttribute("products",productService.findProductsForPersonCart());
        return "market/cart";
    }
    @PostMapping("/add_to_cart")
    public String addProductToCart(@RequestParam("product_id") int idProduct, @RequestParam("cart_id") int idCart) {
        cartService.addProductToCart(idProduct,idCart);
        return "redirect:/market";
    }
    @PostMapping("/delete_product_from_cart")
    public String deleteProductFromCart(@RequestParam("product_id") int idProduct, @RequestParam("cart_id") int idCart) {
        cartService.deleteProductFromCart(idProduct,idCart);
        return "redirect:/market/cart/{cart_id}";
    }


}
