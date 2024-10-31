# RMI Bag of Tasks Project

This project is a distributed task management system based on Java RMI (Remote Method Invocation). It is designed to handle, execute, and manage tasks such as calculating Fibonacci sequences across a network. The tasks are managed by a server and executed by worker threads, with results stored in separate databases.

## Project Summary

The RMI Bag of Tasks project provides a client-server architecture to submit and manage computing tasks remotely. Clients can create, update, query, or delete tasks, and workers on the server execute these tasks asynchronously. Task metadata and results are stored in two separate databases, ensuring a structured and organized data management approach.

## Requirements

- **Java**: Make sure Java is installed and configured (`javac` and `java` should be accessible).
- **Database Driver**: If using Oracle, `ojdbc8.jar` is required. For other databases, download the appropriate JDBC driver and add it to the project directory.
- **Database Setup**: Create two separate databases (`Eluard` and `Butor`) or use any two distinct databases (e.g., MySQL as tested locally). The SQL schema for the database is provided in `db.sql`.

## Setting Up the Project at University

If accessing the system from the university machines:

1. **Login to the University Server**:
   ```bash
   ssh <login>@eluard
   ```

   Replace `<login>` with your actual university login.

2. **Access Oracle Database**:
   Load the Oracle environment with:
   ```bash
   . /opt/oraenv.sh
   ```
   Then, start `sqlplus`:
   ```bash
   sqlplus
   ```
   Log in with your Oracle username and password.

## Setting Up Locally

If running locally, ensure you have two distinct databases (like MySQL databases as tested) and configure the constants in `DatabaseHelper.java` accordingly.

1. Import `db.sql` into both databases to set up the tables.
2. Download the appropriate JDBC driver (e.g., MySQL Connector for MySQL) and place it in the project directory.
3. Update `DatabaseHelper.java` with your local database credentials and JDBC URL.

## Compiling and Running the Project

### Compilation

Compile all necessary classes:
```bash
javac -cp ojdbc8.jar:. interfaces/*.java server/*.java worker/*.java client/*.java tasks/*.java database/*.java
```

Replace `ojdbc8.jar` with your database’s JDBC driver if necessary.

### Starting the Server

To start the server, run:
```bash
java -cp ojdbc8.jar:. server.ServerApp
```

This starts the RMI registry and waits for client connections. Ensure `rmiregistry` is running on port 1099 (or update as needed).

### Starting the Client

To start the client and interact with the server:
```bash
java -cp ojdbc8.jar:. client.Client
```

Replace `ojdbc8.jar` with the correct database driver if using a non-Oracle database.

## Database Schema

The database schema file `db.sql` is included in this repository. It contains the definitions for both `TASK_RESULTS` (for task metadata) and `FIBONACCI_SEQUENCE` (for computed Fibonacci sequences). Import this file into your databases before starting the server.

## Notes

- Modify `DatabaseHelper.java` with appropriate credentials if you change databases.
- This project assumes two separate databases are available for storing metadata and task results.
- Consult the GitHub repository for further documentation and updates.
