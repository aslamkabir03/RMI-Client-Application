# RMI-Client-Application
Parallel Processing and Distributed System


# Distributed Attendance Management System

## Project Description

This project is a Distributed Attendance Management System developed using Java RMI and Multithreading concepts. The system allows multiple faculty members to remotely mark, update, and view student attendance records through a graphical user interface (GUI).

The application uses Java RMI architecture for communication between clients and the server. The server stores attendance records in a shared in-memory data structure and provides thread-safe access to ensure proper handling of multiple client requests simultaneously.

## Features

* Mark student attendance (Present/Absent)
* View attendance status/history of a specific student
* Calculate attendance percentage
* Display all student attendance records
* Support multiple clients simultaneously
* Thread-safe shared data management

## Technologies Used

* Java
* Java RMI
* Java Swing
* Multithreading
* HashMap

## How to Run

1. Compile all Java files.
2. Start the RMI Registry.
3. Run the RMI Server.
4. Run the Client Application.
5. Multiple clients can connect to the server simultaneously.

## Project Components

### Remote Interface

Defines all remote methods that can be accessed by clients.

### Server Implementation

Implements remote methods and manages attendance records.

### RMI Registry

Registers the remote object so that clients can locate and access the server.

### GUI Client

Provides a user-friendly interface for interacting with the server.

## Multithreading

The server handles multiple client requests concurrently. Thread-safe techniques are used to ensure data consistency when multiple clients access or modify attendance records simultaneously.

## Conclusion

This project demonstrates the practical implementation of distributed systems, remote communication, shared data management, and multithreading using Java RMI.
