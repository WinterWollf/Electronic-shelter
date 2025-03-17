package gui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;
import model.DataInitializer;
import system.Animal;
import system.AnimalCondition;
import system.AnimalShelter;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import exceptions.ShelterNotSelectedException;
import exceptions.AnimalNotSelectedException;
import exceptions.InvalidCapacityException;
import exceptions.InvalidAnimalDataException;


public class AdminPageController {
    @FXML
    private Label usernameLabel;

    @FXML
    private TableView<AnimalShelter> shelterTable;

    @FXML
    private TableColumn<AnimalShelter, String> shelterNameColumn;

    @FXML
    private TableColumn<AnimalShelter, Integer> shelterCapacityColumn;

    @FXML
    private TableColumn<AnimalShelter, Double> shelterOccupancyRateColumn;

    @FXML
    private TableView<Animal> animalTable;

    @FXML
    private TableColumn<Animal, String> animalNameColumn;

    @FXML
    private TableColumn<Animal, String> animalSpeciesColumn;

    @FXML
    private TableColumn<Animal, Integer> animalAgeColumn;

    @FXML
    private TableColumn<Animal, Integer> animalPriceColumn;

    @FXML
    private TableColumn<Animal, AnimalCondition> animalConditionColumn;

    @FXML
    private ComboBox<AnimalCondition> stateComboBox;

    @FXML
    private Button adoptButton;

    @FXML
    private Button sortShelterButton;

    @FXML
    private Button removeShelterButton;

    @FXML
    private Button removeAnimalButton;

    @FXML
    private TextField searchField;

    @FXML
    private Button addShelterButton;

    @FXML
    private Button addAnimalButton;

    @FXML
    private Button editShelterButton;

    @FXML
    private Button editAnimalButton;

    @FXML
    private Button LogOutButton;

    private FilteredList<Animal> filteredAnimals;
    private AnimalShelter selectedShelter;
    private Animal selectedAnimal;

    private final ObservableList<Animal> allAnimals = FXCollections.observableArrayList();
    private final ObservableList<AnimalShelter> allshelters = FXCollections.observableArrayList();

    public void setUser(String user) {
        usernameLabel.setText("Hello, " + user + "!");
    }

    @FXML
    public void initialize() {
        DataInitializer dataInitializer = DataInitializer.getInstance();

        allshelters.addAll(dataInitializer.getAllShelters());
        allAnimals.addAll(dataInitializer.getAllAnimals());

        stateComboBox.setItems(FXCollections.observableArrayList(AnimalCondition.values()));

        shelterNameColumn.setCellValueFactory(new PropertyValueFactory<>("shelterName"));
        shelterCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        shelterOccupancyRateColumn.setCellValueFactory(new PropertyValueFactory<>("occupancyRate"));

        animalNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        animalSpeciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        animalAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        animalConditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        animalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        filteredAnimals = new FilteredList<>(FXCollections.observableArrayList(), p -> true);
        animalTable.setItems(filteredAnimals);
        shelterTable.setItems(allshelters);

        shelterTable.setOnMouseClicked(event -> {
            selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
            showAnimalsInSelectedShelter();
        });

        animalTable.setOnMouseClicked(event -> {
            selectedAnimal = animalTable.getSelectionModel().getSelectedItem();
            updateAdoptButtonVisibility();
        });
    }

    private void showAnimalsInSelectedShelter() {
        if (selectedShelter != null) {
            filteredAnimals = new FilteredList<>(FXCollections.observableArrayList(selectedShelter.getAnimalList()), p -> true);
            animalTable.setItems(filteredAnimals);
        } else {
            filteredAnimals.clear();
        }

        adoptButton.setVisible(false);
        selectedAnimal = null;
    }

    private void updateAdoptButtonVisibility() {
        if (selectedAnimal != null && selectedAnimal.getCondition() == AnimalCondition.HEALTHY) {
            adoptButton.setVisible(true);
        } else {
            adoptButton.setVisible(false);
        }
    }

