package ch.zhaw.petcare.model.archive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PetOwnerUpdateDTO {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;

}
