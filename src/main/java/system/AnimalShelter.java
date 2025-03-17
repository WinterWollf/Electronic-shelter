package system;

import java.util.*;
import java.util.List;

public class AnimalShelter {
    private String shelterName;
    private List<Animal> animalList;
    private int maxCapacity;
    private double OccupancyRate;

    // Constructor
    public AnimalShelter(String shelterName, int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.animalList = new ArrayList<>();
        this.shelterName = shelterName;
        this.OccupancyRate = 0.0;
    }

    // Methods
    public void addAnimal(Animal animal) {
        if (this.animalList.size() < this.maxCapacity) {
            for (Animal shelteredAniaml : animalList) {
                if (shelteredAniaml.compareTo(animal) == 0) {
                    System.out.println("The same animal is already in the shelter");
                    return;
                }
            }
            animalList.add(animal);
            System.out.println("The animal has been added to the shelter");
            calculateOccupancyRate();
        }
        else {
            System.err.println("There is no room in the shelter for a new animal");
        }
    }

    public void removeAnimal(Animal animal) {
        for (Animal shelteredAniaml : animalList) {
            if (shelteredAniaml.compareTo(animal) == 0) {
                animalList.remove(animal);
                System.out.println("The animal was removed from the shelter");
                calculateOccupancyRate();
                return;
            }
        }
        System.err.println("The animal is not in a shelter");
    }

    public void getAnimal(Animal animal) {
        for (Animal shelteredAniaml : animalList) {
            if (shelteredAniaml.compareTo(animal) == 0) {
                this.changeCondition(animal, AnimalCondition.ADOPTED);
                removeAnimal(animal);
                System.out.println("The animal was adopted");
                calculateOccupancyRate();
                return;
            }
        }
        System.err.println("The animal is not in a shelter");
    }

    public void changeCondition(Animal animal, AnimalCondition condition) {
        for (Animal shelteredAniaml : animalList) {
            if (shelteredAniaml.compareTo(animal) == 0) {
                animal.setCondition(condition);
                System.out.println("The animal's condition has changed");
                return;
            }
        }
        System.err.println("The animal is not in a shelter");
    }

    public void changeAge(Animal animal, int age) {
        for (Animal shelteredAniaml : animalList) {
            if (shelteredAniaml.compareTo(animal) == 0) {
                animal.setAge(age);
            }
        }
        System.err.println("The animal is not in a shelter");
    }

    public int countByCondition(AnimalCondition condition) {
        int counter = 0;
        for(Animal shelteredAniaml : animalList){
            if (shelteredAniaml.getCondition() == condition)
                counter++;
        }
        return counter;
    }

    public List<Animal> sortByName() {
        List<Animal> sortedList = new ArrayList<>(animalList);
        Collections.sort(sortedList);
        return sortedList;
    }

    public List<Animal> sortByPrice() {
        List<Animal> sortedList = new ArrayList<>(animalList);
        Collections.sort(sortedList, new Comparator<Animal>() {
            @Override
            public int compare(Animal o1, Animal o2) {
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
        });
        return sortedList;
    }

    public Animal search(String name) {
        AnimalNameComparator comparator = new AnimalNameComparator();
        for (Animal shelteredAniaml : animalList) {
            if (comparator.compare(shelteredAniaml, new Animal(name, "", AnimalCondition.HEALTHY, 0, 0, 0)) == 0) {
                return shelteredAniaml;
            }
        }
        System.err.println("The animal is not in a shelter");
        return null;
    }

    public List<Animal> searchPartial(String fragment) {
        List<Animal> resultList = new ArrayList<>();
        for (Animal animal : animalList) {
            if (animal.getName().toLowerCase().contains(fragment.toLowerCase()) ||
                    animal.getSpecies().toLowerCase().contains(fragment.toLowerCase())) {
                resultList.add(animal);
            }
        }
        return resultList;
    }

    public Animal max() {
        return Collections.max(this.animalList, new Comparator<Animal>() {
            @Override
            public int compare(Animal o1, Animal o2) {
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
        });
    }

    public void summary() {
        System.out.println("Shelter: " + shelterName);
        System.out.println("Maximum capacity: " + maxCapacity);
        System.out.println("Current number of animals: " + animalList.size());
        System.out.println("\nAnimals in the shelter:");
        for (Animal shelteredAnimal : animalList) {
            shelteredAnimal.print();
            System.out.println();
        }
    }

    public void calculateOccupancyRate() {
        double occupancyRate = (animalList.size() / (double) maxCapacity) * 100;
        this.OccupancyRate = Math.round(occupancyRate * 100.0) / 100.0;
    }

    // Getters and Setters
    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public List<Animal> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(List<Animal> animalList) {
        this.animalList = animalList;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        calculateOccupancyRate();
    }

    public double getOccupancyRate() {
        return OccupancyRate;
    }
}