    @FXML
    private void adoptAnimal() {
        if (selectedAnimal != null && selectedShelter != null) {
            selectedAnimal.setCondition(AnimalCondition.ADOPTED);
            allAnimals.remove(selectedAnimal);
            AnimalCondition selectedCondition = stateComboBox.getValue();
            filteredAnimals.setPredicate(animal -> {
                boolean matchesShelter = selectedShelter.getAnimalList().contains(animal);
                boolean matchesCondition = (selectedCondition == null || animal.getCondition() == selectedCondition);
                return matchesShelter && matchesCondition && animal.getCondition() != AnimalCondition.ADOPTED;
            });

            adoptButton.setVisible(false);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Adoption");
            alert.setHeaderText(null);
            alert.setContentText("The animal was adopted: " + selectedAnimal.getName());
            alert.showAndWait();
        }
    }

    @FXML
    private void filterByState() {
        AnimalCondition selectedCondition = stateComboBox.getValue();
        if (selectedCondition != null) {
            filteredAnimals.setPredicate(animal -> animal.getCondition() == selectedCondition);
        } else {
            filteredAnimals.setPredicate(animal -> true);
        }
    }

    @FXML
    private void sortSheltersByOccupancyRate() {
        ObservableList<AnimalShelter> sortedShelters = FXCollections.observableArrayList(allshelters);
        sortedShelters.sort((s1, s2) -> Double.compare(s2.getOccupancyRate(), s1.getOccupancyRate()));
        shelterTable.setItems(sortedShelters);
    }

    @FXML
    private void filterByText() {
        String searchText = searchField.getText().toLowerCase();

        if (selectedShelter != null) {
            filteredAnimals.setPredicate(animal -> {
                boolean matchesShelter = selectedShelter.getAnimalList().contains(animal);
                boolean matchesName = animal.getName().toLowerCase().contains(searchText);
                boolean matchesSpecies = animal.getSpecies().toLowerCase().contains(searchText);
                boolean matchesCondition = animal.getCondition().toString().toLowerCase().contains(searchText);
                boolean matchesAge = Integer.toString(animal.getAge()).contains(searchText);
                boolean matchesPrice = Double.toString(animal.getPrice()).contains(searchText);
                return matchesShelter && (matchesName || matchesSpecies || matchesCondition || matchesAge || matchesPrice);
            });
        }

        if (searchText.isEmpty()) {
            shelterTable.setItems(allshelters);
        } else {
            ObservableList<AnimalShelter> filteredShelters = FXCollections.observableArrayList();
            for (AnimalShelter shelter : allshelters) {
                if (shelter.getShelterName().toLowerCase().contains(searchText)) {
                    filteredShelters.add(shelter);
                }
            }
            shelterTable.setItems(filteredShelters);
        }
    }

    @FXML
    private void removeSelectedShelter() {
        if (selectedShelter != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Removing the shelter");
            alert.setHeaderText("Are you sure you want to delete this shelter?");
            alert.setContentText("Shelter: " + selectedShelter.getShelterName());

            if (alert.showAndWait().get() == ButtonType.OK) {
                allshelters.remove(selectedShelter);
                shelterTable.setItems(allshelters);

                if (selectedShelter != null) {
                    selectedShelter = null;
                    animalTable.setItems(FXCollections.observableArrayList());
                }
            }
        }
    }

    @FXML
    private void removeSelectedAnimal() {
        if (selectedAnimal != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Removing an animal");
            alert.setHeaderText("Are you sure you want to delete this pet?");
            alert.setContentText("Animal: " + selectedAnimal.getName());

            if (alert.showAndWait().get() == ButtonType.OK) {
                selectedShelter.removeAnimal(selectedAnimal);
                allAnimals.remove(selectedAnimal);

                filteredAnimals = new FilteredList<>(FXCollections.observableArrayList(selectedShelter.getAnimalList()), p -> true);
                animalTable.setItems(filteredAnimals);
                animalTable.refresh();

                AnimalCondition selectedCondition = stateComboBox.getValue();
                if (selectedCondition != null) {
                    filteredAnimals.setPredicate(animal -> animal.getCondition() == selectedCondition);
                }

                selectedAnimal = null;
            }
        }
    }

