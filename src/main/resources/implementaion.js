/*
function useSqliteWasmOffline() {
  return new Promise(async (resolve, reject) => {
    try {
      // Initialize SQL.js with the WebAssembly binary passed as `wasmBytes`
      const SQL = await initSqlJs({
        wasmBinary: new Uint8Array(wasmfile)  // use wasmBytes as the binary data
      });

      const db = new SQL.Database();

      // Creating tables
      db.run("CREATE TABLE users (id INT, name TEXT);");
      db.run("CREATE TABLE orders (id INT, user_id INT, amount DECIMAL);");

      // Inserting data into the tables
      db.run("INSERT INTO users VALUES (1, 'John'), (2, 'Alice'), (3, 'Bob');");
      db.run("INSERT INTO orders VALUES (1, 1, 100.0), (2, 1, 200.0), (3, 2, 150.0), (4, 3, 50.0);");

      // Simple Select
      const result1 = db.exec("SELECT * FROM users;");
      console.log("Users:", result1[0].values); // Get all users

      // Join Query - Users with their orders
      const result2 = db.exec(`
        SELECT users.name, orders.amount
        FROM users
        JOIN orders ON users.id = orders.user_id;
      `);
      console.log("Users and Orders:", result2[0].values); // Join users and their orders

      // Aggregate Query - Total amount spent by each user
      const result3 = db.exec(`
        SELECT users.name, SUM(orders.amount) AS total_spent
        FROM users
        LEFT JOIN orders ON users.id = orders.user_id
        GROUP BY users.id;
      `);
      console.log("Total Spent by Users:", result3[0].values); // Sum of orders per user

      // Complex query with condition
      const result4 = db.exec(`
        SELECT name, amount
        FROM users
        JOIN orders ON users.id = orders.user_id
        WHERE amount > 100;
      `);
      console.log("Orders with amount > 100:", result4[0].values); // Orders where amount > 100

      // Subquery
      const result5 = db.exec(`
        SELECT name FROM users WHERE id IN (SELECT user_id FROM orders WHERE amount > 100);
      `);
      console.log("Users with Orders > 100:", result5[0].values); // Users who have made orders > 100

      //your database
      const binaryData = db.export();
      Polyglot.export("binaryData", binaryData);
      db.close();

      resolve(); // Resolve the promise once the queries are complete
    } catch (error) {
      console.error("Error in useSqliteWasmOffline:", error);
      reject(error); // Reject the promise if any error occurs
    }
  });
}
useSqliteWasmOffline()
  .then(() => {
    console.log("All queries executed successfully.");
  })
  .catch(error => {
    console.error("Error initializing SQLite:", error);
  });

*/

let db;
let sql;
(async () => {
SQL = await initSqlJs({ wasmBinary: new Uint8Array(wasmfile) });
db = new SQL.Database();})();

function execQuery(quer) {
  return new Promise(async (resolve, reject) => {
    try {
      //db.run("CREATE TABLE users (id INT, name TEXT);");
      //db.run("CREATE TABLE orders (id INT, user_id INT, amount DECIMAL);");

      // Inserting data into the tables
      //db.run("INSERT INTO users VALUES (1, 'John'), (2, 'Alice'), (3, 'Bob');");
      //const result2 = db.exec("INSERT INTO orders VALUES (1, 1, 100.0), (2, 1, 200.0), (3, 2, 150.0), (4, 3, 50.0);");
      //console.log(result2)
      // Simple Select
    const result1 = db.exec(quer);
    if (quer.toLowerCase().startsWith("select") && result1 !== undefined && result1.length > 0) {
        console.log(result1[0].values);
    }

      resolve(); // Resolve the promise once the queries are complete
    } catch (error) {
      console.error("Error in useSqliteWasmOffline:", error);
      reject(error); // Reject the promise if any error occurs
    }
  });
}
function close(){
      const binaryData = db.export();
      Polyglot.export("binaryData", binaryData);
      db.close();
}

