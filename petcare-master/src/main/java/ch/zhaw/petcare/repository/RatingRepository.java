package ch.zhaw.petcare.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import ch.zhaw.petcare.model.Rating;

public interface RatingRepository extends MongoRepository<Rating, String> {
    List<Rating> findByAccommodationId(String accommodationId);
}
