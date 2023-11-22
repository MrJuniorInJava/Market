package ru.kolpakov.Market.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.models.Property;
import ru.kolpakov.Market.App.models.Review;
import ru.kolpakov.Market.App.services.CartService;
import ru.kolpakov.Market.App.services.ProductService;
import ru.kolpakov.Market.App.utils.GetPerson;
import ru.kolpakov.Market.SecurityForApp.models.Person;

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
        model.addAttribute("user", GetPerson.returnPersonFromContext());
        return "market/main_page";
    }

    @GetMapping("/product/{id}")
    public String productPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.findProductById(id));
        model.addAttribute("newProperty", new Property());
        model.addAttribute("newReview", new Review());
        model.addAttribute("user",GetPerson.returnPersonFromContext());
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
        model.addAttribute("user",GetPerson.returnPersonFromContext());
        return "market/main_page";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
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
        model.addAttribute("cart", cartService.findById(id));
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

    @PostMapping("/product/{id_product}/add_property")
    public String addProperty(@PathVariable("id_product") int idProduct, @ModelAttribute("newProperty") Property property) {
        productService.addPropertyToProduct(idProduct,property);

        return "redirect:/market/product/{id_product}";
    }
    @PostMapping("/product/{id_product}/delete_property")
    public String deleteProperty(@PathVariable("id_product") int idProduct, @RequestParam int id) {
        productService.deletePropertyFromProduct(id, idProduct);

        return "redirect:/market/product/{id_product}";
    }
    @PostMapping("/product/{id_product}/add_review")
    public String addReview(@PathVariable("id_product") int idProduct, @ModelAttribute("newReview") Review review) {
        productService.addReviewToProduct(idProduct,review);

        return "redirect:/market/product/{id_product}";
    }
    @PostMapping("/product/{id_product}/delete_review")
    public String deleteReview(@PathVariable("id_product") int idProduct, @RequestParam("id") int id,
                               @RequestParam("login") String login) {
        productService.deleteReviewFromProduct(idProduct, id, login);

        return "redirect:/market/product/{id_product}";
    }


}
