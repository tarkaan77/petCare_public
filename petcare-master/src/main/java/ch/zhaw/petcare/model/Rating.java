package ch.zhaw.petcare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Document("rating")
public class Rating {
    @Id 
    private String id;

    @NonNull
    private String accommodationId;
    @NonNull
    private int ratingValue;
    private String comment;

    public void setRatingValue(int ratingValue) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.ratingValue = ratingValue;
    }
    
}
