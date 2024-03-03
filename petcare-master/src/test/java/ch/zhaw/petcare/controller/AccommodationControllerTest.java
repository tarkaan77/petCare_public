package ch.zhaw.petcare.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ch.zhaw.petcare.model.Accommodation;
import ch.zhaw.petcare.model.Rating;
import ch.zhaw.petcare.repository.AccommodationRepository;
import ch.zhaw.petcare.security.TestSecurityConfig;

@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AccommodationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    AccommodationRepository accommodationRepository;

    private static final String TEST_STRING = "TEST-abc...xyz";
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    @Test
    @Order(1)
    @WithMockUser
    public void testCreateAccommodation() throws Exception {
        Accommodation accommodation = new Accommodation();
        accommodation.setName(TEST_STRING);
        accommodation.setAddress(TEST_STRING);
        accommodation.setEmail(TEST_STRING);
        accommodation.setPhoneNumber(TEST_STRING);
        accommodation.setIban(TEST_STRING);
        accommodation.setCapacity(5);
        accommodation.setPrice(50);
        var jsonBody = ow.writeValueAsString(accommodation);

        MvcResult result = mvc.perform(post("/api/accommodation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
                
        //del acc
        String response = result.getResponse().getContentAsString();
        Accommodation responseAccommodation = mapper.readValue(response, Accommodation.class);
        accommodationRepository.delete(responseAccommodation);
    }

    @Test
    @Order(2)
    @WithMockUser
    public void getAllAccommodation() throws Exception {
        var json = getAllAccommodations();

        assertFalse(json.isEmpty());
        assertTrue(json.contains("TestAccommodation"));
    }

    @Test
    @Order(3)
    @WithMockUser
    public void getAccommodationId() throws Exception {
        Accommodation accommodation2 = new Accommodation();
        accommodation2.setName(TEST_STRING);
        accommodation2.setAddress(TEST_STRING);
        accommodation2.setEmail(TEST_STRING);
        accommodation2.setPhoneNumber(TEST_STRING);
        accommodation2.setIban(TEST_STRING);
        accommodation2.setCapacity(5);
        accommodation2.setPrice(50);
        accommodation2.setId(TEST_STRING);

        Accommodation savedAccommodation = accommodationRepository.save(accommodation2);
        String accommodationId = savedAccommodation.getId();

        mvc.perform(get("/api/accommodation/{id}", accommodationId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_STRING))
                .andReturn();

        accommodationRepository.delete(savedAccommodation);
    }

    @Test
    @Order(4)
    @WithMockUser
    public void getAccommodationRatings() throws Exception {
        Accommodation accommodation = new Accommodation();
        accommodation.setId("1");
        accommodation.setName("Test Accommodation");
        Rating rating = new Rating();
        rating.setRatingValue(5);
        Rating rating2 = new Rating();
        rating2.setRatingValue(4);
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        ratings.add(rating2);
        accommodation.setRatings(ratings);

        Accommodation savedAccommodation = accommodationRepository.save(accommodation);

        String expectedJsonResponse = ow.writeValueAsString(ratings);

        mvc.perform(get("/api/accommodation/{id}/ratings", savedAccommodation.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonResponse));

        accommodationRepository.delete(savedAccommodation);
    }

    @Test
    @Order(5)
    @WithMockUser
    public void testCountRatings() throws Exception {
        Accommodation accommodation = new Accommodation();
        accommodation.setId("1");
        accommodation.setName("Test Accommodation");
        Rating rating = new Rating();
        rating.setRatingValue(5);
        Rating rating2 = new Rating();
        rating2.setRatingValue(4);
        Rating rating3 = new Rating();
        rating3.setRatingValue(3);
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        ratings.add(rating2);
        ratings.add(rating3);
        accommodation.setRatings(ratings);

        Accommodation savedAccommodation = accommodationRepository.save(accommodation);

        int expectedCount = 3;

        mvc.perform(get("/api/accommodation/{id}/ratings/count", savedAccommodation.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expectedCount));

        accommodationRepository.delete(savedAccommodation);
    }

    @Test
    @Order(6)
    @WithMockUser
    public void testGetTopAccommodations() throws Exception {
        // Create and save accommodations with different ratings
        Accommodation accommodation1 = new Accommodation();
        Accommodation accommodation2 = new Accommodation();
        Accommodation accommodation3 = new Accommodation();
        Accommodation accommodation4 = new Accommodation();
        Accommodation accommodation5 = new Accommodation();
        accommodation1.setRating(5.0);
        accommodation2.setRating(4.9);
        accommodation3.setRating(4.9);
        accommodation4.setRating(4.0);
        accommodation5.setRating(2.5);
        accommodation1.setId("1");
        accommodation2.setId("2");
        accommodation3.setId("3");
        accommodation4.setId("4");
        accommodation5.setId("5");
        accommodationRepository.save(accommodation1);
        accommodationRepository.save(accommodation2);
        accommodationRepository.save(accommodation3);
        accommodationRepository.save(accommodation4);
        accommodationRepository.save(accommodation5);

        mvc.perform(get("/api/accommodation/top")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                //check order of returned
                .andExpect(jsonPath("$[0].id", is(accommodation1.getId())))
                .andExpect(jsonPath("$[1].id", is(accommodation3.getId())))
                .andExpect(jsonPath("$[2].id", is(accommodation2.getId())));

        // Cdelete
        accommodationRepository.delete(accommodation1);
        accommodationRepository.delete(accommodation2);
        accommodationRepository.delete(accommodation3);
        accommodationRepository.delete(accommodation4);
        accommodationRepository.delete(accommodation5);
    }

    private String getAllAccommodations() throws Exception {
        var result = mvc.perform(get("/api/accommodation")
                .param("pageSize", String.valueOf(Integer.MAX_VALUE))
                .contentType(MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

}
