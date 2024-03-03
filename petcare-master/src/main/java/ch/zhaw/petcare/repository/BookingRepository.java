package ch.zhaw.petcare.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.data.mongodb.repository.MongoRepository;
import ch.zhaw.petcare.model.Booking;
import ch.zhaw.petcare.model.enums.BookingStatus;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByPersonId (String personId);
    void deleteById(String bookingId);
    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);
}
