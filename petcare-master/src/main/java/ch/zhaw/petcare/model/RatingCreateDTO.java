package ch.zhaw.petcare.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RatingCreateDTO {
    private String accommodationId;

    @Min(value = 1, message = "Rating value must be between 1 and 5")
    @Max(value = 5, message = "Rating value must be between 1 and 5")
    private int ratingValue;

    @Size(max = 1000, message = "Comment must be less than or equal to 1000 characters")
    private String comment;
}
