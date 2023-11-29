package ru.kolpakov.Market.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolpakov.Market.App.models.ProductImage;
import ru.kolpakov.Market.App.repositories.ProductImagesRepository;

import java.io.ByteArrayInputStream;

@RestController
public class ProductImageController {
    private final ProductImagesRepository productImagesRepository;

    @Autowired
    public ProductImageController(ProductImagesRepository productImagesRepository) {
        this.productImagesRepository = productImagesRepository;
    }


    @GetMapping("/market/product_images/{id}")
    private ResponseEntity<?> getImageById(@PathVariable int id) {
        ProductImage productImage = productImagesRepository.findById(id).get();

        return ResponseEntity.ok()
                .header("fileName", productImage.getOriginalFileName())
                .contentType(MediaType.valueOf(productImage.getContentType()))
                .contentLength(productImage.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(productImage.getBytes())));
    }

}