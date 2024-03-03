package ch.zhaw.petcare.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//Data Access Object -> DAO
//DAO has CRUD operations - read and load data from database
//there is some logic in the DAO class
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Document("accommodation")
public class Accommodation {
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
    @NonNull
    private String iban;
    @NonNull
    private int capacity;
    @NonNull
    private double price;
    private double rating;
    private List<String> imageUrls;


    private List<Rating> ratings;

    public void addRating(Rating rating) {
        if (ratings == null) {
            ratings = new ArrayList<>();
        }
        ratings.add(rating);
        updateAverageRating();
    }

    public void updateAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            rating = 0;
            return;
        }
        double sum = 0;
        for (Rating r : ratings) {
            sum += r.getRatingValue();
        }
        rating = roundToTwoDecimalPlaces(sum / ratings.size());
    }

    public double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public double getRating() {
        return roundToTwoDecimalPlaces(rating);
    }

    
}
