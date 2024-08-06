CREATE DATABASE IF NOT EXISTS mydb;

USE mydb;

CREATE TABLE IF NOT EXISTS Users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    user_password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    provider VARCHAR(50)
);

INSERT INTO Users (username, user_password, email, provider) 
VALUES ('testuser', 'password123', 'testuser@example.com', 'LOCAL');
