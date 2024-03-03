/*package ch.zhaw.petcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.petcare.model.AccommodationRequest;
import ch.zhaw.petcare.model.AccommodationRequestCreateDTO;
import ch.zhaw.petcare.repository.AccommodationRequestRepository;

@RestController
@RequestMapping("/api")
public class AccommodationRequestController {

    @Autowired
    AccommodationRequestRepository accommodationRequestRepository;

    @PostMapping("/request-accommodation")
    public ResponseEntity<AccommodationRequest> createAccommodationRequest(
            @RequestBody AccommodationRequestCreateDTO arDTO) {
        AccommodationRequest arDAO = new AccommodationRequest(arDTO.getName(), arDTO.getAddress(), arDTO.getEmail(),
                arDTO.getPhoneNumber(), arDTO.getWebsiteURL(), arDTO.getIban(), arDTO.getCapacity(), arDTO.getPrice(),
                arDTO.getImageUrls(), arDTO.getApplicationText());
        AccommodationRequest ar = accommodationRequestRepository.save(arDAO);
        return new ResponseEntity<>(ar, HttpStatus.CREATED);
    }

    @GetMapping("/accommodation-requests")
    public ResponseEntity<List<AccommodationRequest>> getAllAccommodationRequests() {
        List<AccommodationRequest> accommodationRequests = accommodationRequestRepository.findAll();
        return new ResponseEntity<>(accommodationRequests, HttpStatus.OK);
    }

    
}
*/