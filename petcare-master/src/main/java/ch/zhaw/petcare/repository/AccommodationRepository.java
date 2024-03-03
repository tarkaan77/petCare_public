package ch.zhaw.petcare.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.petcare.model.Accommodation;

public interface AccommodationRepository extends MongoRepository<Accommodation, String>{
    Page<Accommodation> findByPriceLessThanEqual(Double price, Pageable pageable);
    Page<Accommodation> findByCapacityGreaterThan(int capacity, Pageable pageable);
    Page<Accommodation> findByPriceLessThanEqualAndCapacityGreaterThan(Double price, int capacity, Pageable pageable);

    List<Accommodation> findTop3ByOrderByRatingDesc();

}
