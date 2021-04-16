CREATE DATABASE db;
CREATE USER 'username'@'%' identified by 'password';
GRANT ALL ON db.* to 'username'@'%';