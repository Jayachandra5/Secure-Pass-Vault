package passwordvaltapp;

import java.io.File;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import passwordvaltapp.Backend;

public class UI  {

    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();

    FontWeight fw = FontWeight.BOLD;
    FontPosture fp = FontPosture.REGULAR;

    int sceneWidth = screenWidth;
    int sceneHeight = screenHeight;

    Label title;
    TextField searchBar;
    TableView<Person> table;
    ObservableList<Person> data;
    Menu menu1, menu2, menu3;
    MenuItem item11, item12, item21, item22, item31;
    MenuBar menuBar;
    TextField appName, userName, password, status, url;
    Button addNew;

    Backend db = new Backend();

    List<String> currentData = new LinkedList<String>();

    public void start(Stage stage) {
        stage.setTitle("Password Vault");

        title = new Label("                                                      "
                + "                                                             "
                + "                                              Secure Pass Vault");
        title.setStyle("-fx-background-color:DODGERBLUE;");
        title.setPrefSize(sceneWidth, 20);
        title.setFont(Font.font("Calibri", fw, fp, 18));

        menu1 = new Menu("      Menu      ");
        menu2 = new Menu("      View      ");
        menu3 = new Menu("      New      ");

        item11 = new MenuItem("Home");
        item12 = new MenuItem("Logout");

        item21 = new MenuItem("Prev Passwords");
        item22 = new MenuItem("Full List");

        item31 = new MenuItem("New User");

        menu1.getItems().addAll(item11, item12);
        menu2.getItems().addAll(item21, item22);
        menu3.getItems().addAll(item31);

        menuBar = new MenuBar();

        menuBar.getMenus().addAll(menu1, menu2, menu3);

        searchBar = new TextField();
        searchBar.setStyle("-fx-background-color:DODGERBLUE;-fx-prompt-text-fill:black;-fx-text-fill:black;");
        searchBar.setFont(Font.font("Calibri", fw, fp, 13));
        searchBar.setPromptText("🔍 Enter App Name");
        searchBar.setPrefSize(sceneWidth - 320, 25);

        table = new TableView<>();
        table.setEditable(true);
        table.setPrefHeight(sceneHeight - 200);
        table.setPrefWidth(sceneWidth - 50);
        table.setStyle("-fx-background-color:white;");

        appName = new TextField();
        userName = new TextField();
        password = new TextField();
        status = new TextField();
        url = new TextField();

        appName.setMinWidth((sceneWidth / 6));
        userName.setMinWidth((sceneWidth / 6));
        password.setMinWidth((sceneWidth / 6));
        status.setMinWidth((sceneWidth / 6));
        url.setMinWidth((sceneWidth / 6));

        addNew = new Button("AddNew");

        int set = sceneWidth - 320;

        getData();
        TableColumn<Person, String> Col1 = createColumn("Appname", "firstName");
        TableColumn<Person, String> Col2 = createColumn("Username", "lastName");
        TableColumn<Person, String> Col3 = createColumn("Password", "email");
        TableColumn<Person, String> Col4 = createColumn("Status", "Price");
        TableColumn<Person, String> Col5 = createColumn("URL", "Tax");
        TableColumn<Person, Button> Col6 = createDeleteColumn("Delete");

        Col1.setMinWidth((sceneWidth / 6));
        Col2.setMinWidth((sceneWidth / 6) - 15);
        Col3.setMinWidth((sceneWidth / 6) - 15);
        Col4.setMinWidth((sceneWidth / 6) - 15);
        Col5.setMinWidth((sceneWidth / 6) - 15);
        Col6.setMinWidth((sceneWidth / 6) - 15);

        table.getColumns().addAll(Col1, Col2, Col3, Col4, Col5, Col6);
        table.setItems(data);

        HBox hbox = new HBox(title);
        hbox.setSpacing(15);
        hbox.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox2 = new HBox(menuBar, searchBar);
        hbox2.setSpacing(15);
        hbox2.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox3 = new HBox(appName, userName, password, status, url, addNew);
        hbox3.setSpacing(15);
        hbox3.setPadding(new Insets(5, 5, 5, 5));

        VBox fullScreen = new VBox(hbox, hbox2, table, hbox3);
        fullScreen.setSpacing(10);
        fullScreen.setPadding(new Insets(10, 10, 10, 10));

        File imageFile = new File(Constants.appIcon);
        Image logoImage = new Image(imageFile.toURI().toString());

        // Set the logo as the application icon
        stage.getIcons().add(logoImage);

        Scene scene = new Scene(fullScreen, sceneWidth, sceneHeight - 70);
        stage.setScene(scene);
        stage.show();

        addNew.setOnAction((ActionEvent e) -> {

            String getAppName = appName.getText();
            String getUserName = userName.getText();
            String getPassword = password.getText();
            String getStatus = status.getText();
            String getUrl = url.getText();

            if (!getAppName.isEmpty() && !getUserName.isEmpty() && !getPassword.isEmpty() && !getStatus.isEmpty() && !getUrl.isEmpty()) {
                db.insertData(getAppName, getUserName, getPassword, getStatus, getUrl);
                System.out.println("data ");

                data.add(new Person(getAppName, getUserName, getPassword, getStatus, getUrl));

                getData();
                table.setItems(data);

                appName.clear();
                userName.clear();
                password.clear();
                status.clear();
                url.clear();
            }
        });

        appName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                userName.requestFocus();
            }
        });

        userName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                password.requestFocus();
            }
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                status.requestFocus();
            }
        });

        status.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                url.requestFocus();
            }
        });

        url.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                addNew.fire();
            }
        });

        // TextFields.bindAutoCompletion(searchBar, currentData);
        FilteredList<Person> filterdAppData = new FilteredList<>(data, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdAppData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (person.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });

            SortedList<Person> sortedData = new SortedList<>(filterdAppData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
        });

        item12.setOnAction((ActionEvent e) -> {
            LoginApp loginpage = new LoginApp();

            loginpage.start(stage);
        });

        item21.setOnAction((ActionEvent e) -> {
            PrevPassword prev = new PrevPassword();

            prev.start(stage);
        });

        item22.setOnAction((ActionEvent e) -> {
            FullTable completeList = new FullTable();
            completeList.start(stage);
        });

        item31.setOnAction((ActionEvent e) -> {
            LoginApp loginpage = new LoginApp();

            loginpage.singinPage(stage);
        });

    }

    private TableColumn<Person, String> createColumn(String columnName, String property) {
        TableColumn<Person, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProperty(property)));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> updateData(event.getRowValue(), event));
        return column;
    }

    private TableColumn<Person, Button> createDeleteColumn(String columnName) {
        TableColumn<Person, Button> column = new TableColumn<>(columnName);
        column.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getDeleteButton()));

        return column;
    }

    private void addData() {
        String getAppName = appName.getText();
        String getUserName = userName.getText();
        String getPassword = password.getText();
        String getStatus = status.getText();
        String getUrl = url.getText();

        if (!getAppName.isEmpty() && !getUserName.isEmpty() && !getPassword.isEmpty() && !getStatus.isEmpty() && !getUrl.isEmpty()) {
            table.getItems().add(new Person(getAppName, getUserName, getPassword, getStatus, getUrl));
        }
    }

    private void updateData(Person personToUpdate, TableColumn.CellEditEvent<Person, String> event) {
        // Extract the updated value from the event
        String newValue = event.getNewValue();

//        TableColumn<Person, String> editedColumn = event.getTableColumn();
//        if (editedColumn == appNameCol) {
        db.updatePassword(personToUpdate.getFirstName(), newValue);
        personToUpdate.setEmail(newValue);
//        } 

        table.refresh();
    }

    public void deleteData(Person personToDelete) {
        String deletedAppName = personToDelete.getFirstName();
        
        // Then, you can safely update the database or perform any other operations
        db.deleteData(deletedAppName);

        // Remove the item directly from the ObservableList
        data.remove(personToDelete);

        getData();
        table.setItems(data);

        System.out.println("Deleted App Name: " + deletedAppName);
    }

    public class Person {

        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;
        private final SimpleStringProperty Price;
        private final SimpleStringProperty Tax;
        private Button deleteButton;

        public Person(String fName, String lName, String email, String price, String tax) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
            this.Price = new SimpleStringProperty(price);
            this.Tax = new SimpleStringProperty(tax);
            this.deleteButton = new Button("🗑");
            this.deleteButton.setOnAction(event -> deleteData(this));
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(String fName) {
            lastName.set(fName);
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String email) {
            this.email.set(email);
        }

        public String getPrice() {
            return Price.get();
        }

        public void setPrice(String price) {
            Price.set(price);
        }

        public String getTax() {
            return Tax.get();
        }

        public void setTax(String tax) {
            Tax.set(tax);
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

        public void setDeleteButton(Button deleteButton) {
            this.deleteButton = deleteButton;
        }

        public String getProperty(String property) {
            if ("firstName".equals(property)) {
                return getFirstName();
            } else if ("lastName".equals(property)) {
                return getLastName();
            } else if ("email".equals(property)) {
                return getEmail();
            } else if ("Price".equals(property)) {
                return getPrice();
            } else if ("Tax".equals(property)) {
                return getTax();
            }
            return null;
        }
    }

    public void getData() {

        try (Connection conn = Constants.getConnection()) {
            String selectDataSQL = "SELECT * FROM data WHERE currentUserName=?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectDataSQL)) {

                String currentUserName = GlobalContext.getCurrentUserName();
                pstmt.setString(1, currentUserName);

                ResultSet resultSet = pstmt.executeQuery();

                data = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    String Appname = resultSet.getString("Appname");
                    String url1 = resultSet.getString("url");
                    String username = resultSet.getString("username");
                    String encryptedMessage = resultSet.getString("password");
                    String status1 = resultSet.getString("status");

                    TwoWayEncryptionExample hash = new TwoWayEncryptionExample();
                    String password = hash.decrypt(encryptedMessage, "ThisIsASecretKey");

                    currentData.add(Appname);
                    data.add(new Person(Appname, username, password, status1, url1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}
