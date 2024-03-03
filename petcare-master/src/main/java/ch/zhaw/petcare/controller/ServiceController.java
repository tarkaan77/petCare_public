package ch.zhaw.petcare.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.petcare.model.Booking;
import ch.zhaw.petcare.model.BookingCreateDTO;
import ch.zhaw.petcare.model.Pet;
import ch.zhaw.petcare.model.Rating;
import ch.zhaw.petcare.model.RatingCreateDTO;
import ch.zhaw.petcare.model.enums.BookingStatus;
import ch.zhaw.petcare.repository.BookingRepository;
import ch.zhaw.petcare.repository.PetRepository;
import ch.zhaw.petcare.service.BookingService;
import ch.zhaw.petcare.service.RatingService;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
    @Autowired
    RatingService ratingService;

    @Autowired
    BookingService bookingService;

    @Autowired
    PetRepository petRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Secured({ "ROLE_admin", "ROLE_petOwner" })
    @PostMapping("/rating")
    public ResponseEntity<Rating> createRating(@RequestBody RatingCreateDTO ratingCreateDTO) {
        Rating createdRating = ratingService.createAndAssignRating(ratingCreateDTO);
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }

    @Secured({ "ROLE_admin", "ROLE_petOwner" })
    @PostMapping("/booking")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingCreateDTO bookingCreateDTO) {
        Booking createdBooking = bookingService.createAndAssignBooking(bookingCreateDTO);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }


    @Secured({ "ROLE_admin", "ROLE_petOwner" })
    @GetMapping("/booking/person/{id}")
    public ResponseEntity<Iterable<Booking>> getBookingsByPersonId(@PathVariable("id") String personId) {
        Iterable<Booking> bookings = bookingService.getBookingsByPersonId(personId);

        for (Booking booking : bookings) {
            Optional<Pet> pet = petRepository.findById(booking.getPetId());
            if (pet.isPresent()) {
                booking.setPetName(pet.get().getName());
            }
        }

        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @Secured({ "ROLE_admin", "ROLE_petOwner" })
    @PutMapping("/booking/{id}/status")
    public ResponseEntity<Booking> updateBookingStatus(@PathVariable("id") String bookingId,
            @RequestBody BookingStatus newStatus) {
        Booking updatedBooking = bookingService.updateBookingStatus(bookingId, newStatus);
        return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
    }

    @Secured({ "ROLE_admin", "ROLE_petOwner" })
    @DeleteMapping("/booking/{id}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable("id") String bookingId) {
        bookingService.cancelBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({ "ROLE_admin" })
    @GetMapping("/bookings")
    public ResponseEntity<Page<Booking>> getAllBookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "4") Integer pageSize) {
        Page<Booking> bookings;

        if (status == null) {
            bookings = bookingRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        } else {
            bookings = bookingRepository.findByStatus(status, PageRequest.of(pageNumber - 1, pageSize));
        }

        for (Booking booking : bookings) {
            Optional<Pet> pet = petRepository.findById(booking.getPetId());
            if (pet.isPresent()) {
                booking.setPetName(pet.get().getName());
            }
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

}
