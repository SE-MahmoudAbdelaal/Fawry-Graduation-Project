-- Create the database
CREATE DATABASE user_api;
USE user_api;

-- Create the roles table
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT chk_role_name CHECK (LENGTH(role_name) > 0 AND LENGTH(role_name) <= 50)
);

-- Insert roles into the roles table
INSERT INTO roles (role_name) VALUES ('user'), ('admin'), ('trader');

-- Create the users table with a boolean status, a flag for deletion, and a foreign key to roles
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) DEFAULT NULL,
    photo VARCHAR(255) DEFAULT 'default.jpg',
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL, -- This will be used to mark the record as deleted
    role_id INT,
    CONSTRAINT chk_email_format CHECK (email LIKE '%@%.%'),
    CONSTRAINT chk_first_name CHECK (LENGTH(first_name) > 0 AND LENGTH(first_name) <= 50),
    CONSTRAINT chk_last_name CHECK (LENGTH(last_name) > 0 AND LENGTH(last_name) <= 50),
    CONSTRAINT chk_password CHECK (LENGTH(password) >= 8 AND LENGTH(password) <= 255),
    CONSTRAINT chk_phone_format CHECK (phone REGEXP '^[0-9]{10,15}$'),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE SET NULL
);

-- Create the password_resets table with a foreign key to users
CREATE TABLE password_resets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    reset_token VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT chk_reset_token_length CHECK (LENGTH(reset_token) > 0 AND LENGTH(reset_token) <= 255)
);

-- Create the user_logs table
CREATE TABLE user_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    change_type VARCHAR(10) DEFAULT 'UPDATE', -- Since we're only logging updates
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    old_values TEXT,
    new_values TEXT,
    CONSTRAINT fk_user_logs FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Change delimiter
DELIMITER //

-- Create the trigger for logging updates to the users table
CREATE TRIGGER after_user_update
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    INSERT INTO user_logs (user_id, old_values, new_values)
    VALUES (
        OLD.user_id,
        CONCAT(
            'first_name: ', OLD.first_name, ', ',
            'last_name: ', OLD.last_name, ', ',
            'phone: ', COALESCE(OLD.phone, 'NULL'), ', ',
            'photo: ', OLD.photo, ', ',
            'password: ', OLD.password, ', ',
            'email: ', OLD.email, ', ',
            'is_active: ', OLD.is_active
        ),
        CONCAT(
            'first_name: ', NEW.first_name, ', ',
            'last_name: ', NEW.last_name, ', ',
            'phone: ', COALESCE(NEW.phone, 'NULL'), ', ',
            'photo: ', NEW.photo, ', ',
            'password: ', NEW.password, ', ',
            'email: ', NEW.email, ', ',
            'is_active: ', NEW.is_active
        )
    );
END//

-- Create trigger to update the updated_at timestamp before an update
CREATE TRIGGER before_user_update
BEFORE UPDATE ON users
FOR EACH ROW
SET NEW.updated_at = NOW();

-- Create trigger to set the deleted_at timestamp instead of deleting the user
CREATE TRIGGER after_user_delete
AFTER DELETE ON users
FOR EACH ROW
BEGIN
    -- This will mark the user as deleted by setting the deleted_at timestamp
    UPDATE users SET deleted_at = NOW() WHERE user_id = OLD.user_id;
    -- Remove the related password_resets records as they are no longer valid
    DELETE FROM password_resets WHERE user_id = OLD.user_id;
END//

-- Create trigger to validate email format before inserting a new user
CREATE TRIGGER before_user_insert
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    IF NOT NEW.email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid email format';
    END IF;
END//

-- Reset delimiter
DELIMITER ;
