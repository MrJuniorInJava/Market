package ru.kolpakov.Market.App.utils;

import org.springframework.web.multipart.MultipartFile;
import ru.kolpakov.Market.App.models.ProductImage;
import ru.kolpakov.Market.App.models.ReviewImage;

import java.io.IOException;

public class ReturnImage {
    public static ProductImage productImageToImageEntity(MultipartFile file) {
        ProductImage productImage = new ProductImage();
        productImage.setName(file.getName());
        productImage.setOriginalFileName(file.getOriginalFilename());
        productImage.setContentType(file.getContentType());
        productImage.setSize(file.getSize());
        try {
            productImage.setBytes(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return productImage;
    }
    public static ReviewImage reviewImageToImageEntity(MultipartFile file) {
        ReviewImage reviewImage = new ReviewImage();
        reviewImage.setName(file.getName());
        reviewImage.setOriginalFileName(file.getOriginalFilename());
        reviewImage.setContentType(file.getContentType());
        reviewImage.setSize(file.getSize());
        try {
            reviewImage.setBytes(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return reviewImage;
    }
}
