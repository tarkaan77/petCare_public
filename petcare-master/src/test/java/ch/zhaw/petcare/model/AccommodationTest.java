package ch.zhaw.petcare.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
//import static org.mockito.Mockito.mock;

public class AccommodationTest {

    Accommodation accommodation = new Accommodation();

    @BeforeEach
    public void setUp() {
        accommodation.setName("TestAccommodation");
        accommodation.setAddress("Teststrasse 3, 9545 Testhausen");
        accommodation.setEmail("test@test.com");
        accommodation.setPhoneNumber("0791234567");
        accommodation.setIban("CH12 3456 7890 1234 5678 9");
        accommodation.setCapacity(10);
        accommodation.setPrice(50);
    }

    @Test
    public void checkRatingValueWithNoRating() {
        assertEquals(0.0, accommodation.getRating());
    }

    @Test
    public void checkRatingValueWithOneRating() {
        Rating rating = new Rating();
        rating.setRatingValue(5);
        accommodation.addRating(rating);
        assertEquals(5.0, accommodation.getRating());
    }

    @Test
    public void checkRatingValueWithTwoRatings() {
        Rating rating1 = new Rating();
        rating1.setRatingValue(5);
        accommodation.addRating(rating1);
        Rating rating2 = new Rating();
        rating2.setRatingValue(3);
        accommodation.addRating(rating2);
        assertEquals(4.0, accommodation.getRating());
    }

    @Test
    public void checkRatingValueWithMultipleRatings() {
        Rating rating1 = new Rating();
        rating1.setRatingValue(5);
        accommodation.addRating(rating1);
        Rating rating2 = new Rating();
        rating2.setRatingValue(3);
        accommodation.addRating(rating2);
        Rating rating3 = new Rating();
        rating3.setRatingValue(4);
        accommodation.addRating(rating3);
        assertEquals(4.0, accommodation.getRating());
    }

    @Test
    public void checkAverageRatingWithOneRating() {
        Rating rating = new Rating();
        rating.setRatingValue(5);
        accommodation.addRating(rating);
        assertEquals(5.0, accommodation.getRating());
    }

    @Test
    public void checkAverageRatingWithNoRating() {
        accommodation.updateAverageRating();
        assertEquals(0.0, accommodation.getRating());
    }

    @Test
    public void checkRoundingBehavior() {
        double value = 3.456;
        double roundedValue = accommodation.roundToTwoDecimalPlaces(value);
        assertEquals(3.46, roundedValue);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    public void testRatingAverageWithDifferentRatings(int ratingValue) {
        Rating rating = new Rating();
        rating.setRatingValue(ratingValue);
        accommodation.addRating(rating);

        double expectedAverage = ratingValue;
        assertEquals(expectedAverage, accommodation.getRating());
    }


    @Test
    public void testAverageRatingValueWithMultipleRatings() {
        Accommodation accommodation2 = new Accommodation();

        for(int i = 0; i < 5; i++) {
            Rating rating = new Rating();
            rating.setRatingValue(i+1);
            accommodation2.addRating(rating);
        }

        double expectedAverage = 3;
        assertEquals(expectedAverage, accommodation2.getRating());
    }

    @Test
    public void testAverageRatingValueWithMultipleRatings2 () {
        Accommodation accommodation2 = new Accommodation();

        int [] ratings = {3, 3, 4, 5, 5};

        for (int i = 0; i < 5; i++) {
            Rating rating = new Rating();
            rating.setRatingValue(ratings[i]);
            accommodation2.addRating(rating);
        }

        double expectedAverage = 4;
        assertEquals(expectedAverage, accommodation2.getRating());
    }

    @Test
    public void testAverageRatingValueWithMultipleRatings3() {
        Accommodation accommodation2 = new Accommodation();

        int[] ratings = { 2, 2, 5, 5, 1 };

        for (int i = 0; i < 5; i++) {
            Rating rating = new Rating();
            rating.setRatingValue(ratings[i]);
            accommodation2.addRating(rating);
        }

        double expectedAverage = 3;
        assertEquals(expectedAverage, accommodation2.getRating());
    }



}
