package ch.zhaw.petcare.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccommodationCreateDTO {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private String iban;
    private int capacity;
    private double price;
    private List <String> imageUrls;
}
