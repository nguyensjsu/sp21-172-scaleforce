DROP DATABASE IF EXISTS auth;
CREATE DATABASE auth;
DROP USER IF EXISTS 'username'@'%';
CREATE USER 'username'@'%' identified by 'password';
GRANT ALL ON auth.* to 'username'@'%';
USE auth;