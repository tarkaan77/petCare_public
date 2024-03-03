package ch.zhaw.petcare.model;

import java.util.Date;

import ch.zhaw.petcare.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDTO {
    private String accommodationId;
    private String personId;
    private String petId;
    private Date startDate;
    private Date endDate;
    private String petName;
    private BookingStatus status;

    
}
