package ru.kolpakov.Market.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.services.CartService;
import ru.kolpakov.Market.App.services.ProductService;
import ru.kolpakov.Market.App.utils.GetPerson;

import java.util.Collections;

@Controller
@RequestMapping("/market")
public class MainController {
    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public MainController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping()
    public String mainPage(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("cart_id", GetPerson.returnPersonFromContext()
                .getCart().getId());
        return "market/main_page";
    }

    @GetMapping("/product/{id}")
    public String productPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.findProductById(id));
        return "market/product_page";
    }

    @PostMapping("/product/{id}")
    public String updateField(@PathVariable("id") int id,
                              @RequestParam("fieldName") String fieldName,
                              @RequestParam("newValue") String newValue) {
        productService.updateProductField(id, fieldName, newValue);
        return "redirect:/market/product/{id}";
    }

    @PostMapping()
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
    public String cartPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("cart", cartService.findBtId(id));
        return "market/cart";
    }

    @PostMapping("/cart/{id}/add_product")
    public String addProductToCart(@RequestParam("product_id") int idProduct, @PathVariable("id") int idCart,
                                   Model model) {
        cartService.addProductToCart(idProduct, idCart);
        return "redirect:/market";
    }

    @PostMapping("/cart/{id}/delete_product")
    public String deleteProductFromCart(@RequestParam("product_id") int idProduct,
                                        @PathVariable("id") int idCart) {
        cartService.deleteProductFromCart(idProduct, idCart);
        return "redirect:/market/cart/{id}";
    }


}
