package exceptions;

public class AnimalNotSelectedException extends Exception {
    public AnimalNotSelectedException() {
        super("No animal was selected.");
    }

    public AnimalNotSelectedException(String message) {
        super(message);
    }
}
