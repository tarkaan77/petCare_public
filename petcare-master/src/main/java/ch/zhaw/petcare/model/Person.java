package ch.zhaw.petcare.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import ch.zhaw.petcare.model.enums.PersonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document("person")
public class Person {
    @Id
    private String id;
    
    @NonNull
    private String name;
    @NonNull
    private String address;
    @NonNull
    private String email;
    @NonNull
    private String phoneNumber;
    private PersonType personType = PersonType.NOT_SET;

    //for PetOwner
    private List<Pet> pets;

    //for AnimalRightsActivist
    private String associationName;

}
