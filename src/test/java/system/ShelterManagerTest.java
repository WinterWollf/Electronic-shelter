package system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

public class ShelterManagerTest {

    private ShelterManager shelterManager;

    @BeforeEach
    public void setUp() {
        shelterManager = new ShelterManager(new HashMap<>());
    }

    @Test
    public void testAddShelter() {
        shelterManager.addShelter("Shelter1", 10);
        assertEquals(1, shelterManager.getShelters().size());
        assertTrue(shelterManager.getShelters().containsKey("Shelter1"));
    }

    @Test
    public void testAddShelter_AlreadyExists() {
        shelterManager.addShelter("Shelter1", 10);
        shelterManager.addShelter("Shelter1", 15);
        assertEquals(1, shelterManager.getShelters().size());
    }

    @Test
    public void testRemoveShelter() {
        shelterManager.addShelter("Shelter1", 10);
        shelterManager.removeShelter("Shelter1");
        assertEquals(0, shelterManager.getShelters().size());
    }

    @Test
    public void testRemoveShelter_NotFound() {
        shelterManager.addShelter("Shelter1", 10);
        shelterManager.removeShelter("Shelter2");
        assertEquals(1, shelterManager.getShelters().size());
    }

    @Test
    public void testFindEmptyShelters() {
        shelterManager.addShelter("Shelter1", 10);
        shelterManager.addShelter("Shelter2", 15);
        shelterManager.addShelter("Shelter3", 20);

        Animal animal = new Animal("Burek", "Dog", AnimalCondition.HEALTHY, 3, 300.0, 5.0);
        AnimalShelter shelter1 = shelterManager.getShelters().get("Shelter1");
        shelter1.addAnimal(animal);

        var emptyShelters = shelterManager.findEmpty();
        assertEquals(2, emptyShelters.size());
    }

    @Test
    public void testSummary() {
        shelterManager.addShelter("Shelter1", 10);
        shelterManager.addShelter("Shelter2", 20);
        Animal animal = new Animal("Burek", "Dog", AnimalCondition.HEALTHY, 3, 300.0, 5.0);
        AnimalShelter shelter1 = shelterManager.getShelters().get("Shelter1");
        shelter1.addAnimal(animal);
        shelterManager.summary();
    }

    @Test
    public void testAddAnimalToShelter() {
        shelterManager.addShelter("Shelter1", 10);
        Animal animal = new Animal("Burek", "Dog", AnimalCondition.HEALTHY, 3, 300.0, 5.0);
        AnimalShelter shelter = shelterManager.getShelters().get("Shelter1");
        shelter.addAnimal(animal);
        assertEquals(1, shelter.getAnimalList().size());
        assertTrue(shelter.getAnimalList().contains(animal));
    }

    @Test
    public void testSetShelters() {
        Map<String, AnimalShelter> newShelters = new HashMap<>();
        AnimalShelter shelter1 = new AnimalShelter("Shelter1", 10);
        newShelters.put("Shelter1", shelter1);

        shelterManager.setShelters(newShelters);

        assertEquals(1, shelterManager.getShelters().size());
        assertTrue(shelterManager.getShelters().containsKey("Shelter1"));
        assertEquals(shelter1, shelterManager.getShelters().get("Shelter1"));
    }
}
