package ch.zhaw.petcare.model;

import java.util.Date;

import ch.zhaw.petcare.model.enums.Gender;
import ch.zhaw.petcare.model.enums.Size;
import ch.zhaw.petcare.model.enums.Species;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PetCreateDTO {
    private String name;
    private Date birthdate;
    private Gender gender;
    private Species species;
    private Size size;
    private String ownerId;
    private String description;
}
