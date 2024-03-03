package ch.zhaw.petcare.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import ch.zhaw.petcare.model.enums.Gender;
import ch.zhaw.petcare.model.enums.Size;
import ch.zhaw.petcare.model.enums.Species;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Document("pet")
public class Pet {
    @Id
    private String id;

    @NonNull
    private String name;
    @NonNull
    private Date birthdate;
    @NonNull
    private Gender gender;
    @NonNull
    private Species species;
    @NonNull
    private Size size;
    @NonNull
    private String description;
}
