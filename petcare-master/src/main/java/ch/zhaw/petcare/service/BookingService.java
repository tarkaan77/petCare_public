package ch.zhaw.petcare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.petcare.model.Accommodation;
import ch.zhaw.petcare.model.Booking;
import ch.zhaw.petcare.model.BookingCreateDTO;
import ch.zhaw.petcare.model.Pet;
import ch.zhaw.petcare.model.enums.BookingStatus;
import ch.zhaw.petcare.repository.AccommodationRepository;
import ch.zhaw.petcare.repository.BookingRepository;
import ch.zhaw.petcare.repository.PetRepository;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    PetRepository petRepository;

    public Booking createAndAssignBooking(BookingCreateDTO bookingCreateDTO) {
        Optional<Accommodation> accommodation = accommodationRepository.findById(bookingCreateDTO.getAccommodationId());
        Optional<Pet> pet = petRepository.findById(bookingCreateDTO.getPetId());

        if (accommodation.isPresent() && pet.isPresent()) {
            Booking booking = new Booking();

            booking.setAccommodationId(bookingCreateDTO.getAccommodationId());
            booking.setPersonId(bookingCreateDTO.getPersonId());
            booking.setPetId(bookingCreateDTO.getPetId());
            booking.setStartDate(bookingCreateDTO.getStartDate());
            booking.setEndDate(bookingCreateDTO.getEndDate());

            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Accommodation or Pet not found");
        }
    }

    public List<Booking> getBookingsByPersonId(String personId) {
        return bookingRepository.findByPersonId(personId);
    }

    public void cancelBooking(String bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (booking.isPresent()) {
            bookingRepository.deleteById(bookingId);
        } else {
            throw new RuntimeException("Booking not found");
        }
    }

    public Booking updateBookingStatus(String bookingId, BookingStatus newStatus) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(newStatus);
            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Booking not found");
        }
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

}
