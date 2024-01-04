package passwordvaltapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginBackend {

    public static boolean createTable() {
        try (Connection connection = Constants.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS users ("
                + "mail VARCHAR(100) NOT NULL, "
                + "username VARCHAR(50) NOT NULL PRIMARY KEY, "
                + "password VARCHAR(256) NOT NULL)"
        )) {
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String checkUserName(String getUserName) {
        String selectUsernameQuery = "SELECT mail FROM users WHERE username = ? OR mail = ?";

        try (Connection connection = Constants.getConnection(); PreparedStatement selectUsernameStatement = connection.prepareStatement(selectUsernameQuery)) {

            // Set the parameter for both username and email
            selectUsernameStatement.setString(1, getUserName);
            selectUsernameStatement.setString(2, getUserName);

            ResultSet rs = selectUsernameStatement.executeQuery();

            if (rs.next()) {
                // Return the matched email if a result is found

                return rs.getString("mail");

            } else {
                // Return null if no match is found
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Return null in case of an error
            return null;
        }
    }

    public int updatePassword(String username, String newPassword) {
        String updatePasswordQuery = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection connection = Constants.getConnection(); PreparedStatement updatePasswordStatement = connection.prepareStatement(updatePasswordQuery)) {

            PasswordHashing hash = new PasswordHashing();
            String hashedPassword = hash.hashPassword(newPassword);

            updatePasswordStatement.setString(1, hashedPassword);
            updatePasswordStatement.setString(2, username);

            int rowsAffected = updatePasswordStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Password updated successfully");
                return 1; // Password updated successfully
            } else {
                System.out.println("Failed to update password. Username not found.");
                return 2; // Username not found (password not updated)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // Error occurred
        }
    }

    public static int insertUser(String name, String username, String password, String password2) {
        String selectUsernameQuery = "SELECT username FROM users WHERE username = ?";
        String insertUserQuery = "INSERT INTO users (mail, username, password) VALUES (?, ?, ?)";

        try (Connection connection = Constants.getConnection(); PreparedStatement selectUsernameStatement = connection.prepareStatement(selectUsernameQuery); PreparedStatement insertUserStatement = connection.prepareStatement(insertUserQuery)) {

            // Check if the username already exists
            selectUsernameStatement.setString(1, username);
            ResultSet rs = selectUsernameStatement.executeQuery();
            if (rs.next()) {
                System.out.println("Username already exists");
                return 1; // Username already exists
            }

            // Check if passwords match
            if (!password.equals(password2)) {
                System.out.println("Password mismatch");
                return 2; // Passwords don't match
            }

            PasswordHashing hash = new PasswordHashing();

            String hashedPassword = hash.hashPassword(password);

            // Insert the new user
            insertUserStatement.setString(1, name);
            insertUserStatement.setString(2, username);
            insertUserStatement.setString(3, hashedPassword);
            int rowsAffected = insertUserStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User inserted successfully");
                return 3; // User inserted successfully
            } else {
                System.out.println("Failed to insert user");
                return 0; // Failed to insert user
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // Error occurred
        }
    }

    public static int checkCredentials(String username, String password) {
        try (Connection connection = Constants.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM users WHERE username = ? "
        )) {
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            PasswordHashing hash = new PasswordHashing();

            if (rs.next()) {
                // User found, check credentials
                if (username.equals(rs.getString("username")) && hash.verifyPassword(rs.getString("password"), password)) {
                    GlobalContext.setCurrentUserName(username);
                    String currentUserName = GlobalContext.getCurrentUserName();
                    //    GlobalContext.setCurrentPassword(rs.getString("password"));
                    System.out.println(currentUserName);
                    System.out.println(rs.getString("password"));
                    return 4; // Credentials are correct
                } else {
                    return 3; // Username and password don't match
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Error occurred
    }

//    public static void main(String[] args) {
//        createTable();
//    }
}
