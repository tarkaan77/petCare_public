package ch.zhaw.petcare.model;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import ch.zhaw.petcare.model.enums.BookingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Document("booking")
public class Booking {
    @Id
    private String id;

    @NonNull
    private String accommodationId;
    @NonNull
    private String personId;
    @NonNull
    private String petId;
    @NonNull
    private Date startDate;
    @NonNull
    private Date endDate;
    private String petName;
    private BookingStatus status = BookingStatus.PENDING;

    
}
