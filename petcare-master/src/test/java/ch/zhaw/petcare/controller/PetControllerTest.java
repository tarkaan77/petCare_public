package ch.zhaw.petcare.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import ch.zhaw.petcare.model.Pet;
import ch.zhaw.petcare.model.enums.Gender;
import ch.zhaw.petcare.model.enums.Size;
import ch.zhaw.petcare.model.enums.Species;
import ch.zhaw.petcare.repository.PetRepository;
import ch.zhaw.petcare.security.TestSecurityConfig;

@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    PetRepository petRepository;

    private static final String TEST_STRING = "TEST-abc...xyz";
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    Pet testPet;
    
    @BeforeEach
    void setUp() {
        testPet = new Pet();
        testPet.setName(TEST_STRING);
        testPet.setBirthdate(new Date());
        testPet.setGender(Gender.MALE);
        testPet.setSpecies(Species.DOG);
        testPet.setSize(Size.SMALL);
        testPet.setDescription(TEST_STRING);
        testPet.setId("petId");
        petRepository.save(testPet);
    }

    @AfterEach
    void tearDown() {
        petRepository.delete(testPet);
    }

    @Test
    @Order(1)
    @WithMockUser
    public void testCreatePet() throws Exception {
        Pet newPet = new Pet();
        newPet.setName("New Pet");
        newPet.setBirthdate(new Date());
        newPet.setGender(Gender.FEMALE);
        newPet.setSpecies(Species.CAT);
        newPet.setSize(Size.MEDIUM);
        newPet.setDescription("This is a new pet for testing");
        newPet.setId("newPetId");
        var jsonBody = ow.writeValueAsString(newPet);

        // POST Json to service with authorization header
        MvcResult result = mvc.perform(post("/api/pet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        // Delete the new pet
        String response = result.getResponse().getContentAsString();
        Pet responsePet = mapper.readValue(response, Pet.class);
        petRepository.delete(responsePet);
    }

    @Test
    @Order(2)
    @WithMockUser(roles = "admin")
    public void testGetAllPet() throws Exception {
        var json = getAllPets();

        // assert list of pets is not empty and result contains test string
        assertFalse(json.isEmpty());
        assertTrue(json.contains(TEST_STRING));
    }

    @Test
    @Order(3)
    @WithMockUser
    public void testGetPetById() throws Exception {
        String petId = testPet.getId();

        mvc.perform(get("/api/pet/{id}", petId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_STRING))
                .andReturn();
    }

    @Test
    @Order(4)
    @WithMockUser
    public void testDeletePetById() throws Exception {
        String petId = testPet.getId();

        Optional<Pet> optPet = petRepository.findById(petId);
        assertTrue(optPet.isPresent());

        mvc.perform(delete("/api/pet/{id}", petId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Check if the pet still exists
        optPet = petRepository.findById(petId);
        assertFalse(optPet.isPresent());
    }

    private String getAllPets() throws Exception {
        var result = mvc.perform(get("/api/pet")
                .contentType(MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }
}
