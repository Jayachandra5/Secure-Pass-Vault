package passwordvaltapp;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class newPassword  {

    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();

    FontWeight fw = FontWeight.BOLD;
    FontPosture fp = FontPosture.REGULAR;

    int sceneWidth = screenWidth;
    int sceneHeight = screenHeight;

    public void start(Stage stage) {
        stage.setTitle("Login Page");

        // Create UI components (TextFields, PasswordField, Button)
        Label Login = new Label("Change Password");
        Login.setFont(Font.font("Calibri", fw, fp, 28));

        Label password = new Label("New Password     ");
        password.setFont(Font.font("Calibri", fw, fp, 26));

        PasswordField passwordText = new PasswordField();
        passwordText.setFont(Font.font("Calibri", fw, fp, 20));

        ToggleButton toggleButton1 = new ToggleButton("👁");
        toggleButton1.setOnAction(event -> {
            if (toggleButton1.isSelected()) {
                passwordText.setPromptText(passwordText.getText());
                passwordText.clear();
            } else {
                passwordText.setText(passwordText.getPromptText());
                passwordText.setPromptText(null);
            }
        });

        StackPane root1 = new StackPane(passwordText, toggleButton1);
        root1.setAlignment(toggleButton1, Pos.CENTER_RIGHT);

        Label password2 = new Label("Re Enter Password");
        password2.setFont(Font.font("Calibri", fw, fp, 22));

        PasswordField passwordField = new PasswordField();
        passwordField.setFont(Font.font("Calibri", fw, fp, 20));

        ToggleButton toggleButton = new ToggleButton("👁");
        toggleButton.setOnAction(event -> {
            if (toggleButton.isSelected()) {
                passwordField.setPromptText(passwordField.getText());
                passwordField.clear();
            } else {
                passwordField.setText(passwordField.getPromptText());
                passwordField.setPromptText(null);
            }
        });

        StackPane root = new StackPane(passwordField, toggleButton);
        root.setAlignment(toggleButton, Pos.CENTER_RIGHT);

        Button loginButton = new Button("Done");
        loginButton.setFont(Font.font("Calibri", fw, fp, 20));

        Label text = new Label("");
        text.setFont(Font.font("Calibri", fw, fp, 20));

        HBox hbox = new HBox();
        hbox.getChildren().add(Login);
        hbox.setSpacing(15);
        hbox.setPadding(new Insets(5, 5, 50, 120));

        HBox hbox1 = new HBox();
        hbox1.getChildren().addAll(password, root1);
        hbox1.setSpacing(15);
        hbox1.setPadding(new Insets(5, 5, 5, 5));

        HBox hboxPass = new HBox();
        hboxPass.getChildren().addAll(password2, root);
        hboxPass.setSpacing(15);
        hboxPass.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox4 = new HBox();
        hbox4.getChildren().addAll(loginButton);
        hbox4.setSpacing(15);
        hbox4.setPadding(new Insets(5, 5, 5, 140));

        HBox hbox5 = new HBox();
        hbox5.getChildren().addAll(text);
        hbox5.setSpacing(15);
        hbox5.setPadding(new Insets(5, 5, 5, 65));

        VBox fullScreen = new VBox();
        fullScreen.setSpacing(10);
        fullScreen.setPadding(new Insets(220, 10, 10, 550));
        fullScreen.getChildren().addAll(hbox, hbox1, hboxPass, hbox4, hbox5);

        loginButton.setOnAction((ActionEvent e) -> {
            
            if(!passwordText.getText().equals(passwordField.getText())){
                text.setText("Password Mismatch");
            }else{
                LoginBackend db = new LoginBackend();
                
                db.updatePassword(GlobalContext.getCurrentUserName(),passwordText.getText() );
                text.setText("Password Updated Successfuly");
                LoginApp login = new LoginApp();
                login.start(stage);
            }
            
        });

        passwordText.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginButton.fire();
            }
        });

        File imageFile = new File(Constants.appIcon);
        Image logoImage = new Image(imageFile.toURI().toString());

        // Set the logo as the application icon
        stage.getIcons().add(logoImage);

        // Create the Scene and set it on the Stage
        Scene scene = new Scene(fullScreen, sceneWidth, sceneHeight - 70);
        stage.setScene(scene);
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}
