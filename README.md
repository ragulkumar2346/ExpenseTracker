# Expense Tracker

A **simple and user-friendly Java application** to track and manage daily expenses.  
This project uses **Java Collections, JDBC, and MySQL** for persistent storage.

---

## **Features**

- Add expenses with **date, category, amount, and notes**  
- View **all recorded expenses**  
- Calculate **total expenses**  
- **Filter expenses** by category  
- **Delete expenses** by ID  
- **Sort expenses** by date or amount  
- Persistent storage with **MySQL database**

---

## **Technologies Used**

- **Java** – Core programming  
- **JDBC (Java Database Connectivity)** – Connects Java application to MySQL  
- **MySQL** – Database for storing expenses  

---

## **Setup Instructions**

### 1️⃣ Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/ExpenseTracker.git
2️⃣ Create MySQL Database
Open MySQL CLI or Workbench and run:

sql
Copy code
CREATE DATABASE expense_db;

USE expense_db;

CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE,
    category VARCHAR(50),
    amount DOUBLE,
    note VARCHAR(255)
);
3️⃣ Update MySQL Credentials
In ExpenseTracker.java, make sure your database URL, username, and password are correct:

java
Copy code
Connection conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/expense_db", "root", "root123"
);
4️⃣ Compile and Run
Make sure the MySQL Connector JAR is in your project folder:

bash
Copy code
javac -cp ".;mysql-connector-j-9.5.0.jar" ExpenseTracker.java
java -cp ".;mysql-connector-j-9.5.0.jar" ExpenseTracker
Usage
Run the application in terminal.

Choose options from the menu:

Add expense

View all expenses

View total

Filter by category

Delete expense

Sort by date or amount

Expenses are saved directly to MySQL and can be retrieved anytime.

