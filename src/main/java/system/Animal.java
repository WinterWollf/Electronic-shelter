package system;

public class Animal implements Comparable<Animal> {
    private String name;
    private String species;
    private AnimalCondition condition;
    private int age;
    private double price;
    private double weight;

    // Constructor
    public Animal(String name, String species, AnimalCondition condition, int age, double price, double weight) {
        this.name = name;
        this.species = species;
        this.condition = condition;
        this.age = age;
        this.price = price;
        this.weight = weight;
    }

    // Methods
    public void print() {
        System.out.println("Name: " + name + "\nSpecies: " + species + "\nCondition: " + condition + "\nAge: " + age + "\nPrice: " + price + "\nWeight: " + weight);
    }

    @Override
    public int compareTo(Animal other) {
        int result = this.name.compareTo(other.name);
        if (result == 0) {
            result = this.species.compareTo(other.species);
            if (result == 0) {
                result = this.age - other.age;
            }
        }
        return result;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public AnimalCondition getCondition() {
        return condition;
    }

    public void setCondition(AnimalCondition condition) {
        this.condition = condition;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
