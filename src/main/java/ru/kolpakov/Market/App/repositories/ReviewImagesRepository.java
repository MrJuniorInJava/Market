package ru.kolpakov.Market.App.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolpakov.Market.App.models.ReviewImage;

@Repository
public interface ReviewImagesRepository extends JpaRepository<ReviewImage,Integer> {
}
