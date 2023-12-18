package ru.kolpakov.Market.App.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.kolpakov.Market.App.models.*;
import ru.kolpakov.Market.App.repositories.*;
import ru.kolpakov.Market.App.utils.GetPerson;
import ru.kolpakov.Market.SecurityForApp.models.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.kolpakov.Market.App.utils.ReturnImage.productImageToImageEntity;
import static ru.kolpakov.Market.App.utils.ReturnImage.reviewImageToImageEntity;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductsRepository productsRepository;
    private final PropertiesRepository propertiesRepository;
    private final ReviewsRepository reviewsRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ReviewImagesRepository reviewImagesRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository, PropertiesRepository propertiesRepository, ReviewsRepository reviewsRepository, ProductImagesRepository productImagesRepository, ReviewImagesRepository reviewImagesRepository) {
        this.productsRepository = productsRepository;
        this.propertiesRepository = propertiesRepository;
        this.reviewsRepository = reviewsRepository;
        this.productImagesRepository = productImagesRepository;
        this.reviewImagesRepository = reviewImagesRepository;
    }

    public List<Product> findAll() {
        return productsRepository.findAll();
    }

    public List<Product> findProductsForPersonCart() {
        return GetPerson.returnPersonFromContext().getCart().getProducts();
    }

    public Product findProductById(int id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Transactional
    public void addProduct(Product product, List<MultipartFile> files) {
        ProductImage productImage = null;
        for (MultipartFile file : files) {
            if (file.getSize() != 0) {
                productImage = productImageToImageEntity(file);
                product.addImageToProduct(productImage);
            }
            productImagesRepository.save(productImage);
        }
        product.getImages().get(0).setPreviewImage(true);
        product.setPreviewImageId(0);
        GetPerson.returnPersonFromContext().addProductToPerson(product);
        product.setTime(LocalDateTime.now());
        productsRepository.save(product);
    }

    @Transactional
    public void deleteProduct(int id) {
        productsRepository.deleteById(id);
    }

    public List<Product> searchByFirstChars(String name) {
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        return productsRepository.findAll().stream()
                .filter(object -> pattern.matcher(object.getName()).find())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateProductField(int id, String fieldName, String newValue) {
        Product currentProduct = productsRepository.findById(id).get();
        switch (fieldName) {
            case "name":
                currentProduct.setName(newValue);
                break;
            case "description":
                currentProduct.setDescription(newValue);
                break;
            case "price":
                currentProduct.setPrice(Integer.parseInt(newValue));
                break;

        }
        productsRepository.save(currentProduct);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_SELLER') and #owner.login==principal.username)")
    public void addPropertyToProduct(int idProduct, Property property, Person owner) {
        propertiesRepository.save(property);
        productsRepository.findById(idProduct).get().addPropertyToProduct(property);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_SELLER') and #owner.login==principal.username)")
    public void deletePropertyFromProduct(int id, int idProduct, Person owner) {
        Property property = propertiesRepository.findById(id).get();
        productsRepository.findById(idProduct).get().getProperties().remove(property);
        propertiesRepository.delete(property);
    }

    @Transactional
    public void addReviewToProduct(int idProduct, Review review, List<MultipartFile> files) {
        ReviewImage reviewImage = null;
        for (MultipartFile file : files) {
            if (file.getSize() != 0) {
                reviewImage = reviewImageToImageEntity(file);
                review.addImageToReview(reviewImage);
                reviewImagesRepository.save(reviewImage);
            }
        }
        Product product = productsRepository.findById(idProduct).get();
        review.setCreatedAt(LocalDateTime.now());
        review.setOwner(GetPerson.returnPersonFromContext());
        reviewsRepository.save(review);
        product.addReviewToProduct(review);
        double sumRating = product.getReviews().stream().mapToDouble(Review::getRating).sum();
        double n = product.getReviews().size();
        double avgRating = sumRating / n;
        product.setAvgRating((Math.round(avgRating * 10)) / 10.0);
    }

    @Transactional
    public void deleteReviewFromProduct(int idProduct, int id) {
        Product product = productsRepository.findById(idProduct).get();
        product.getReviews().remove(reviewsRepository.findById(id).get());
        reviewsRepository.deleteById(id);
        double sumRating = product.getReviews().stream().mapToDouble(Review::getRating).sum();
        double n = product.getReviews().size();
        double avgRating = sumRating / n;
        product.setAvgRating((Math.round(avgRating * 10)) / 10.0);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or #owner.login==principal.username")
    public void addImageToProduct(int idProduct, MultipartFile file, Person owner) {
        Product product = productsRepository.findById(idProduct).get();
        ProductImage productImage = null;
        if (file.getSize() != 0) {
            productImage = productImageToImageEntity(file);
            if (product.getImages().isEmpty()) {
                productImage.setPreviewImage(true);
                product.setPreviewImageId(productImage.getId());
            }
            product.addImageToProduct(productImage);
            productImagesRepository.save(productImage);
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or #owner.login==principal.username")
    public void deleteImageFromProduct(int idProduct, int idImage, Person owner) {
        Product product = productsRepository.findById(idProduct).get();
        ProductImage productImage = productImagesRepository.findById(idImage).get();
        if (productImage.equals(product.getImages().get(product.getPreviewImageId()))) {
            if (product.getImages().size() != 1) {
                product.setPreviewImageId(product.getImages()
                        .indexOf(product.getImages()
                                .stream().findFirst().get()));
            }
        }
        product.getImages().remove(productImage);
        productImagesRepository.delete(productImage);
    }

}
