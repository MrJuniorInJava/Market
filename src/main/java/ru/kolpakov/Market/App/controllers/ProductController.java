package ru.kolpakov.Market.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.services.ProductService;

import java.util.Collections;

@Controller
@RequestMapping("/market")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public String mainPage(Model model) {
        model.addAttribute("products", productService.findAll());
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


}
