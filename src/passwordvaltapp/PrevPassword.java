package passwordvaltapp;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PrevPassword {

    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();

    FontWeight fw = FontWeight.BOLD;
    FontPosture fp = FontPosture.REGULAR;

    int sceneWidth = screenWidth;
    int sceneHeight = screenHeight;

    Label title;
    Menu menu1, menu2, menu3;
    MenuItem item11, item12, item21, item22, item31;
    MenuBar menuBar;
    TextField searchBar;

    TableView<Person> table = new TableView<Person>();

    ObservableList<Person> data;

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
        int set = sceneWidth - 320;

        searchBar = new TextField();
        searchBar.setStyle("-fx-background-color:DODGERBLUE;-fx-prompt-text-fill:black;-fx-text-fill:black;");
        searchBar.setFont(Font.font("Calibri", fw, fp, 13));
        searchBar.setPromptText("🔍 Enter App Name");
        searchBar.setPrefSize(set, 25);

        table.setEditable(true);
        table.setPrefHeight(sceneHeight - 200);
        table.setPrefWidth(sceneWidth - 50);
        table.setStyle("-fx-background-color:white;");

        getData();
        TableColumn Col1 = new TableColumn("Appname");
        Col1.setMinWidth((sceneWidth / 6));
        Col1.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));

        TableColumn Col2 = new TableColumn("Password1");
        Col2.setMinWidth((sceneWidth / 6) - 15);
        Col2.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));

        TableColumn Col3 = new TableColumn("Password2");
        Col3.setMinWidth((sceneWidth / 6) - 15);
        Col3.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));

        TableColumn Col4 = new TableColumn("Password3");
        Col4.setMinWidth((sceneWidth / 6) - 15);
        Col4.setCellValueFactory(
                new PropertyValueFactory<Person, String>("Price"));

        table.getColumns().addAll(Col1, Col2, Col3, Col4);
        table.setItems(data);

        HBox hbox = new HBox();
        hbox.getChildren().add(title);
        hbox.setSpacing(15);
        hbox.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(menuBar, searchBar);
        hbox2.setSpacing(15);
        hbox2.setPadding(new Insets(5, 5, 5, 5));

        VBox fullScreen = new VBox();
        fullScreen.setSpacing(10);
        fullScreen.setPadding(new Insets(10, 10, 10, 10));
        fullScreen.getChildren().addAll(hbox, hbox2, table);

        File imageFile = new File(Constants.appIcon);
        Image logoImage = new Image(imageFile.toURI().toString());

        // Set the logo as the application icon
        stage.getIcons().add(logoImage);

        Scene scene = new Scene(fullScreen, sceneWidth, sceneHeight - 70);
        stage.setScene(scene);
        stage.show();

        FilteredList<Person> filterdAppData = new FilteredList<>(data, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdAppData.setPredicate(Person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (Person.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Person> sortedData = new SortedList<>(filterdAppData);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);

        item11.setOnAction((ActionEvent e) -> {
            UI mainUI = new UI();

            mainUI.start(stage);
        });

        item12.setOnAction((ActionEvent e) -> {
            LoginApp loginpage = new LoginApp();

            loginpage.start(stage);
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

    public static class Person {

        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;
        private final SimpleStringProperty Price;

        private Person(String fName, String lName, String email, String price) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
            this.Price = new SimpleStringProperty(price);

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

        public void setEmail(String fName) {
            email.set(fName);
        }

        public String getPrice() {
            return Price.get();
        }

        public void setPrice(String fName) {
            Price.set(fName);
        }
    }

    public void getData() {

        try (Connection conn = Constants.getConnection()) {
            String selectDataSQL = "SELECT * FROM oldpasswords WHERE currentUserName=?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectDataSQL)) {
                String currentUserName = GlobalContext.getCurrentUserName();
                pstmt.setString(1, currentUserName);
                ResultSet resultSet = pstmt.executeQuery();

                data = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    String Appname = resultSet.getString("appname");
                    String hashPassword1 = resultSet.getString("password1");
                    String hashPassword2 = resultSet.getString("password2");
                    String hashPassword3 = resultSet.getString("password3");

                    String password1 =null;
                    String password2 =null;
                    String password3 =null;
                    
                    
                    TwoWayEncryptionExample hash = new TwoWayEncryptionExample();
                    if(hashPassword1 != null){
                        password1 = hash.decrypt(hashPassword1, "ThisIsASecretKey");
                    }
                    
                    if(hashPassword2 != null){
                        password2 = hash.decrypt(hashPassword2, "ThisIsASecretKey");
                    }
                    
                    if(hashPassword3 != null){
                         password3 = hash.decrypt(hashPassword3, "ThisIsASecretKey");
                    }
                    
                    
                    data.add(new Person(Appname, password1, password2, password3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//     public static void main(String[] args) {
//        launch(args);
//    }
}
