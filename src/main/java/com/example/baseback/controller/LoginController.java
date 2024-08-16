package com.example.baseback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("/base")
public class LoginController {

    @Autowired
    private DataSource dataSource;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login attempt with username: " + loginRequest.getUsername());
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM Users WHERE username = ? AND user_password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, loginRequest.getUsername());
            statement.setString(2, loginRequest.getPassword());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new ResponseEntity<>("Login successful", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

class LoginRequest {
    private String username;
    private String password;

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
