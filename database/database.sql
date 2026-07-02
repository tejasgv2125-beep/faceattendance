CREATE DATABASE faceattendance;
USE faceattendance;

SHOW TABLES;
DESCRIBE admins;
DESCRIBE students;
DESCRIBE attendance;
DELETE FROM admins;
SELECT * FROM admins;
SELECT id,
       name,
       face_descriptor
FROM students;