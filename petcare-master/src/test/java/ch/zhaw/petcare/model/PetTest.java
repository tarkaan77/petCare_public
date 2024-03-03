package ch.zhaw.petcare.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.zhaw.petcare.model.enums.Gender;
import ch.zhaw.petcare.model.enums.Size;
import ch.zhaw.petcare.model.enums.Species;

public class PetTest {

    public Pet pet;

    @BeforeEach
    public void setUp() {
        pet = new Pet();

        pet.setName("TestPet");
        pet.setBirthdate(new Date());
        pet.setGender(Gender.MALE);
        pet.setSpecies(Species.DOG);
        pet.setSize(Size.LARGE);
        pet.setDescription("Test description");
    }

    @Test
    public void checkName() {
        assertEquals("TestPet", pet.getName());
    }

    @Test
    public void checkGender() {
        assertEquals(Gender.MALE, pet.getGender());
    }

    @Test
    public void checkSpecies() {
        assertEquals(Species.DOG, pet.getSpecies());
    }

    @Test
    public void checkSize() {
        assertEquals(Size.LARGE, pet.getSize());
    }

    @Test
    public void checkDescription() {
        assertEquals("Test description", pet.getDescription());
    }

    @Test
    public void checkNullValues() {
        assertThrows(NullPointerException.class, () -> pet.setName(null));
        assertThrows(NullPointerException.class, () -> pet.setBirthdate(null));
        assertThrows(NullPointerException.class, () -> pet.setGender(null));
        assertThrows(NullPointerException.class, () -> pet.setSpecies(null));
        assertThrows(NullPointerException.class, () -> pet.setSize(null));
        assertThrows(NullPointerException.class, () -> pet.setDescription(null));
    }

}
