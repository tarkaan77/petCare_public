package ch.zhaw.petcare.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.petcare.model.Accommodation;
import ch.zhaw.petcare.model.Booking;
import ch.zhaw.petcare.model.BookingCreateDTO;
import ch.zhaw.petcare.model.Pet;
import ch.zhaw.petcare.model.enums.BookingStatus;
import ch.zhaw.petcare.repository.AccommodationRepository;
import ch.zhaw.petcare.repository.BookingRepository;
import ch.zhaw.petcare.repository.PetRepository;

public class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    AccommodationRepository accommodationRepository;

    @Mock
    PetRepository petRepository;

    @InjectMocks
    BookingService bookingService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAndAssignBooking() {
        BookingCreateDTO bookingCreateDTO = new BookingCreateDTO("acc1", "pet1", "person1", new Date(), new Date(),
                "Rex", BookingStatus.PENDING);

        Accommodation accommodation = new Accommodation();
        Pet pet = new Pet();

        Booking booking = new Booking();
        booking.setAccommodationId(bookingCreateDTO.getAccommodationId());
        booking.setPetId(bookingCreateDTO.getPetId());
        booking.setPersonId(bookingCreateDTO.getPersonId());
        booking.setStartDate(bookingCreateDTO.getStartDate());
        booking.setEndDate(bookingCreateDTO.getEndDate());
        booking.setStatus(bookingCreateDTO.getStatus());

        when(accommodationRepository.findById(bookingCreateDTO.getAccommodationId()))
                .thenReturn(Optional.of(accommodation));
        when(petRepository.findById(bookingCreateDTO.getPetId())).thenReturn(Optional.of(pet));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createAndAssignBooking(bookingCreateDTO);

        assertEquals(booking, result);
    }

    @Test
    public void testCreateAndAssignBookingWhenAccommodationNotFound() {
        BookingCreateDTO bookingCreateDTO = new BookingCreateDTO("acc1", "pet1", null, null, null, null, null);

        when(accommodationRepository.findById(bookingCreateDTO.getAccommodationId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.createAndAssignBooking(bookingCreateDTO));
    }

    @Test
    public void testGetBookingsByPersonId() {
        String personId = "person1";
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());

        when(bookingRepository.findByPersonId(personId)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByPersonId(personId);

        assertEquals(bookings, result);
    }

    @Test
    public void testCancelBooking() {
        String bookingId = "booking1";
        Booking booking = new Booking();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(bookingId);

        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    public void testCancelBookingWhenBookingNotFound() {
        String bookingId = "booking1";

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.cancelBooking(bookingId));
    }

    @Test
    public void testUpdateBookingStatus() {
        String bookingId = "booking1";
        Booking booking = new Booking();
        BookingStatus newStatus = BookingStatus.IN_PROGRESS;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        Booking result = bookingService.updateBookingStatus(bookingId, newStatus);

        assertEquals(newStatus, result.getStatus());
    }

    @Test
    public void testUpdateBookingStatusWhenBookingNotFound() {
        String bookingId = "booking1";
        BookingStatus newStatus = BookingStatus.IN_PROGRESS;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.updateBookingStatus(bookingId, newStatus));
    }

    @Test
    public void testGetAllBookings() {
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());

        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(bookings, result);
    }

}