    @FXML
    private void addShelter() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Shelter");
        nameDialog.setHeaderText("Creating a new shelter");
        nameDialog.setContentText("Enter the name of the shelter:");

        Optional<String> shelterName = nameDialog.showAndWait();
        if (shelterName.isPresent() && !shelterName.get().trim().isEmpty()) {
            TextInputDialog capacityDialog = new TextInputDialog();
            capacityDialog.setTitle("Capacity");
            capacityDialog.setHeaderText("Please enter the maximum capacity of the shelter:");
            capacityDialog.setContentText("Maximum capacity:");

            Optional<String> capacityInput = capacityDialog.showAndWait();
            if (capacityInput.isPresent()) {
                try {
                    int maxCapacity = Integer.parseInt(capacityInput.get());
                    if (maxCapacity <= 0) {
                        throw new InvalidCapacityException("Capacity must be a positive number");
                    }
                    AnimalShelter newShelter = new AnimalShelter(shelterName.get(), maxCapacity);
                    allshelters.add(newShelter);
                    shelterTable.setItems(allshelters);
                } catch (NumberFormatException | InvalidCapacityException e) {
                    showAlert("Invalid number", e.getMessage());
                }
            }
        }
    }

    @FXML
    private void addAnimal() {
        try {
            if (selectedShelter == null) {
                throw new ShelterNotSelectedException("Select a shelter to add an animal");
            }

            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Add Animal");
            nameDialog.setHeaderText("Adding a new animal");
            nameDialog.setContentText("Enter the name of the animal:");

            Optional<String> animalName = nameDialog.showAndWait();
            if (animalName.isPresent() && !animalName.get().trim().isEmpty()) {
                TextInputDialog speciesDialog = new TextInputDialog();
                speciesDialog.setTitle("Species");
                speciesDialog.setHeaderText("Specify the species of animal:");
                Optional<String> species = speciesDialog.showAndWait();

                ChoiceDialog<AnimalCondition> conditionDialog = new ChoiceDialog<>(AnimalCondition.HEALTHY, AnimalCondition.values());
                conditionDialog.setTitle("Animal condition");
                conditionDialog.setHeaderText("Select animal condition:");
                Optional<AnimalCondition> condition = conditionDialog.showAndWait();

                TextInputDialog ageDialog = new TextInputDialog();
                ageDialog.setTitle("Age");
                ageDialog.setHeaderText("Enter the age of the animal:");
                Optional<String> ageInput = ageDialog.showAndWait();

                TextInputDialog priceDialog = new TextInputDialog();
                priceDialog.setTitle("Price");
                priceDialog.setHeaderText("Enter the price of the animal:");
                Optional<String> priceInput = priceDialog.showAndWait();

                TextInputDialog weightDialog = new TextInputDialog();
                weightDialog.setTitle("Libra");
                weightDialog.setHeaderText("Enter the weight of the animal:");
                Optional<String> weightInput = weightDialog.showAndWait();

                if (species.isPresent() && condition.isPresent() && ageInput.isPresent() && priceInput.isPresent() && weightInput.isPresent()) {
                    try {
                        int age = Integer.parseInt(ageInput.get());
                        double price = Double.parseDouble(priceInput.get());
                        double weight = Double.parseDouble(weightInput.get());

                        if (age < 0 || price < 0 || weight < 0) {
                            throw new InvalidAnimalDataException("Age, price and weight must be positive numbers.");
                        }

                        Animal newAnimal = new Animal(animalName.get(), species.get(), condition.get(), age, price, weight);
                        selectedShelter.addAnimal(newAnimal);
                        showAnimalsInSelectedShelter();
                    } catch (NumberFormatException | InvalidAnimalDataException e) {
                        showAlert("Format error", e.getMessage());
                    }
                }
            }
        } catch (ShelterNotSelectedException e) {
            showAlert("No shelter", e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void editShelter() {
        if (selectedShelter != null) {
            TextInputDialog nameDialog = new TextInputDialog(selectedShelter.getShelterName());
            nameDialog.setTitle("Shelter Edition");
            nameDialog.setHeaderText("Editing the selected shelter");
            nameDialog.setContentText("Shelter name:");

            Optional<String> nameResult = nameDialog.showAndWait();
            nameResult.ifPresent(newName -> {
                selectedShelter.setShelterName(newName);
                shelterTable.refresh();
            });

            TextInputDialog capacitydialog = new TextInputDialog(String.valueOf(selectedShelter.getMaxCapacity()));
            capacitydialog.setTitle("Shelter Edition");
            capacitydialog.setHeaderText("Editing the selected shelter");
            capacitydialog.setContentText("Maximum capacity:");

            Optional<String> capacityResult = capacitydialog.showAndWait();
            capacityResult.ifPresent(newCapacity -> {
                selectedShelter.setMaxCapacity(Integer.parseInt(newCapacity));
                shelterTable.refresh();
            });
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No shelter selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a shelter to edit.");
            alert.showAndWait();
        }
    }

    @FXML
    private void editAnimal() {
        try {
            if (selectedAnimal == null) {
                throw new AnimalNotSelectedException();
            }
            TextInputDialog nameDialog = new TextInputDialog(selectedAnimal.getName());
            nameDialog.setTitle("Animal Edition");
            nameDialog.setHeaderText("Edit pet name");
            nameDialog.setContentText("Name:");
            Optional<String> nameResult = nameDialog.showAndWait();
            nameResult.ifPresent(selectedAnimal::setName);

            TextInputDialog speciesDialog = new TextInputDialog(selectedAnimal.getSpecies());
            speciesDialog.setTitle("Animal Edition");
            speciesDialog.setHeaderText("Editing an animal species");
            speciesDialog.setContentText("Species:");
            Optional<String> speciesResult = speciesDialog.showAndWait();
            speciesResult.ifPresent(selectedAnimal::setSpecies);

            TextInputDialog ageDialog = new TextInputDialog(String.valueOf(selectedAnimal.getAge()));
            ageDialog.setTitle("Animal Edition");
            ageDialog.setHeaderText("Edit animal age");
            ageDialog.setContentText("Age:");
            Optional<String> ageResult = ageDialog.showAndWait();
            ageResult.ifPresent(newAge -> {
                try {
                    selectedAnimal.setAge(Integer.parseInt(newAge));
                } catch (NumberFormatException e) {
                    showError("Please enter a valid numeric value for age");
                }
            });

            TextInputDialog weightDialog = new TextInputDialog(String.valueOf(selectedAnimal.getWeight()));
            weightDialog.setTitle("Animal Edition");
            weightDialog.setHeaderText("Editing the weight of the animal");
            weightDialog.setContentText("Weight:");
            Optional<String> weightResult = weightDialog.showAndWait();
            weightResult.ifPresent(newWeight -> {
                try {
                    selectedAnimal.setWeight(Double.parseDouble(newWeight));
                } catch (NumberFormatException e) {
                    showError("Please enter a valid numeric value for weight.");
                }
            });

            TextInputDialog PriceDialog = new TextInputDialog(String.valueOf(selectedAnimal.getPrice()));
            PriceDialog.setTitle("Animal Edition");
            PriceDialog.setHeaderText("Edit pet price");
            PriceDialog.setContentText("Price:");
            Optional<String> priceResult = PriceDialog.showAndWait();
            priceResult.ifPresent(newPrice -> {
                try {
                    selectedAnimal.setPrice(Double.parseDouble(newPrice));
                } catch (NumberFormatException e) {
                    showError("Please enter a valid numeric value for price.");
                }
            });

            animalTable.refresh();
        } catch (AnimalNotSelectedException e) {
            showWarning("No animal selected", e.getMessage());
        }

    }

    @FXML
    public void onLogOutButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 1000, 800);
            Stage newStage = new Stage();
            newStage.setTitle("Login Page");
            newStage.setScene(newScene);
            newStage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Format error");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}