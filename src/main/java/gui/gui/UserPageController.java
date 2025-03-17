package gui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DataInitializer;
import system.Animal;
import system.AnimalCondition;
import system.AnimalShelter;

import java.io.IOException;

public class UserPageController {
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
    private Button LogOutButton;

    @FXML
    private TextField searchField;

    private FilteredList<Animal> filteredAnimals;
    private AnimalShelter selectedShelter;
    private Animal selectedAnimal;

    private final ObservableList<Animal> allAnimals = FXCollections.observableArrayList();
    private final ObservableList<AnimalShelter> allshelters = FXCollections.observableArrayList();

    public void setUser(String user) {
        usernameLabel.setText("Witaj, " + user + "!");
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
            alert.setTitle("Adopcja");
            alert.setHeaderText(null);
            alert.setContentText("Zwierzę zostało adoptowane: " + selectedAnimal.getName());
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
    public void onLogOutButtonClick(ActionEvent event) {
        try {
            // Ładowanie ekranu logowania
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 1000, 800);

            // Tworzenie nowej sceny i ustawienie jej na stage
            Stage newStage = new Stage();
            newStage.setTitle("Login Page");
            newStage.setScene(newScene);
            newStage.show();

            // Zamknięcie aktualnego okna
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // Filtracja schronisk
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
}