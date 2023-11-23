package ru.kolpakov.Market.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolpakov.Market.App.models.Image;
import ru.kolpakov.Market.App.repositories.ImagesRepository;

import java.io.ByteArrayInputStream;

@RestController
public class ImageController {
    private final ImagesRepository imagesRepository;

    @Autowired
    public ImageController(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }


    @GetMapping("/market/images/{id}")
    private ResponseEntity<?> getImageById(@PathVariable int id) {
        Image image = imagesRepository.findById(id).get();

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

}