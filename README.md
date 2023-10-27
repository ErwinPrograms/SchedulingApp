# Scheduling Desktop Application

This is a GUI-based Java desktop application made for scheduling across multiple timezones and languages with a persistent database.

## Table of contents
- [Environment](#environment)
- [How to run](#how-to-run)
- [Goals](#goals)
- [Features](#features)
- [Author](#author)

---

## Environment
- IntelliJ IDEA 2022.2.2 (Community Edition)
- OpenJDK 11+
- mysql-connector-java-8.0.30 (included in repository)
- JavaFX-SDK-17

---

## How to run
To use this repo:
- Ensure Java FX 17 and JDK 11 are on your machine
- In IntelliJ create a new project from existing sources
  - Use the url "https://github.com/ErwinPrograms/SchedulingApp"
- Ensure path "PATH_TO_FX" is set
  - File -> Settings: Path Variables
  - Make sure the path exists and is pointing to the directory for Java FX library
- Set the SDK version
  - File -> Project Structure : Project
  - SDK: Version 11
  - Language level: SDK default
- Add the lib directory for Java FX
  - File -> Project Structure : Libraries
  - Add library pointing to the directory for Java FX
- Add run configuration
  - Run -> Edit Configurations: Application configuration
  - Main class: main.Main
  - Modify options -> Add VM options
  - Inside VM options add "--module-path ${PATH_TO_FX} --add-modules javafx.fxml,javafx.controls,javafx.graphics"
- Adjust code to work with your database 
  - main.Main.java:32 adjust DBConnection.makeConnection(url, username, password) to your database credentials
---

## Goals
Create a GUI-based application, which allows log-in, customer records, scheduling, and user activity tracking.

Maintain persistent data by using a MySQL database with pre-defined ER diagram. Interact in the business layer through the use JDBC for CRUD operations.

Track user activity and save to local root folder of the application.

Document code with Javadoc comments and index.html file based on Oracle's guidelines for Javadoc tool best practices.

---

## Features
Ability to create, read, update, and delete customers and appointments from a database using a graphical user interface.

The ability to read three separate reports using aggregate data from different tables of the database:
- Appointment counts sorted by type and date
- The schedule of any contact's appointments
- The count of appointments grouped by division

---

## Author
Erwin, a plain old Java developer.

Github: https://github.com/ErwinPrograms

Linkedin: https://www.linkedin.com/in/erwin-programs/

---