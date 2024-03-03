package ch.zhaw.petcare.model;

import java.util.List;

import ch.zhaw.petcare.model.enums.PersonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PersonCreateDTO {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private PersonType personType;

    //for pet owners
    private List <Pet> pets;

    //for animal rights activists
    private String associationName;
}
