package ru.kolpakov.Market.App.utils;

import org.springframework.web.multipart.MultipartFile;
import ru.kolpakov.Market.App.models.Image;

import java.io.IOException;

public class ReturnImage {
    public static Image toImageEntity(MultipartFile file) {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        try {
            image.setBytes(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }
}
