package system;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ShelterManager {
    private Map<String, AnimalShelter> shelters;

    // Constructor
    public ShelterManager(Map<String, AnimalShelter> schroniska) {
        this.shelters = new HashMap<>();
    }

    // Methods
    public void addShelter(String name, int capacity) {
        if (!shelters.containsKey(name)) {
            shelters.put(name, new AnimalShelter(name, capacity));
            System.out.println("A new shelter has been added: " + name);
        } else {
            System.err.println("A shelter with this name already exists");
        }
    }

    public void removeShelter(String name) {
        if (shelters.remove(name) != null) {
            System.out.println("The shelter has been removed: " + name);
        } else {
            System.out.println("No shelter named found: " + name);
        }
    }

    public List<AnimalShelter> findEmpty() {
        List<AnimalShelter> emptyShelters = new ArrayList<>();
        for (AnimalShelter shelter : shelters.values()) {
            if (shelter.getAnimalList().isEmpty()) {
                emptyShelters.add(shelter);
            }
        }
        return emptyShelters;
    }

    public void summary() {
        for (Map.Entry<String, AnimalShelter> entry : shelters.entrySet()) {
            String shelterName = entry.getKey();
            AnimalShelter shelter = entry.getValue();
            int totalCapacity = shelter.getMaxCapacity();
            int currentAnimals = shelter.getAnimalList().size();
            double occupancyRate = (currentAnimals / (double) totalCapacity) * 100;

            System.out.println("Shelter: " + shelterName);
            System.out.printf("Filling: %.2f%%\n", occupancyRate);
        }
    }

    // Getters and Setters
    public Map<String, AnimalShelter> getShelters() {
        return shelters;
    }

    public void setShelters(Map<String, AnimalShelter> shelters) {
        this.shelters = shelters;
    }
}
