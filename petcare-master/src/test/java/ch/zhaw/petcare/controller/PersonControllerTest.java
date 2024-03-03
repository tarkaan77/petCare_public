package ch.zhaw.petcare.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import ch.zhaw.petcare.model.Person;
import ch.zhaw.petcare.model.PersonCreateDTO;
import ch.zhaw.petcare.model.enums.PersonType;
import ch.zhaw.petcare.repository.PersonRepository;
import ch.zhaw.petcare.repository.PetRepository;
import ch.zhaw.petcare.security.TestSecurityConfig;

@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PetRepository petRepository;

    private static final String TEST_STRING = "TEST-abc...xyz";
    private static final String TEST_MAIL = "test@mail.com";
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    private Person testPerson;

    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setName(TEST_STRING);
        testPerson.setAddress(TEST_STRING);
        testPerson.setEmail(TEST_MAIL);
        testPerson.setPhoneNumber(TEST_STRING);
        testPerson.setId("personId");
        personRepository.save(testPerson);
    }

    @AfterEach
    void tearDown() {
        personRepository.delete(testPerson);
    }

    @Test
    @Order(1)
    @WithMockUser
    public void testCreatePerson() throws Exception {
        Person person = new Person();
        person.setName(TEST_STRING);
        person.setAddress(TEST_STRING);
        person.setEmail(TEST_MAIL);
        person.setPhoneNumber(TEST_STRING);
        assertEquals(person.getPersonType(), PersonType.NOT_SET);
        var jsonBody = ow.writeValueAsString(person);

        MvcResult result = mvc.perform(post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        Person responsePerson = mapper.readValue(response, Person.class);
        personRepository.delete(responsePerson);
    }

    @Test
    @Order(2)
    @WithMockUser(roles = "admin")
    public void testGetAllPersons() throws Exception {
        var json = getAllPersons();

        assertFalse(json.isEmpty());
        assertTrue(json.contains(TEST_STRING));
    }

    @Test
    @Order(3)
    @WithMockUser
    public void testGetPersonById() throws Exception {
        String personId = testPerson.getId();

        mvc.perform(get("/api/person/{id}", personId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.name").value("TEST-abc...xyz"))
                .andReturn();
    }

    @Test
    @Order(4)
    @WithMockUser
    public void testGetPersonByEmail() throws Exception {
        String personEmail = testPerson.getEmail();

        mvc.perform(get("/api/person/email/{email}", personEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andReturn();
    }

    @Test
    @Order(6)
    @WithMockUser
    public void testSetPersonType() throws Exception {
        String personId = testPerson.getId();

        mvc.perform(put("/api/person/{id}/setPersonType/{personType}", personId, "petowner")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personType").value(PersonType.PET_OWNER.toString()))
                .andReturn();
    }

    @Test
    @Order(6)
    @WithMockUser
    public void testSetPersonType2() throws Exception {
        String personId = testPerson.getId();

        mvc.perform(put("/api/person/{id}/setPersonType/{personType}", personId, "animalRightsActivist")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personType").value(PersonType.ANIMAL_RIGHTS_ACTIVIST.toString()))
                .andReturn();
    }

    @Test
    @Order(7)
    @WithMockUser
    public void testUpdatePersonAttributes() throws Exception {

        String personId = testPerson.getId();

        // Create the PersonCreateDTO with updated values
        PersonCreateDTO updatedPersonDTO = new PersonCreateDTO();
        updatedPersonDTO.setName("Updated Name");
        updatedPersonDTO.setAddress("Updated Address");
        updatedPersonDTO.setPhoneNumber("Updated Phone Number");

        var jsonBody = ow.writeValueAsString(updatedPersonDTO);

        //change values of personn
        mvc.perform(put("/api/person/{id}/update", personId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedPersonDTO.getName()))
                .andExpect(jsonPath("$.address").value(updatedPersonDTO.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedPersonDTO.getPhoneNumber()))
                .andReturn();
    }




    private String getAllPersons() throws Exception {
        var result = mvc.perform(get("/api/person")
                .contentType(MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }




}
