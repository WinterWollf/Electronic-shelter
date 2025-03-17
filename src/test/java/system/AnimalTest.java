package system;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Nested
    class ConstructorAndGetterTests {
        @Test
        void testAnimalConstructorAndGetters() {
            Animal animal = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            assertEquals("Milo", animal.getName());
            assertEquals("Dog", animal.getSpecies());
            assertEquals(AnimalCondition.HEALTHY, animal.getCondition());
            assertEquals(5, animal.getAge());
            assertEquals(300.0, animal.getPrice());
            assertEquals(20.5, animal.getWeight());
        }
    }

    @Nested
    class SetterAndGettersTests {
        @Test
        void testSetName() {
            Animal animal = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            animal.setName("Buddy");
            assertEquals("Buddy", animal.getName());
        }

        @Test
        void testSetSpecies() {
            Animal animal = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            animal.setSpecies("Cat");
            assertEquals("Cat", animal.getSpecies());
        }

        @Test
        void testSetCondition() {
            Animal animal = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            animal.setCondition(AnimalCondition.SICK);
            assertEquals(AnimalCondition.SICK, animal.getCondition());
        }

        @Test
        void testSetAge() {
            Animal animal = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            animal.setAge(6);
            assertEquals(6, animal.getAge());
        }

        @Test
        void testSetPrice() {
            Animal animal = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            animal.setPrice(350.0);
            assertEquals(350.0, animal.getPrice());
        }

        @Test
        void testSetWeight() {
            Animal animal = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            animal.setWeight(22.0);
            assertEquals(22.0, animal.getWeight());
        }
    }

    @Nested
    class CompareToTests {
        @Test
        void testCompareToSameNameSpeciesAndAge() {
            Animal animal1 = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            Animal animal2 = new Animal("Milo", "Dog", AnimalCondition.SICK, 5, 250.0, 18.0);
            assertEquals(0, animal1.compareTo(animal2));
        }

        @Test
        void testCompareToDifferentName() {
            Animal animal1 = new Animal("Buddy", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            Animal animal2 = new Animal("Milo", "Dog", AnimalCondition.SICK, 5, 250.0, 18.0);
            assertTrue(animal1.compareTo(animal2) < 0);
        }

        @Test
        void testCompareToSameNameDifferentSpecies() {
            Animal animal1 = new Animal("Milo", "Cat", AnimalCondition.HEALTHY, 5, 300.0, 20.5);
            Animal animal2 = new Animal("Milo", "Dog", AnimalCondition.SICK, 5, 250.0, 18.0);
            assertTrue(animal1.compareTo(animal2) < 0);
        }

        @Test
        void testCompareToSameNameAndSpeciesDifferentAge() {
            Animal animal1 = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 3, 300.0, 20.5);
            Animal animal2 = new Animal("Milo", "Dog", AnimalCondition.SICK, 5, 250.0, 18.0);
            assertTrue(animal1.compareTo(animal2) < 0);
        }
    }

    @Nested
    class PrintMethodTests {
        @Test
        void testPrintMethod() {
            Animal animal = new Animal("Milo", "Dog", AnimalCondition.HEALTHY, 5, 300.0, 20.5);

            PrintStream originalOut = System.out;
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            animal.print();
            System.setOut(originalOut);

            String expectedOutput = "Name: Milo\nSpecies: Dog\nCondition: HEALTHY\nAge: 5\nPrice: 300.0\nWeight: 20.5\n";

            assertEquals(expectedOutput.trim(), outContent.toString().trim());
        }

    }
}
