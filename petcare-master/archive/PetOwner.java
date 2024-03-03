package ch.zhaw.petcare.model.archive;
/*package ch.zhaw.petcare.model;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.petcare.model.enums.PersonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PetOwner extends Person {
    private List<Pet> pets = new ArrayList<>();
    //private PersonType personType = PersonType.PET_OWNER;

    public PetOwner(String name, String address, String email, String phoneNumber, List<Pet> pets) {
        super(name, address, email, phoneNumber);
        //initizalize the pets list altough it has no value -> can't be null
        this.pets = pets != null ? pets : new ArrayList<>();

    }

    public PetOwner(Person person) {
        super(person.getName(), person.getAddress(), person.getEmail(), person.getPhoneNumber());
        this.setPersonType(PersonType.PET_OWNER);
    }

}
*/