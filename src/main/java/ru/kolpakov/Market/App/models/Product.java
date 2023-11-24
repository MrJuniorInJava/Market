package ru.kolpakov.Market.App.models;

import org.springframework.format.annotation.DateTimeFormat;
import ru.kolpakov.Market.SecurityForApp.models.Person;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Product")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "price")
    private long price;
    @Column(name="name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "avg_rating")
    private double avgRating;
    @ManyToMany()
    @JoinTable(name = "cart_product",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "cart_id"))
    private List<Cart> carts = new ArrayList<>();
    @ManyToOne()
    @JoinColumn(name = "person_id",referencedColumnName = "id")
    private Person owner;
    @OneToMany(mappedBy = "product")
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "product",cascade = CascadeType.REMOVE)
    private List<Property> properties = new ArrayList<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<>();
    @Column(name = "preview_image_id")
    private Integer previewImageId;
    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
    private LocalDateTime time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Integer getPreviewImageId() {
        return previewImageId;
    }

    public void setPreviewImageId(Integer previewImageId) {
        this.previewImageId = previewImageId;
    }

    public void setPreviewImageId(int previewImageId) {
        this.previewImageId = previewImageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && price == product.price && Double.compare(avgRating, product.avgRating) == 0 && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(carts, product.carts) && Objects.equals(owner, product.owner) && Objects.equals(reviews, product.reviews) && Objects.equals(properties, product.properties) && Objects.equals(images, product.images) && Objects.equals(previewImageId, product.previewImageId) && Objects.equals(time, product.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, name, description, avgRating, carts, owner, reviews, properties, images, previewImageId, time);
    }
    //Вспомогательные методы

    public void addPropertyToProduct(Property property) {
        this.properties.add(property);
        property.setProduct(this);
    }
    public void addReviewToProduct(Review review) {
        this.reviews.add(review);
        review.setProduct(this);
    }
    public void addImageToProduct(Image image) {
        image.setProduct(this);
        this.images.add(image);
    }
}
