package ch.zhaw.petcare.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ch.zhaw.petcare.model.Person;

public interface PersonRepository extends MongoRepository<Person, String> {
    Person findFirstByEmail(String email);
}
