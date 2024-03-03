package ch.zhaw.petcare.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RatingTest {

    Rating rating = new Rating();

    @BeforeEach
    public void setUp() {
        rating.setAccommodationId("TestAccommodationId");
        rating.setRatingValue(5);
        rating.setComment("Great accommodation!");
    }

    @Test
    public void checkRatingValue() {
        assertEquals(5, rating.getRatingValue());
    }

    @Test
    public void checkAccommodationId() {
        assertEquals("TestAccommodationId", rating.getAccommodationId());
    }

    @Test
    public void checkNullComment() {
        rating.setComment(null);
        assertNull(rating.getComment());
    }

    @Test
    public void checkNonNullFields() {
        assertThrows(NullPointerException.class, () -> rating.setAccommodationId(null));
        assertThrows(IllegalArgumentException.class, () -> rating.setRatingValue(-1));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    public void testValidRatingValue(int value) {
        rating.setRatingValue(value);
        assertEquals(value, rating.getRatingValue());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 6, 7, -1, -5, 10 })
    public void testInvalidRatingValue(int value) {
        assertThrows(IllegalArgumentException.class, () -> rating.setRatingValue(value));
    }

    
}
