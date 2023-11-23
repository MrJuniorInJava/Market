package ru.kolpakov.Market.App.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.kolpakov.Market.App.models.Image;
import ru.kolpakov.Market.App.models.Product;
import ru.kolpakov.Market.App.models.Property;
import ru.kolpakov.Market.App.models.Review;
import ru.kolpakov.Market.App.repositories.ImagesRepository;
import ru.kolpakov.Market.App.repositories.ProductsRepository;
import ru.kolpakov.Market.App.repositories.PropertiesRepository;
import ru.kolpakov.Market.App.repositories.ReviewsRepository;
import ru.kolpakov.Market.App.utils.GetPerson;
import ru.kolpakov.Market.App.utils.ReturnImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.kolpakov.Market.App.utils.ReturnImage.toImageEntity;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductsRepository productsRepository;
    private final PropertiesRepository propertiesRepository;
    private final ReviewsRepository reviewsRepository;
    private final ImagesRepository imagesRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository, PropertiesRepository propertiesRepository, ReviewsRepository reviewsRepository, ImagesRepository imagesRepository) {
        this.productsRepository = productsRepository;
        this.propertiesRepository = propertiesRepository;
        this.reviewsRepository = reviewsRepository;
        this.imagesRepository = imagesRepository;
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
    public void addProduct(Product product, List<MultipartFile> files) {
        Image image = null;
        for (MultipartFile file : files) {
            if (file.getSize() != 0) {
                image = toImageEntity(file);
                product.addImageToProduct(image);
            }
            imagesRepository.save(image);
        }
        product.getImages().get(0).setPreviewImage(true);
        product.setPreviewImageId(0);
        GetPerson.returnPersonFromContext().addProductToPerson(product);
        product.setTime(LocalDateTime.now());
        productsRepository.save(product);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
    public void deleteProductById(int id) {
        productsRepository.deleteById(id);
    }

    public List<Product> searchByFirstChars(String name) {
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        return productsRepository.findAll().stream()
                .filter(object -> pattern.matcher(object.getName()).find())
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
    public void addPropertyToProduct(int idProduct, Property property) {
        propertiesRepository.save(property);
        productsRepository.findById(idProduct).get().addPropertyToProduct(property);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
    public void deletePropertyFromProduct(int id, int idProduct) {
        Property property = propertiesRepository.findById(id).get();
        productsRepository.findById(idProduct).get().getProperties().remove(property);
        propertiesRepository.delete(property);
    }

    @Transactional
    public void addReviewToProduct(int idProduct, Review review) {
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or #login==principal.username")
    public void deleteReviewFromProduct(int idProduct, int id, String login) {
        Product product = productsRepository.findById(idProduct).get();
        product.getReviews().remove(reviewsRepository.findById(id).get());
        reviewsRepository.deleteById(id);
        double sumRating = product.getReviews().stream().mapToDouble(Review::getRating).sum();
        double n = product.getReviews().size();
        double avgRating = sumRating / n;
        product.setAvgRating((Math.round(avgRating * 10)) / 10.0);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
    public void addImageToProduct(int idProduct, MultipartFile file) {
        Product product = productsRepository.findById(idProduct).get();
        Image image = null;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            if(product.getImages().isEmpty()) {
                image.setPreviewImage(true);
            }
            product.addImageToProduct(image);
        }
        imagesRepository.save(image);


    }

}
