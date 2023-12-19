package ru.kolpakov.Market.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolpakov.Market.App.models.ProductImage;
import ru.kolpakov.Market.App.models.ReviewImage;
import ru.kolpakov.Market.App.repositories.ReviewImagesRepository;

import java.io.ByteArrayInputStream;

@RestController
public class ReviewImageController {
    private final ReviewImagesRepository reviewImagesRepository;


    @Autowired
    public ReviewImageController(ReviewImagesRepository reviewImagesRepository) {
        this.reviewImagesRepository = reviewImagesRepository;
    }



    @GetMapping("/market/review_images/{id}")
    private ResponseEntity<?> findImageById(@PathVariable int id) {
        ReviewImage reviewImage = reviewImagesRepository.findById(id).get();

        return ResponseEntity.ok()
                .header("fileName", reviewImage.getOriginalFileName())
                .contentType(MediaType.valueOf(reviewImage.getContentType()))
                .contentLength(reviewImage.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(reviewImage.getBytes())));
    }

}
