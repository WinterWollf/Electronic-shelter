package system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class AnimalShelterTest {
    private AnimalShelter shelter;
    private Animal animal1;
    private Animal animal2;

    @BeforeEach
    void setUp() {
        shelter = new AnimalShelter("Shelter A", 5);
        animal1 = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
        animal2 = new Animal("Buddy", "Cat", AnimalCondition.SICK, 3, 150.0, 8.0);
    }

    @Test
    void testAddAnimal() {
        shelter.addAnimal(animal1);
        assertTrue(shelter.getAnimalList().contains(animal1), "The animal should be added to the shelter");
    }

    @Test
    void testAddAnimalExceedsCapacity() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);
        shelter.addAnimal(new Animal("Charlie", "Dog", AnimalCondition.HEALTHY, 2, 200.0, 10.0));
        shelter.addAnimal(new Animal("Luna", "Cat", AnimalCondition.SICK, 4, 100.0, 6.0));
        shelter.addAnimal(new Animal("Max", "Dog", AnimalCondition.HEALTHY, 6, 250.0, 15.0));

        shelter.addAnimal(new Animal("Bella", "Dog", AnimalCondition.HEALTHY, 4, 220.0, 14.0));

        assertEquals(5, shelter.getAnimalList().size(), "The shelter should have a maximum of 5 animals");
    }

    @Test
    void testRemoveAnimal() {
        shelter.addAnimal(animal1);
        shelter.removeAnimal(animal1);
        assertFalse(shelter.getAnimalList().contains(animal1), "The animal should be removed from the shelter");
    }

    @Test
    void testGetAnimal() {
        shelter.addAnimal(animal1);
        shelter.getAnimal(animal1);
        assertEquals(AnimalCondition.ADOPTED, animal1.getCondition(), "The animal's status should be changed to ADOPTED");
        assertFalse(shelter.getAnimalList().contains(animal1), "The animal should be removed from the shelter after adoption");
    }

    @Test
    void testCountByCondition() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);
        shelter.addAnimal(new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 4, 100.0, 10.0));

        int healthyCount = shelter.countByCondition(AnimalCondition.HEALTHY);
        assertEquals(2, healthyCount, "The shelter should have 2 healthy animals");
    }

    @Test
    void testSortByName() {
        shelter.addAnimal(animal2);
        shelter.addAnimal(animal1);

        List<Animal> sortedList = shelter.sortByName();
        assertEquals("Buddy", sortedList.get(0).getName(), "The first animal in the sorted list should be Buddy");
    }

    @Test
    void testSortByPrice() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);

        List<Animal> sortedList = shelter.sortByPrice();
        assertEquals(150.0, sortedList.get(0).getPrice(), "The first animal in the sorted list should have a price of 150.0");
    }

    @Test
    void testSearch() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);

        Animal foundAnimal = shelter.search("Milo");
        assertNotNull(foundAnimal, "The animal named 'Milo' should be found at a shelter");
        assertEquals(animal1, foundAnimal, "The animal found should be the same one that was added");
    }

    @Test
    void testSearchPartial() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);

        List<Animal> result = shelter.searchPartial("Dog");
        assertEquals(1, result.size(), "There should be one animal with a name that contains 'Dog'");
    }

    @Test
    void testMaxPriceAnimal() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);

        Animal maxPriceAnimal = shelter.max();
        assertEquals(animal1, maxPriceAnimal, "Zwierzę o najwyższej cenie powinno być Milo");
    }

    @Test
    void testSummary() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);
        shelter.summary();
    }

    @Test
    void testOccupancyRateCalculation() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);

        shelter.calculateOccupancyRate();
        assertEquals(40.0, shelter.getOccupancyRate(), "The occupancy rate should be 40.0 after adding 2 animals");
    }

    @Test
    void testChangeCondition() {
        shelter.addAnimal(animal1);
        shelter.changeCondition(animal1, AnimalCondition.SICK);
        assertEquals(AnimalCondition.SICK, animal1.getCondition(), "The animal's condition should be changed to SICK");
    }

    @Test
    void testChangeAge() {
        shelter.addAnimal(animal1);
        shelter.changeAge(animal1, 6);
        assertEquals(6, animal1.getAge(), "The age of the animal should be changed to 6");
    }

    @Test
    void testRemoveAnimalNotFound() {
        shelter.addAnimal(animal1);
        Animal animal3 = new Animal("Rocky", "Dog", AnimalCondition.HEALTHY, 4, 180.0, 12.0);
        shelter.removeAnimal(animal3);
        assertEquals(1, shelter.getAnimalList().size(), "The animal should not be deleted because it does not exist");
    }

    @Test
    void testMaxPriceAnimalNotEmpty() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);

        Animal maxPriceAnimal = shelter.max();
        assertNotNull(maxPriceAnimal, "The highest priced animal should be found");
        assertEquals(animal1, maxPriceAnimal, "The highest priced animal should be Milo");
    }

    @Test
    void testOccupancyRateAfterRemovingAnimal() {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);
        shelter.calculateOccupancyRate();
        assertEquals(40.0, shelter.getOccupancyRate(), "The occupancy rate should be 40.0 after adding 2 animals");

        shelter.removeAnimal(animal1);
        shelter.calculateOccupancyRate();
        assertEquals(20.0, shelter.getOccupancyRate(), "The occupancy rate should be 20.0 after removing 1 animal");
    }

}