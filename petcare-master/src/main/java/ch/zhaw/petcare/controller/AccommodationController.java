package ch.zhaw.petcare.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.petcare.model.Accommodation;
import ch.zhaw.petcare.model.AccommodationCreateDTO;
import ch.zhaw.petcare.model.Rating;
import ch.zhaw.petcare.repository.AccommodationRepository;

@RestController
@RequestMapping("/api")
public class AccommodationController {

    @Autowired
    AccommodationRepository accommodationRepository;

    @PostMapping("/accommodation")
    @Secured("ROLE_admin")
    public ResponseEntity<Accommodation> createAccomodation(
            @RequestBody AccommodationCreateDTO aDTO) {
        Accommodation aDAO = new Accommodation(aDTO.getName(), aDTO.getAddress(), aDTO.getEmail(),
                aDTO.getPhoneNumber(), aDTO.getIban(), aDTO.getCapacity(), aDTO.getPrice());
                aDAO.setImageUrls(aDTO.getImageUrls());
        Accommodation a = accommodationRepository.save(aDAO);
        return new ResponseEntity<>(a, HttpStatus.CREATED);
    }

    @GetMapping("/accommodation")
    public ResponseEntity<Page<Accommodation>> getAllAccommodations(
    @RequestParam(required = false) Double price,
    @RequestParam(required = false) Integer capacity,
    @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
    @RequestParam(required = false, defaultValue = "4") Integer pageSize) {
        Page <Accommodation> accommodations;
        if (price == null && capacity == null) {
            accommodations = accommodationRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        } else {   
            if (price != null && capacity != null) {
                accommodations = accommodationRepository.findByPriceLessThanEqualAndCapacityGreaterThan(price, capacity, PageRequest.of(pageNumber - 1, pageSize));
            } else if (price == null && capacity != null) {
                accommodations = accommodationRepository.findByCapacityGreaterThan(capacity, PageRequest.of(pageNumber - 1, pageSize));
            } else {
                accommodations = accommodationRepository.findByPriceLessThanEqual(price, PageRequest.of(pageNumber - 1, pageSize));
            }
        } 
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }

    @GetMapping("/accommodation/{id}")
    @Secured({ "ROLE_admin", "ROLE_petOwner", "ROLE_animalRightsActivist"})
    public ResponseEntity<Accommodation> getAccomodationById(@PathVariable String id) {
        Optional<Accommodation> optAccommodation = accommodationRepository.findById(id);
        if (optAccommodation.isPresent()) {
            return new ResponseEntity<>(optAccommodation.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/accommodation/{id}/ratings")
    @Secured({ "ROLE_admin", "ROLE_petOwner", "ROLE_animalRightsActivist" })
    public ResponseEntity<List<Rating>> getRatings(@PathVariable String id) {
        Optional<Accommodation> optAccommodation = accommodationRepository.findById(id);
        if (optAccommodation.isPresent()) {
            return new ResponseEntity<>(optAccommodation.get().getRatings(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/accommodation/{id}/ratings/count")
    @Secured({ "ROLE_admin", "ROLE_petOwner", "ROLE_animalRightsActivist" })
    public ResponseEntity<Integer> countRatings(@PathVariable String id) {
        Optional<Accommodation> optAccommodation = accommodationRepository.findById(id);
        if (optAccommodation.isPresent()) {
            List<Rating> ratings = optAccommodation.get().getRatings();
            if (ratings != null) {
                return new ResponseEntity<>(ratings.size(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(0, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/accommodation/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<Accommodation> deleteAccommodation(@PathVariable String id) {
        Optional<Accommodation> optAccommodation = accommodationRepository.findById(id);
        if (optAccommodation.isPresent()) {
            accommodationRepository.deleteById(id);
            return new ResponseEntity<>(optAccommodation.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/accommodation/top")
    public ResponseEntity<List<Accommodation>> getTopAccommodations() {
        List<Accommodation> accommodations = accommodationRepository.findTop3ByOrderByRatingDesc();
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }

}
