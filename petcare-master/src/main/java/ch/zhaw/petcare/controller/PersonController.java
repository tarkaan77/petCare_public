package ch.zhaw.petcare.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import ch.zhaw.petcare.model.AnimalRightsActivist;
import ch.zhaw.petcare.model.Person;
import ch.zhaw.petcare.model.PersonCreateDTO;
import ch.zhaw.petcare.model.Pet;
//import ch.zhaw.petcare.model.PetOwner;
//import ch.zhaw.petcare.model.PetOwnerUpdateDTO;
import ch.zhaw.petcare.model.enums.PersonType;

import ch.zhaw.petcare.repository.PersonRepository;
import ch.zhaw.petcare.repository.PetRepository;

@RestController
@RequestMapping("/api")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PetRepository petRepository;

    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@RequestBody PersonCreateDTO pDTO) {
        Person person = new Person();

        person.setName(pDTO.getName());
        person.setAddress(pDTO.getAddress());
        person.setEmail(pDTO.getEmail());
        person.setPhoneNumber(pDTO.getPhoneNumber());

        Person p = personRepository.save(person);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @GetMapping("/person")
    @Secured("ROLE_admin")
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable String id) {
        Optional<Person> optPerson = personRepository.findById(id);
        if (optPerson.isPresent()) {
            return new ResponseEntity<>(optPerson.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/person/petowner/{ownerId}/assignpet/{petId}")
    @Secured({ "ROLE_admin", "ROLE_petOwner" })
    public ResponseEntity<Person> assignPetToOwner(
            @PathVariable String ownerId,
            @PathVariable String petId) {

        Optional<Person> optionalOwner = personRepository.findById(ownerId);
        Optional<Pet> optionalPet = petRepository.findById(petId);

        if (optionalOwner.isPresent() && optionalPet.isPresent()) {
            Person owner = optionalOwner.get();
            if (owner.getPersonType() == PersonType.PET_OWNER) {
                Pet pet = optionalPet.get();
                owner.getPets().add(pet);
                Person updatedOwner = personRepository.save(owner);

                return new ResponseEntity<>(updatedOwner, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/person/petowner")
    public ResponseEntity<List<Person>> getAllPetOwners() {
        List<Person> persons = personRepository.findAll();
        List<Person> petOwners = new ArrayList<>();
        for (Person p : persons) {
            if (p.getPersonType() == PersonType.PET_OWNER) {
                petOwners.add(p);
            }
        }
        return new ResponseEntity<>(petOwners, HttpStatus.OK);
    }

    @GetMapping("/person/petowner/{id}")
    public ResponseEntity<Person> getPetOwnerById(@PathVariable String id) {
        Optional<Person> optPerson = personRepository.findById(id);
        if (optPerson.isPresent() && optPerson.get().getPersonType() == PersonType.PET_OWNER) {
            return new ResponseEntity<>(optPerson.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/person/animalrightsactivist")
    public ResponseEntity<List<Person>> getAllAnimalRightsActivists() {
        List<Person> persons = personRepository.findAll();
        List<Person> animalRightsActivists = new ArrayList<>();
        for (Person p : persons) {
            if (p.getPersonType() == PersonType.ANIMAL_RIGHTS_ACTIVIST) {
                animalRightsActivists.add(p);
            }
        }
        return new ResponseEntity<>(animalRightsActivists, HttpStatus.OK);
    }

    @GetMapping("/person/animalrightsactivist/{id}")
    public ResponseEntity<Person> getAnimalRightsActivistById(@PathVariable String id) {
        Optional<Person> optPerson = personRepository.findById(id);
        if (optPerson.isPresent() && optPerson.get().getPersonType() == PersonType.ANIMAL_RIGHTS_ACTIVIST) {
            return new ResponseEntity<>(optPerson.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/person/petowner/{id}/pets")
    @Secured({ "ROLE_admin", "ROLE_petOwner" })
    public ResponseEntity<List<Pet>> getPetsByPetOwnerId(@PathVariable String id) {
        Optional<Person> optPerson = personRepository.findById(id);
        if (optPerson.isPresent()) {
            Person person = optPerson.get();
            if (person.getPersonType() == PersonType.PET_OWNER) {
                return new ResponseEntity<>(person.getPets(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/person/email/{email}")
    public ResponseEntity<Person> getPersonByEmail(@PathVariable String email) {
        Person person = personRepository.findFirstByEmail(email);
        if (person != null) {
            return new ResponseEntity<>(person, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/person/petowner/{ownerId}/deletepet/{petId}")
    @Secured({ "ROLE_admin", "ROLE_petOwner" })
    public ResponseEntity<Person> deletePetFromOwner(
            @PathVariable String ownerId,
            @PathVariable String petId) {

        Optional<Person> optionalOwner = personRepository.findById(ownerId);

        if (optionalOwner.isPresent()) {
            Person owner = optionalOwner.get();
            if (owner.getPersonType() == PersonType.PET_OWNER) {
                if (owner.getPets().stream().anyMatch(pet -> pet.getId().equals(petId))) {
                    owner.getPets().removeIf(pet -> pet.getId().equals(petId));
                    personRepository.save(owner); // Update the owner without the pet
                    petRepository.deleteById(petId); // Delete the pet from the pet repository
                    return new ResponseEntity<>(owner, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("person/{id}/setPersonType/{personType}")
    public ResponseEntity<Person> setPersonType(
            @PathVariable String id,
            @PathVariable String personType) {

        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();

            if (personType.equalsIgnoreCase("petowner")) {
                person.setPersonType(PersonType.PET_OWNER);
            } else if (personType.equalsIgnoreCase("animalrightsactivist")) {
                person.setPersonType(PersonType.ANIMAL_RIGHTS_ACTIVIST);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Person updatedPerson = personRepository.save(person);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/person/{id}/update")
    public ResponseEntity<Person> updatePersonAttributes(
            @PathVariable String id,
            @RequestBody PersonCreateDTO personDTO) {

        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();

            // Update common attributes
            person.setName(personDTO.getName());
            person.setAddress(personDTO.getAddress());
            person.setPhoneNumber(personDTO.getPhoneNumber());

            // If the person is a pet owner, initialize the pets list
            if (person.getPersonType() == PersonType.PET_OWNER) {
                person.setPets(new ArrayList<>());
                person.setAssociationName(null);
            }
            // If the person is an animal rights activist, set the association name
            else if (person.getPersonType() == PersonType.ANIMAL_RIGHTS_ACTIVIST) {
                person.setAssociationName(personDTO.getAssociationName());
                person.setPets(null);
            }

            Person updatedPerson = personRepository.save(person);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
