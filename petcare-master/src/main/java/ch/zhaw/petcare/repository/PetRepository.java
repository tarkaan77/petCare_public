package ch.zhaw.petcare.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ch.zhaw.petcare.model.Pet;

public interface PetRepository extends MongoRepository<Pet, String> {
    void deleteById(String petId);
    
}
