/*package ch.zhaw.petcare.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Document("accommodationRequest")
public class AccommodationRequest {
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
    private String websiteURL;
    @NonNull
    private String iban;
    @NonNull
    private int capacity;
    @NonNull
    private double price;
    private List<String> imageUrls;
    private String applicationText;

    public AccommodationRequest(String name, String address, String email, String phoneNumber, String websiteURL,
            String iban, int capacity, double price, List<String> imageUrls, String applicationText) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.websiteURL = websiteURL;
        this.iban = iban;
        this.capacity = capacity;
        this.price = price;
        this.imageUrls = imageUrls;
        this.applicationText = applicationText;
    }

}*/