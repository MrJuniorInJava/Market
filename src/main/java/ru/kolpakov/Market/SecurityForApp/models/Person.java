package ru.kolpakov.Market.SecurityForApp.models;

import org.springframework.format.annotation.DateTimeFormat;
import ru.kolpakov.Market.App.models.Cart;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.models.Review;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_name")
    @NotEmpty
    @NotNull
    @Size(min = 2, max = 70, message = "Имя должно иметь количество символов от 2 до 70")
    private String username;
    @Column(name = "login")
    @NotEmpty
    @NotNull
    @Size(min = 2, max = 70, message = "Имя должно иметь количество символов от 2 до 70")
    private String login;
    @Column(name = "year_of_birth")
    @Min(value = 1900, message = "Год рождения должен иметь значение выше 1900")
    private int yearOfBirth;
    @Column(name = "password")
    @NotNull
    @NotEmpty
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "date_reg")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfRegistration;
    @OneToOne(mappedBy = "owner")
    private Cart cart;
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Product> products = new HashSet<>();
    @OneToMany(mappedBy = "owner")
    private List<Review> reviews = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDateTime getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDateTime dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    public void addReviewToPerson(Review review){
        review.setOwner(this);
        this.reviews.add(review);
    }

    public void addProductToPerson(Product product){
        product.setOwner(this);
        this.products.add(product);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", login='" + login + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", cart=" + cart +
                ", products=" + products +
                '}';
    }
}
