package ru.kolpakov.Market.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.models.Property;
import ru.kolpakov.Market.App.models.Review;
import ru.kolpakov.Market.App.services.CartService;
import ru.kolpakov.Market.App.services.ProductService;
import ru.kolpakov.Market.App.utils.GetPerson;
import ru.kolpakov.Market.SecurityForApp.services.PersonDetailsService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/market")
public class MainController {
    private final ProductService productService;
    private final CartService cartService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public MainController(ProductService productService, CartService cartService, PersonDetailsService personDetailsService) {
        this.productService = productService;
        this.cartService = cartService;
        this.personDetailsService = personDetailsService;
    }
    @GetMapping()
    public String mainPage(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("user", GetPerson.returnPersonFromContext());
        return "market/main_page";
    }

    @GetMapping("user/{id}")
    public String userPage(@PathVariable("id") int id,
                           Model model){
        model.addAttribute("user",personDetailsService.findUserById(id));
        return "market/user_page";
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
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam("files") List<MultipartFile> files) {
        productService.addProduct(product,files);
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
    @PostMapping("/product/{id_product}/add_image")
    public String addImageToProduct(@PathVariable("id_product") int idProduct, @RequestParam("file") MultipartFile file) {
        productService.addImageToProduct(idProduct,file);

        return "redirect:/market/product/{id_product}";
    }
    @PostMapping("/product/{id_product}/delete_image")
    public String addImageToProduct(@PathVariable("id_product") int idProduct, @RequestParam("id_image") String idImage) {
        productService.deleteImageFromProduct(idProduct,Integer.parseInt(idImage));

        return "redirect:/market/product/{id_product}";
    }


}
