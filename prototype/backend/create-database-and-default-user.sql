DROP DATABASE IF EXISTS db;
CREATE DATABASE db;
DROP USER IF EXISTS 'username'@'%';
CREATE USER 'username'@'%' identified by 'password';
GRANT ALL ON db.* to 'username'@'%';
USE db;
CREATE TABLE todos (
    id BIGINT PRIMARY KEY,
    is_completed BOOLEAN,
    description VARCHAR(255),
    title VARCHAR(255)
)  ENGINE=INNODB;