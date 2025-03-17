CREATE DATABASE shelter;

USE shelter;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status ENUM('admin', 'user') NOT NULL
);

INSERT INTO users (username, password, status) VALUES
('admin', 'admin1234', 'admin'),
('user', 'user1234', 'user');
