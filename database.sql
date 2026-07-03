CREATE DATABASE faceattendance;
USE faceattendance;

SHOW TABLES;
DESCRIBE admins;
DESCRIBE students;
DESCRIBE attendance;
DELETE FROM admins;
SELECT * FROM admins;
SELECT id, name, photo_path, face_descriptor
FROM students;