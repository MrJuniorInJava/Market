package ru.kolpakov.Market.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.models.Property;
import ru.kolpakov.Market.App.models.Review;
import ru.kolpakov.Market.App.services.CartService;
import ru.kolpakov.Market.App.services.ProductService;
import ru.kolpakov.Market.App.utils.GetPerson;
import ru.kolpakov.Market.SecurityForApp.models.Person;
import ru.kolpakov.Market.SecurityForApp.services.PersonDetailsService;
import ru.kolpakov.Market.SecurityForApp.utils.IncorrectUserIsDoingSomethingWithObjectException;

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
                           Model model) {
        model.addAttribute("user", personDetailsService.findUserById(id));
        return "market/user_page";
    }

    @GetMapping("/product/{id}")
    public String productPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.findProductById(id));
        model.addAttribute("newProperty", new Property());
        model.addAttribute("newReview", new Review());
        model.addAttribute("user", GetPerson.returnPersonFromContext());
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
        model.addAttribute("user", GetPerson.returnPersonFromContext());
        return "market/main_page";
    }


    @GetMapping("/add_product")
    public String addProductPage(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("searchProducts", Collections.emptyList());
        return "market/add_product";
    }

    @PostMapping("/add_product")
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam("files") List<MultipartFile> files) {
        productService.addProduct(product, files);
        return "redirect:/market";
    }

    @PostMapping("/delete_product")
    public String deleteProduct(@RequestParam("id") int id) {
        productService.deleteProduct(id);
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
        productService.addPropertyToProduct(idProduct, property);

        return "redirect:/market/product/{id_product}";
    }

    @PostMapping("/product/{id_product}/delete_property")
    public String deleteProperty(@PathVariable("id_product") int idProduct, @RequestParam int id) {
        productService.deletePropertyFromProduct(idProduct, id);

        return "redirect:/market/product/{id_product}";
    }

    @PostMapping("/product/{id_product}/add_review")
    public String addReview(@PathVariable("id_product") int idProduct,
                            @ModelAttribute("newReview") Review review,
                            @RequestParam("files") List<MultipartFile> files) {
        productService.addReviewToProduct(idProduct, review, files);

        return "redirect:/market/product/{id_product}";
    }

    @PostMapping("/product/{id_product}/delete_review")
    public String deleteReview(@PathVariable("id_product") int idProduct, @RequestParam("id") int id) {
        productService.deleteReviewFromProduct(idProduct, id);

        return "redirect:/market/product/{id_product}";
    }

    @PostMapping("/product/{id_product}/add_image")
    public String addImageToProduct(@PathVariable("id_product") int idProduct, @RequestParam("file") MultipartFile file) {
        Person owner = productService.findProductById(idProduct).getOwner();
        productService.addImageToProduct(idProduct, file, owner);

        return "redirect:/market/product/{id_product}";
    }

    @DeleteMapping("/product/{id_product}/delete_image/{id_image}")
    @ResponseBody
    public String deleteImageToProduct(@PathVariable("id_product") int idProduct, @PathVariable("id_image") String idImage) {
        Person owner = productService.findProductById(idProduct).getOwner();
        productService.deleteImageFromProduct(idProduct, Integer.parseInt(idImage), owner);

        return "Удаление прошло успешно";
    }

}
