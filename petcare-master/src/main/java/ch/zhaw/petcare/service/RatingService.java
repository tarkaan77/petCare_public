package ch.zhaw.petcare.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ch.zhaw.petcare.model.Accommodation;
import ch.zhaw.petcare.model.Rating;
import ch.zhaw.petcare.model.RatingCreateDTO;
import ch.zhaw.petcare.repository.AccommodationRepository;
import ch.zhaw.petcare.repository.RatingRepository;

@Service
public class RatingService {
    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    public Rating createAndAssignRating(RatingCreateDTO ratingCreateDTO) {
        Rating rating = new Rating();
        rating.setRatingValue(ratingCreateDTO.getRatingValue());
        rating.setComment(ratingCreateDTO.getComment());

        //check accomodation and assign it
        Optional<Accommodation> accommodation = accommodationRepository.findById(ratingCreateDTO.getAccommodationId());
        if (accommodation.isPresent()) {
            Accommodation acc = accommodation.get();
            rating.setAccommodationId(acc.getId());

            Rating savedRating = ratingRepository.save(rating);
            rating.setId(savedRating.getId());
            acc.addRating(rating);
            accommodationRepository.save(acc);
            return savedRating;
        } else {
            throw new RuntimeException("Accomodation not found");
        }
    }
}
