package ch.zhaw.petcare.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.zhaw.petcare.model.enums.BookingStatus;

public class BookingTest {
    public Booking booking;

    @BeforeEach
    public void setUp() {
        booking = new Booking();
    }

    @Test
    public void checkNonNullFields() {
        assertThrows(NullPointerException.class, () -> booking.setAccommodationId(null));
        assertThrows(NullPointerException.class, () -> booking.setPersonId(null));
        assertThrows(NullPointerException.class, () -> booking.setPetId(null));
        assertThrows(NullPointerException.class, () -> booking.setStartDate(null));
        assertThrows(NullPointerException.class, () -> booking.setEndDate(null));
    }

    @Test
    public void checkStatus() {
        assertEquals(BookingStatus.PENDING, booking.getStatus());
    }

    @Test
    public void checkAndSetStatus() {
        assertEquals(BookingStatus.PENDING, booking.getStatus());

        booking.setStatus(BookingStatus.IN_PROGRESS);
        assertEquals(BookingStatus.IN_PROGRESS, booking.getStatus());

        booking.setStatus(BookingStatus.DONE);
        assertEquals(BookingStatus.DONE, booking.getStatus());
    }

    @Test
    public void checkSetterMethods() {
        String accommodationId = "123";
        booking.setAccommodationId(accommodationId);
        assertEquals(accommodationId, booking.getAccommodationId());

        String personId = "456";
        booking.setPersonId(personId);
        assertEquals(personId, booking.getPersonId());

        String petId = "789";
        booking.setPetId(petId);
        assertEquals(petId, booking.getPetId());

        Date startDate = new Date();
        booking.setStartDate(startDate);
        assertEquals(startDate, booking.getStartDate());

        Date endDate = new Date();
        booking.setEndDate(endDate);
        assertEquals(endDate, booking.getEndDate());
    }


}
