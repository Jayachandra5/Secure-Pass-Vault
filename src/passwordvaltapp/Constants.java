package passwordvaltapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Constants {
    
    public static final String appIcon = "C:\\SecurePassVault\\Icon.png";
    
    public static String splashScreen = "C:\\SecurePassVault\\Splash.gif";
    
    
    public static final String DB_URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "JcV@5818";
    public static final String DATABASE_NAME = "PasswordVault";

    public static Connection getConnection() throws SQLException {
        String fullDBUrl = DB_URL + DATABASE_NAME;
        return DriverManager.getConnection(fullDBUrl, DB_USER, DB_PASS);
    }
    
}
