package passwordvaltapp;

/** 
 * @author K Jayachandra
 */
import javafx.application.Application;
import javafx.stage.Stage;

public class PasswordValtApp extends Application {
    
    LoginApp app = new LoginApp();
    public static void main(String[] args) {
        launch(args);
        
    }

    @Override
    public void start(Stage stage) throws Exception {
        app.start(stage);
        
    }
    
}
