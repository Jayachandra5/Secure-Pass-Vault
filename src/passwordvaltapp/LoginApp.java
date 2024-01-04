package passwordvaltapp;

import java.io.File;
import java.util.Properties;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class LoginApp extends Application {

    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();

    FontWeight fw = FontWeight.BOLD;
    FontPosture fp = FontPosture.REGULAR;

    int sceneWidth = screenWidth;
    int sceneHeight = screenHeight;

    LoginBackend db = new LoginBackend();
    Backend mainDb = new Backend();
    //   mailtest dd = new mailtest();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Login Page");

        mainDb.createDatabase();
        mainDb.createDataTables();
        db.createTable();

        // dd.testBro();
        // Create UI components (TextFields, PasswordField, Button)
        Label Login = new Label("User Login");
        Login.setFont(Font.font("Calibri", fw, fp, 28));

        Label userName = new Label("Username ");
        userName.setFont(Font.font("Calibri", fw, fp, 28));

        TextField usernameField = new TextField();
        usernameField.setFont(Font.font("Calibri", fw, fp, 20));

        Label password = new Label("Password  ");
        password.setFont(Font.font("Calibri", fw, fp, 28));

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

        Button SinginButton = new Button("New User");
        SinginButton.setFont(Font.font("Calibri", fw, fp, 20));

        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font("Calibri", fw, fp, 20));

        Button forgotButton = new Button("Forgot Password");
        forgotButton.setFont(Font.font("Calibri", fw, fp, 16));

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
        hbox3.getChildren().addAll(password, root);
        hbox3.setSpacing(15);
        hbox3.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox4 = new HBox();
        hbox4.getChildren().addAll(SinginButton, loginButton);
        hbox4.setSpacing(15);
        hbox4.setPadding(new Insets(5, 5, 5, 110));

        HBox hbox5 = new HBox();
        hbox5.getChildren().addAll(forgotButton);
        hbox5.setSpacing(15);
        hbox5.setPadding(new Insets(5, 5, 5, 150));

        HBox hbox6 = new HBox();
        hbox6.getChildren().addAll(text);
        hbox6.setSpacing(15);
        hbox6.setPadding(new Insets(5, 5, 5, 80));

        VBox fullScreen = new VBox();
        fullScreen.setSpacing(10);
        fullScreen.setPadding(new Insets(220, 400, 400, 550));
        fullScreen.getChildren().addAll(hbox, hbox2, hbox3, hbox4, hbox5, hbox6);

        Scene scene = new Scene(new Group(), sceneWidth, sceneHeight - 70);

        Image image = new Image(Constants.splashScreen);
        ImageView imageView = new ImageView(image);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(imageView.opacityProperty(), 0.0)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new KeyValue(imageView.opacityProperty(), 1.0)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(3000), new KeyValue(imageView.opacityProperty(), 1.0)));
       // timeline.getKeyFrames().add(new KeyFrame(Duration.millis(6000), new KeyValue(imageView.opacityProperty(), 0.0)));
        timeline.setCycleCount(1);
        timeline.setOnFinished(event -> {
            ((Group) scene.getRoot()).getChildren().addAll(fullScreen);
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.show();
        });
        timeline.play();
        StackPane root1 = new StackPane(imageView);

        File imageFile = new File(Constants.appIcon);
        Image logoImage = new Image(imageFile.toURI().toString());

        // Set the logo as the application icon
        stage.getIcons().add(logoImage);

        // Create the Scene and set it on the Stage
        Scene scene1 = new Scene(root1);
        stage.setScene(scene1);

        stage.show();

        SinginButton.setOnAction((ActionEvent e) -> {
            singinPage(stage);
        });

        loginButton.setOnAction((ActionEvent e) -> {
            int status = db.checkCredentials(usernameField.getText(), passwordField.getText());

            if (status == 0) {
                text.setStyle("-fx-text-fill:red;");
                text.setText("Incorrect Username ");

            } else if (status == 3) {
                text.setStyle("-fx-text-fill:red;");
                text.setText("Incorrect Password");

            } else if (status == 4) {
                // Set currentUserName when the user logs in

                text.setStyle("-fx-text-fill:black;");
                text.setText("Login Success");

                UI mainUI = new UI();

                mainUI.start(stage);

            }
        });

        forgotButton.setOnAction((ActionEvent e) -> {

            System.out.println(usernameField.getText());
            String checkUser = db.checkUserName(usernameField.getText());

            if (checkUser != null) {

                JavaMailSender mailSender = createMailSender();
                SendEmail test = new SendEmail(mailSender);
                test.sendOtp(checkUser);
                GlobalContext.setCurrentUserName(usernameField.getText());

                ForgotPage forgot = new ForgotPage();
                forgot.start(stage);
            } else {
                text.setText("User Does not exist");
            }
        });

        usernameField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginButton.fire();
            }
        });

    }

    private JavaMailSender createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); //  SMTP host
        mailSender.setPort(587); //  SMTP port
        mailSender.setUsername("inventorymanagementsystem2023@gmail.com"); //  SMTP username
        mailSender.setPassword("qvaqyxpfkhufguas"); // SMTP password

        // Enable TLS
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    public void singinPage(Stage stage) {

        // Create UI components (TextFields, PasswordField, Button)
        Label Login = new Label("User Signin");
        Login.setFont(Font.font("Calibri", fw, fp, 28));

        Label Name = new Label("Mail ID          ");
        Name.setFont(Font.font("Calibri", fw, fp, 26));

        TextField nameField = new TextField();
        nameField.setFont(Font.font("Calibri", fw, fp, 20));

        Label userName = new Label("Username    ");
        userName.setFont(Font.font("Calibri", fw, fp, 26));

        TextField usernameField = new TextField();
        usernameField.setFont(Font.font("Calibri", fw, fp, 20));

        Label password = new Label("Password     ");
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

        Label password2 = new Label("Re Password");
        password2.setFont(Font.font("Calibri", fw, fp, 26));

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

        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font("Calibri", fw, fp, 20));

        Button SinginButton = new Button("Singin");
        SinginButton.setFont(Font.font("Calibri", fw, fp, 20));

        Label text = new Label("");
        text.setFont(Font.font("Calibri", fw, fp, 20));

        HBox hbox = new HBox();
        hbox.getChildren().add(Login);
        hbox.setSpacing(15);
        hbox.setPadding(new Insets(5, 5, 50, 120));

        HBox hbox1 = new HBox();
        hbox1.getChildren().addAll(Name, nameField);
        hbox1.setSpacing(15);
        hbox1.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(userName, usernameField);
        hbox2.setSpacing(15);
        hbox2.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox3 = new HBox();
        hbox3.getChildren().addAll(password, root1);
        hbox3.setSpacing(15);
        hbox3.setPadding(new Insets(5, 5, 5, 5));

        HBox hboxPass = new HBox();
        hboxPass.getChildren().addAll(password2, root);
        hboxPass.setSpacing(15);
        hboxPass.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox4 = new HBox();
        hbox4.getChildren().addAll(loginButton, SinginButton);
        hbox4.setSpacing(15);
        hbox4.setPadding(new Insets(5, 5, 5, 140));

        HBox hbox5 = new HBox();
        hbox5.getChildren().addAll(text);
        hbox5.setSpacing(15);
        hbox5.setPadding(new Insets(5, 5, 5, 65));

        VBox fullScreen = new VBox();
        fullScreen.setSpacing(10);
        fullScreen.setPadding(new Insets(220, 10, 10, 550));
        fullScreen.getChildren().addAll(hbox, hbox1, hbox2, hbox3, hboxPass, hbox4, hbox5);

        SinginButton.setOnAction((ActionEvent e) -> {
            int status = db.insertUser(nameField.getText(), usernameField.getText(), passwordText.getText(), passwordField.getText());

            if (status == 1) {
                text.setStyle("-fx-text-fill:red;");
                text.setText("User Alredy Exist ");

            } else if (status == 2) {
                text.setStyle("-fx-text-fill:red;");
                text.setText("Password & Re- Password didnt match ");
            } else if (status == 3) {
                text.setStyle("-fx-text-fill:black;");
                text.setText("User Created ");
            }
        });

        loginButton.setOnAction((ActionEvent e) -> {
            start(stage);
        });

        nameField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                usernameField.requestFocus();
            }
        });

        usernameField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordText.requestFocus();
            }
        });

        passwordText.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                SinginButton.fire();
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

    public static void main(String[] args) {
        launch(args);
    }
}
