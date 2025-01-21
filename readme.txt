This document provides all the necessary details about the User and Course Management System application, developed using JavaFX and SQL. The application is designed for managing users, courses, and assignments in a user-friendly graphical interface.

Description
The User and Course Management System is a desktop application aimed at simplifying administrative tasks for educational institutions. It allows administrators to manage users (such as students, professors, and staff) and courses efficiently. The system offers a secure login functionality and persistent data storage using a SQL database.

Features
The application includes several features that make it functional and versatile.

User Management: Administrators can add, update, and delete user accounts with detailed attributes such as name, surname, phone number, and IBAN.
Course Management: Administrators can create, modify, or delete courses and assign professors to them. The system also allows viewing enrolled students.
Secure Login: Users must log in using a username and password to access the application.
Search and Filter: Courses and users can be searched using the respective search bars for quick access.
Technologies Used
The application is developed using Java for programming, JavaFX for the graphical interface, and SQL for database management.

Installation Instructions
To set up the application, follow these steps:

Install Required Tools: Ensure you have Java JDK 8 or above installed on your system. Use an IDE such as IntelliJ IDEA or Eclipse for the source code.
Database Setup: Configure your SQL database (e.g., MySQL or PostgreSQL) and import the provided database schema. Update the database credentials in the application code.
Source Code: Download the project source files and open them in your chosen IDE.
Run the Application: Build the project and execute it to launch the application. You will be greeted with the login screen as the starting interface.
How to Use the Application
Once the application is installed and running, follow these steps to use its functionality:

Login: Enter your username and password in the fields provided and click "Login" to access the system.
Managing Users: To add a user, fill in the required fields and click "Insert User." To modify a user's details, select the user from the table, make the changes, and click "Modify User." To delete a user, select them from the table and click "Delete User."
Managing Courses: To add a course, fill in the course details such as name, type, and professor, then click "Insert Course." Professors can be assigned to courses by using the "Assign Professor to Course" button. Use the search bar to locate specific courses.
Logout: Click the "Logout" button at the top-right corner of the screen to exit the application securely.
Database Structure
The database consists of two main tables.

Users Table: Stores user details like name, username, email, and phone number.
Courses Table: Contains course details such as name, type, and maximum number of students.
Relationships exist between these tables for user-course assignments.
Future Development Ideas
Several enhancements can be added to the application in the future:

A notification system to send updates to users.
Two-factor authentication for improved security.
A report generation feature to export data to PDF or Excel.
A calendar for scheduling classes and events.
Multi-language support to make the application accessible to a wider audience