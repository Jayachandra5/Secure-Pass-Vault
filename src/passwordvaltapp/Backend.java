package passwordvaltapp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Backend {

    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASS)) {
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + Constants.DATABASE_NAME;
            try (PreparedStatement pstmt = conn.prepareStatement(createDatabaseSQL)) {
                pstmt.executeUpdate();
               // System.out.println("Database created successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDataTables() {
        try (Connection conn = Constants.getConnection()) {
            String createDataTableSQL = "CREATE TABLE IF NOT EXISTS data ("
                    + "Appname VARCHAR(255) PRIMARY KEY, "
                    + "url VARCHAR(255), "
                    + "username VARCHAR(255), "
                    + "password VARCHAR(256), "
                    + "status VARCHAR(255), "
                    + "currentUserName VARCHAR(255))";
            try (PreparedStatement pstmt = conn.prepareStatement(createDataTableSQL)) {
                pstmt.execute();
            }

            String createOldPasswordsTableSQL = "CREATE TABLE IF NOT EXISTS Oldpasswords ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "appname VARCHAR(255), "
                    + "password1 VARCHAR(256), "
                    + "password2 VARCHAR(256), "
                    + "password3 VARCHAR(256), "
                    + "currentUserName VARCHAR(255), "
                    + "FOREIGN KEY (appname) REFERENCES data(Appname))";

            try (PreparedStatement pstmt = conn.prepareStatement(createOldPasswordsTableSQL)) {
                pstmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDataTableProcedure() {
        try (Connection conn = Constants.getConnection()) {
            String storedProcedureCall = "{ call createDataTableProcedure() }";
            try (CallableStatement callableStatement = conn.prepareCall(storedProcedureCall)) {
                callableStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createOldPasswordsTableProcedure() {
        try (Connection conn = Constants.getConnection()) {
            String storedProcedureCall = "{ call createOldPasswordsTableProcedure() }";
            try (CallableStatement callableStatement = conn.prepareCall(storedProcedureCall)) {
                callableStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   public static void insertData(String Appname, String username, String password, String status, String url) {
    try (Connection conn = Constants.getConnection()) {
        String currentUserName = GlobalContext.getCurrentUserName();
        System.out.println(currentUserName);

        // Check if the Appname already exists in the table
        if (!isAppnameExists(conn, Appname, currentUserName)) {

            String insertDataSQL = "INSERT INTO data (Appname, username, password, status, url, currentUserName) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertDataSQL)) {
                TwoWayEncryptionExample hash = new TwoWayEncryptionExample();
                String hashedPassword = hash.encrypt(password, "ThisIsASecretKey");

                pstmt.setString(1, Appname);
                pstmt.setString(2, username);
                pstmt.setString(3, hashedPassword);
                pstmt.setString(4, status);
                pstmt.setString(5, url);
                pstmt.setString(6, currentUserName);
                pstmt.executeUpdate();
                System.out.println("Inserted");
            }
        } else {
            System.out.println("Appname already exists. No duplicate entry.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Helper method to check if Appname already exists in the table
private static boolean isAppnameExists(Connection conn, String Appname, String currentUserName) throws SQLException {
    String query = "SELECT COUNT(*) FROM data WHERE Appname = ? AND currentUserName = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, Appname);
        pstmt.setString(2, currentUserName);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        }
    }
    return false;
}


    public static void insertOldPassword(String appName, String newPassword) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Assuming you have a Connection object initialized somewhere
            connection = Constants.getConnection();

            String currentUserName = GlobalContext.getCurrentUserName();
            String sql = "SELECT password1, password2, password3 FROM "
                    + "Oldpasswords WHERE appname = ? AND currentUserName=?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, appName);
            pstmt.setString(2, currentUserName);

            ResultSet resultSet = pstmt.executeQuery();

            String password1 = null;
            String password2 = null;
            String password3 = null;

            if (resultSet.next()) {
                password1 = resultSet.getString("password1");
                password2 = resultSet.getString("password2");
                password3 = resultSet.getString("password3");
            }

            if (password1 == null) {
                // Insert into password1
                sql = "INSERT INTO Oldpasswords (appname, password1, currentUserName) VALUES (?, ?, ?)";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, appName);
                TwoWayEncryptionExample hash = new TwoWayEncryptionExample();
                String hashedPassword = hash.encrypt(newPassword, "ThisIsASecretKey");

                pstmt.setString(2, hashedPassword);
                pstmt.setString(3, currentUserName);
                pstmt.executeUpdate();
            } else {
                // Shift passwords and insert new password into password1
                sql = "UPDATE Oldpasswords SET password3 = ?, password2 = ?, password1 = ? WHERE appname = ?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, password2);
                pstmt.setString(2, password1);

                TwoWayEncryptionExample hash = new TwoWayEncryptionExample();
                String hashedPassword = hash.encrypt(newPassword, "ThisIsASecretKey");

                pstmt.setString(3, hashedPassword);
                pstmt.setString(4, appName);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static String getLastPassword(String appName, int order, String currentUserName) {
        try (Connection conn = Constants.getConnection()) {
            String selectPasswordSQL = "SELECT password" + order + " FROM Oldpasswords WHERE appname = ? AND currentUserName=" + currentUserName;
            try (PreparedStatement pstmt = conn.prepareStatement(selectPasswordSQL)) {

                pstmt.setString(1, appName);

                ResultSet resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString(1);

                    TwoWayEncryptionExample hash = new TwoWayEncryptionExample();
                    String password = hash.decrypt(hashedPassword, "ThisIsASecretKey");

                    return password;

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updatePassword(String Appname, String newPassword) {

        insertOldPassword(Appname, getOldPassword(Appname));

        try (Connection conn = Constants.getConnection()) {
            String updateDataSQL = "UPDATE data SET password = ? WHERE Appname = ? AND currentUserName=?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateDataSQL)) {
                String currentUserName = GlobalContext.getCurrentUserName();

                TwoWayEncryptionExample hash = new TwoWayEncryptionExample();
                String hasedPassword = hash.encrypt(newPassword, "ThisIsASecretKey");

                pstmt.setString(1, hasedPassword);
                pstmt.setString(2, Appname);
                pstmt.setString(3, currentUserName);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getOldPassword(String Appname) {

        try (Connection conn = Constants.getConnection()) {
            String selectUsernameSQL = "SELECT password FROM data WHERE Appname = ? AND currentUserName=?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectUsernameSQL)) {

                String currentUserName = GlobalContext.getCurrentUserName();
                pstmt.setString(1, Appname);
                pstmt.setString(2, currentUserName);
                ResultSet resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");

                    TwoWayEncryptionExample hash = new TwoWayEncryptionExample();
                    String password = hash.decrypt(hashedPassword, "ThisIsASecretKey");

                    return password;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void displayData() {
        try (Connection conn = Constants.getConnection()) {
            String selectDataSQL = "SELECT * FROM data WHERE currentUserName=?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectDataSQL)) {

                String currentUserName = GlobalContext.getCurrentUserName();
                pstmt.setString(1, currentUserName);
                ResultSet resultSet = pstmt.executeQuery();

                System.out.println("Appname\tURL\tUsername\tPassword\tStatus");
                while (resultSet.next()) {

                    String Appname = resultSet.getString("Appname");
                    String url = resultSet.getString("url");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String status = resultSet.getString("status");

                    System.out.println(Appname + "\t" + url + "\t" + username + "\t" + password + "\t" + status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayOldPasswords(String appName) {

        String currentUserName = GlobalContext.getCurrentUserName();

        try (Connection conn = Constants.getConnection()) {
            String selectOldPasswordsSQL = "SELECT password1, password2, password3 FROM Oldpasswords"
                    + " WHERE appname = ? AND currentUserName=?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectOldPasswordsSQL)) {
                pstmt.setString(1, appName);
                pstmt.setString(2, currentUserName);
                ResultSet resultSet = pstmt.executeQuery();
                System.out.println("Old Passwords for " + appName);
                while (resultSet.next()) {
                    String password1 = resultSet.getString("password1");
                    String password2 = resultSet.getString("password2");
                    String password3 = resultSet.getString("password3");
                    System.out.println("Password 1: " + password1);
                    System.out.println("Password 2: " + password2);
                    System.out.println("Password 3: " + password3);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteData(String Appname) {
        try (Connection conn = Constants.getConnection()) {

            String currentUserName = GlobalContext.getCurrentUserName();

            // Delete the row from the data table
            String deleteDataSQL = "DELETE FROM Oldpasswords WHERE appname = ? AND currentUserName=?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteDataSQL)) {
                pstmt.setString(1, Appname);
                pstmt.setString(2, currentUserName);
                pstmt.executeUpdate();

                // Delete the row from the Oldpasswords table using the username
                String deleteOldPasswordsSQL = "DELETE FROM data WHERE Appname = ? AND currentUserName=?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(deleteOldPasswordsSQL)) {
                    pstmt2.setString(1, Appname);
                    pstmt2.setString(2, currentUserName);
                    pstmt2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void deleteData1(String Appname) {
        try (Connection conn = Constants.getConnection()) {
            String currentUserName = GlobalContext.getCurrentUserName();

            // Delete the row from the data table
            String deleteDataSQL = "DELETE FROM data WHERE Appname = ? AND currentUserName=?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteDataSQL)) {
                pstmt.setString(1, Appname);
                pstmt.setString(2, currentUserName);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePassword2(String Appname, String newPassword) {
        insertOldPassword(Appname, getOldPassword(Appname));

        try (Connection conn = Constants.getConnection()) {
            String updateDataSQL = "UPDATE data SET password = ? WHERE Appname = ? AND currentUserName=?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateDataSQL)) {
                String currentUserName = GlobalContext.getCurrentUserName();
                pstmt.setString(1, newPassword);
                pstmt.setString(2, Appname);
                pstmt.setString(3, currentUserName);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

//    public static void main(String[] args) {
//        createDatabase();
//        createDataTables();
//        
//    }
}
