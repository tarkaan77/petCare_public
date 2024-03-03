package ch.zhaw.petcare.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.zhaw.petcare.model.enums.PersonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

public class PersonTest {

    public Person person;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setName("Test Person");
        person.setAddress("Teststrasse 3, 9545 Teststrasse 3");
        person.setEmail("test@test.com");
        person.setPhoneNumber("0791234567");
    }

    @Test
    public void checkPetsForPetOwner() {
        person.setPersonType(PersonType.PET_OWNER);
        Pet pet1 = mock(Pet.class);
        Pet pet2 = mock(Pet.class);
        List<Pet> pets = Arrays.asList(pet1, pet2);
        person.setPets(pets);

        assertEquals(PersonType.PET_OWNER, person.getPersonType());
        assertEquals(pets, person.getPets());
        assertNull(person.getAssociationName());
    }

    @Test
    public void checkPetsForAnimalRightsActivist() {
        person.setPersonType(PersonType.ANIMAL_RIGHTS_ACTIVIST);
        person.setAssociationName("Test Association");

        assertEquals(PersonType.ANIMAL_RIGHTS_ACTIVIST, person.getPersonType());
        assertNull(person.getPets());
        assertEquals("Test Association", person.getAssociationName());
    }

    @Test
    public void checkPetsForNotSetPersonType() {
        person.setPersonType(PersonType.NOT_SET);

        assertEquals(PersonType.NOT_SET, person.getPersonType());
        assertNull(person.getPets());
        assertNull(person.getAssociationName());
    }

    @Test
    public void checkNonNullFields() {
        assertThrows(NullPointerException.class, () -> person.setName(null));
        assertThrows(NullPointerException.class, () -> person.setAddress(null));
        assertThrows(NullPointerException.class, () -> person.setEmail(null));
        assertThrows(NullPointerException.class, () -> person.setPhoneNumber(null));
    }

    @Test
    public void testMockPerson() {
        Person mockPerson = mock(Person.class);
        when(mockPerson.getName()).thenReturn("Mocked Person");
        when(mockPerson.getAddress()).thenReturn("Mockstrasse 3, 9545 Mockhausen");

        assertEquals("Mocked Person", mockPerson.getName());
        assertEquals("Mockstrasse 3, 9545 Mockhausen", mockPerson.getAddress());
    }
}
