package model;

import system.Animal;
import system.AnimalCondition;
import system.AnimalShelter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataInitializer {
    private static final DataInitializer instance = new DataInitializer();
    private final ObservableList<AnimalShelter> allShelters = FXCollections.observableArrayList();
    private final ObservableList<Animal> allAnimals = FXCollections.observableArrayList();

    private DataInitializer() {
        initializeData();
    }

    public static DataInitializer getInstance() {
        return instance;
    }

    public ObservableList<AnimalShelter> getAllShelters() {
        return allShelters;
    }

    public ObservableList<Animal> getAllAnimals() {
        return allAnimals;
    }

    private void initializeData() {
        Animal animal1 = new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 5, 100.0, 25.0);
        Animal animal2 = new Animal("Mia", "Cat", AnimalCondition.HEALTHY, 3, 200.0, 4.0);
        Animal animal3 = new Animal("Burek", "Dog", AnimalCondition.ADOPTED, 2, 100.0, 30.0);
        Animal animal4 = new Animal("Filemon", "Cat", AnimalCondition.IN_ADOPTION, 4, 150.0, 5.0);
        Animal animal5 = new Animal("Azor", "Dog", AnimalCondition.QUARANTINE, 1, 300.0, 20.0);

        AnimalShelter shelter1 = new AnimalShelter("Daisy", 50);
        AnimalShelter shelter2 = new AnimalShelter("Cloud", 30);

        shelter1.addAnimal(animal1);
        shelter1.addAnimal(animal2);
        shelter2.addAnimal(animal3);
        shelter1.addAnimal(animal4);
        shelter2.addAnimal(animal5);

        allShelters.addAll(shelter1, shelter2);
        allAnimals.addAll(animal1, animal2, animal3, animal4, animal5);
    }
}
