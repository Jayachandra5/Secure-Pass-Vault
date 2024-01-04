//
//
//import javafx.application.Application;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.TextFieldTableCell;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import javafx.util.Callback;
//
//public class EditableTableViewExample extends Application {
//
//    private TextField firstNameField;
//    private TextField lastNameField;
//    private TableView<Person> tableView;
//
//    @Override
//    public void start(Stage primaryStage) {
//        tableView = new TableView<>();
//        tableView.setEditable(true);
//
//        // Create columns
//        TableColumn<Person, String> firstNameColumn = createColumn("First Name", "firstName");
//        TableColumn<Person, String> lastNameColumn = createColumn("Last Name", "lastName");
//
//        // Add columns to the table
//        tableView.getColumns().addAll(firstNameColumn, lastNameColumn);
//
//        // Set data for the table
//        ObservableList<Person> data = PersonData.getSampleData();
//        tableView.setItems(data);
//
//        // Enable editing for the cells
//        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        firstNameColumn.setOnEditCommit(event -> {
//            Person person = event.getTableView().getItems().get(event.getTablePosition().getRow());
//            person.setFirstName(event.getNewValue());
//        });
//
//        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        lastNameColumn.setOnEditCommit(event -> {
//            Person person = event.getTableView().getItems().get(event.getTablePosition().getRow());
//            person.setLastName(event.getNewValue());
//        });
//
//        // Text fields for first name and last name
//        firstNameField = new TextField();
//        lastNameField = new TextField();
//
//        // Create an "Add" button
//        Button addButton = new Button("Add");
//        addButton.setOnAction(event -> addData());
//
//        VBox tableContainer = new VBox(tableView);
//        HBox inputContainer = new HBox(firstNameField, lastNameField, addButton);
//
//        VBox root = new VBox(tableContainer, inputContainer);
//        Scene scene = new Scene(root, 300, 200);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Editable TableView Example");
//        primaryStage.show();
//    }
//
//    private TableColumn<Person, String> createColumn(String columnName, String property) {
//        TableColumn<Person, String> column = new TableColumn<>(columnName);
//        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Person, String> param) {
//                return new SimpleStringProperty(param.getValue().getProperty(property));
//            }
//        });
//        return column;
//    }
//
//    private void addData() {
//        String firstName = firstNameField.getText();
//        String lastName = lastNameField.getText();
//        if (!firstName.isEmpty() && !lastName.isEmpty()) {
//            tableView.getItems().add(new Person(firstName, lastName));
//            firstNameField.clear();
//            lastNameField.clear();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
//
//class Person {
//    private final SimpleStringProperty firstName;
//    private final SimpleStringProperty lastName;
//
//    public Person(String firstName, String lastName) {
//        this.firstName = new SimpleStringProperty(firstName);
//        this.lastName = new SimpleStringProperty(lastName);
//    }
//
//    public String getFirstName() {
//        return firstName.get();
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName.set(firstName);
//    }
//
//    public String getLastName() {
//        return lastName.get();
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName.set(lastName);
//    }
//
//    public String getProperty(String property) {
//        if (property.equals("firstName")) {
//            return getFirstName();
//        } else if (property.equals("lastName")) {
//            return getLastName();
//        }
//        return null;
//    }
//}
//
//class PersonData {
//    public static ObservableList<Person> getSampleData() {
//        ObservableList<Person> data = FXCollections.observableArrayList();
//        data.add(new Person("John", "Doe"));
//        data.add(new Person("Jane", "Smith"));
//        data.add(new Person("Bob", "Johnson"));
//        return data;
//    }
//}
