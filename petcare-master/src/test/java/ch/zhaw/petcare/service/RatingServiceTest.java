package ch.zhaw.petcare.service;

import ch.zhaw.petcare.model.Accommodation;
import ch.zhaw.petcare.model.Rating;
import ch.zhaw.petcare.model.RatingCreateDTO;
import ch.zhaw.petcare.repository.AccommodationRepository;
import ch.zhaw.petcare.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RatingServiceTest {

    @Mock
    RatingRepository ratingRepository;

    @Mock
    AccommodationRepository accommodationRepository;

    @InjectMocks
    RatingService ratingService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAndAssignRating() {
        RatingCreateDTO ratingCreateDTO = new RatingCreateDTO();
        ratingCreateDTO.setRatingValue(5);
        ratingCreateDTO.setComment("Great!");
        ratingCreateDTO.setAccommodationId("acc1");

        Rating rating = new Rating();
        rating.setRatingValue(ratingCreateDTO.getRatingValue());
        rating.setComment(ratingCreateDTO.getComment());

        Accommodation accommodation = new Accommodation();
        accommodation.setId("acc1");

        when(accommodationRepository.findById(ratingCreateDTO.getAccommodationId()))
                .thenReturn(Optional.of(accommodation));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(i -> {
            Rating savedRating = (Rating) i.getArguments()[0];
            savedRating.setId("rating1");
            return savedRating;
        });

        Rating result = ratingService.createAndAssignRating(ratingCreateDTO);

        assertEquals(rating.getRatingValue(), result.getRatingValue());
        assertEquals(rating.getComment(), result.getComment());
        verify(accommodationRepository, times(1)).save(accommodation);
    }

    @Test
    public void testCreateAndAssignRatingWhenAccommodationNotFound() {
        RatingCreateDTO ratingCreateDTO = new RatingCreateDTO();
        ratingCreateDTO.setAccommodationId("acc1");

        when(accommodationRepository.findById(ratingCreateDTO.getAccommodationId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ratingService.createAndAssignRating(ratingCreateDTO));
    }
}
