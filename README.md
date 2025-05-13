# SQLite Console Simulator

This is a simple demo that simulates an interactive SQLite console in the browser. It allows you to execute SQL commands and saves your progress as an `.sqlite` database file.

## ✨ Features

- Execute SQL queries interactively.
- Supports basic SQL operations: `CREATE`, `INSERT`, `SELECT`, etc.
- Automatically persists your session as a downloadable `.sqlite` file.

## 🧪 Example Usage

To test the demo, try executing the following SQL commands:

```sql
CREATE TABLE users (id INT, name TEXT);
INSERT INTO users VALUES (1, 'John'), (2, 'Alice'), (3, 'Bob');
SELECT * FROM users;
