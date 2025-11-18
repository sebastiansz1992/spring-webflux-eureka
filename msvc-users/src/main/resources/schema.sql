-- MySQL Schema for Users Table
-- Database: msvc_users

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    email VARCHAR(100) NOT NULL UNIQUE,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comments for documentation
-- id: Primary key, auto-incrementing
-- username: Unique username, max 50 characters
-- password: User password (should be encrypted), max 255 characters to accommodate hashed passwords
-- enabled: Boolean flag (0 = disabled, 1 = enabled)
-- email: Unique email address, max 100 characters
-- Indexes on username and email for faster lookups
