package ch.zhaw.petcare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.zhaw.petcare.model.Pet;
import ch.zhaw.petcare.model.PetCreateDTO;
import ch.zhaw.petcare.repository.PetRepository;

@RestController
@RequestMapping("/api")
public class PetController {
    
    @Autowired
    PetRepository petRepository;

    @PostMapping("/pet")
    @Secured({"ROLE_admin", "ROLE_petOwner"})
    public ResponseEntity<Pet> createPet(
            @RequestBody PetCreateDTO pDTO) {
        Pet pDAO = new Pet(pDTO.getName(), pDTO.getBirthdate(), pDTO.getGender(), pDTO.getSpecies(), pDTO.getSize(), pDTO.getDescription());
        Pet p = petRepository.save(pDAO);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @GetMapping("/pet")
    @Secured("ROLE_admin")
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/pet/{id}")
    @Secured({"ROLE_admin", "ROLE_petOwner"})
    public ResponseEntity<Pet> getPetById(@PathVariable String id) {
        Optional<Pet> optPet = petRepository.findById(id);
        if(optPet.isPresent()) {
            return new ResponseEntity<>(optPet.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/pet/{id}")
    @Secured({"ROLE_admin", "ROLE_petOwner"})
    public ResponseEntity<String> deletePetById(@PathVariable String id) {
        Optional<Pet> optPet = petRepository.findById(id);
        if(optPet.isPresent()) {
            petRepository.deleteById(id);
            return new ResponseEntity<>("Pet deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Pet not found", HttpStatus.NOT_FOUND);
        }
    }

}
