# Two-Tier Client-Server Application Using MySQL and JDBC Project Overview
Developed a Java-based two-tier client-server application using MySQL and JDBC. The project involves creating a GUI front-end that allows different client users to execute SQL commands based on their permissions. A separate accountant-level application will query a transaction-logging database.

## 🎯 Objectives
- Develop a two-tier client-server application using Java and MySQL.
- Implement JDBC for database connectivity.
- Create a user-friendly GUI for executing SQL commands.
- Develop a specialized accountant interface for monitoring transaction logs.

## 📋 Requirements
- Create two Java applications:
  - **Main Application** – Allows general users to execute DDL and DML commands based on permissions.
  - **Accountant Application** – Allows accountant-level users to query the operations log.
- Maintain user credentials and database URLs in properties files.
- Verify user credentials before establishing a connection.
- Log executed commands and store them in a separate `operationslog` database.

## 🛠️ Setup
### 1. Create and Populate Databases
- Run `project3dbscript.sql` to create the `project3` database.
- Run `project3operationslog.sql` to create the `operationslog` database.

### 2. Create Client-Level Users
- Execute `clientCreationScriptProject3.sql` to create the following users:
  - `client1`
  - `client2`
  - `project3app`
  - `theaccountant`

### 3. Assign Permissions
- Execute `clientPermissionsScriptProject3.sql` to assign correct permissions:
  - `client1` – `SELECT` on `project3` and `bikedb`
  - `client2` – `SELECT`, `UPDATE` on `project3` and `bikedb`
  - `project3app` – `SELECT`, `INSERT`, `UPDATE` on `operationslog`
  - `theaccountant` – `SELECT` on `operationslog`

## ⚙️ Functionality
- User authentication through properties files.
- Execution of DDL and DML commands through the GUI.
- Real-time display of query results in the GUI.
- Separate logging of executed commands in the `operationslog` database.

## 📦 Deliverables
1. **Source Code** – All `.java` files
2. **Root Command Screenshots** – 17 screenshots from `project3rootscript.sql`.
3. **Client1 Command Screenshots** – 11 screenshots from `project3client1script.sql`.
4. **Client2 Command Screenshots** – 11 screenshots from `project3client2script.sql`.
5. **Accountant-OperationsLog Screenshots** – 3 screenshots:
   - Results of `SELECT num_queries FROM operationscount WHERE login_username = "root@localhost";`
   - Results of `SELECT * FROM operationscount;`
   - Workbench result of `SELECT * FROM operationscount;`
6. **Credentials Mismatch Screenshot** – One screenshot showing a login failure.

## 📝 Notes
- Ensure all user credentials are stored securely in properties files.
- Implement proper exception handling for database connectivity.
- Use `PreparedStatement` for executing SQL commands.
