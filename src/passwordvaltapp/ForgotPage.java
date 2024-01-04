package passwordvaltapp;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ForgotPage  {

    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();

    FontWeight fw = FontWeight.BOLD;
    FontPosture fp = FontPosture.REGULAR;

    int sceneWidth = screenWidth;
    int sceneHeight = screenHeight;

    public void start(Stage stage) {
        stage.setTitle("Forgot Page");

        // Create UI components (TextFields, PasswordField, Button)
        Label Login = new Label("OTP Verfication");
        Login.setFont(Font.font("Calibri", fw, fp, 28));

        Label userName = new Label("OTP ( Recived in mail) ");
        userName.setFont(Font.font("Calibri", fw, fp, 28));

        TextField usernameField = new TextField();
        usernameField.setFont(Font.font("Calibri", fw, fp, 20));

        Button otpButton = new Button("Done");
        otpButton.setFont(Font.font("Calibri", fw, fp, 20));

        Label text = new Label("");
        text.setFont(Font.font("Calibri", fw, fp, 20));

        HBox hbox = new HBox();
        hbox.getChildren().add(Login);
        hbox.setSpacing(15);
        hbox.setPadding(new Insets(5, 5, 50, 120));

        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(userName, usernameField);
        hbox2.setSpacing(15);
        hbox2.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox3 = new HBox();
        hbox3.getChildren().addAll(otpButton);
        hbox3.setSpacing(15);
        hbox3.setPadding(new Insets(5, 5, 5, 200));

        HBox hbox6 = new HBox();
        hbox6.getChildren().addAll(text);
        hbox6.setSpacing(15);
        hbox6.setPadding(new Insets(5, 5, 5, 80));

        VBox fullScreen = new VBox();
        fullScreen.setSpacing(10);
        fullScreen.setPadding(new Insets(220, 400, 400, 550));
        fullScreen.getChildren().addAll(hbox, hbox2, hbox3, hbox6);

        otpButton.setOnAction((ActionEvent e) -> {
            
            boolean get =verifyOtp(usernameField.getText(),GlobalContext.getCurrentOtp());
            
            if ( get == true){
                newPassword newPass =  new newPassword();
                
                newPass.start(stage);
                
            }else if(get == false){
                text.setText("Otp Mismatch try again");
            }
        });

        File imageFile = new File(Constants.appIcon);
        Image logoImage = new Image(imageFile.toURI().toString());

        // Set the logo as the application icon
        stage.getIcons().add(logoImage);
        
        // Create the Scene and set it on the Stage
        Scene scene = new Scene(fullScreen, sceneWidth, sceneHeight - 70);
        stage.setScene(scene);

        stage.show();
    }

    public boolean verifyOtp(String enteredOtp, String expectedOtp) {
        // Compare entered OTP with the expected OTP
        return enteredOtp.equals(expectedOtp);
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}
