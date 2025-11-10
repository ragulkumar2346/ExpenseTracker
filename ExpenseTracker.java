import java.sql.*;
import java.util.Scanner;

public class ExpenseTracker {
    private static Scanner sc = new Scanner(System.in);
    private static Connection conn;

    public static void main(String[] args) {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/expense_db", "root", "root123"
            );

            int choice = 0;
            do {
                System.out.println("\n====== Expense Tracker ======");
                System.out.println("1. Add Expense");
                System.out.println("2. View All Expenses");
                System.out.println("3. View Total Expenses");
                System.out.println("4. Filter by Category");
                System.out.println("5. Delete Expense");
                System.out.println("6. Sort by Date");
                System.out.println("7. Sort by Amount");
                System.out.println("8. Exit");
                System.out.print("Enter your choice: ");
                try {
                    choice = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number 1-8.");
                    continue;
                }

                switch (choice) {
                    case 1: addExpense(); break;
                    case 2: viewAll(); break;
                    case 3: viewTotal(); break;
                    case 4: filterByCategory(); break;
                    case 5: deleteExpense(); break;
                    case 6: sortByDate(); break;
                    case 7: sortByAmount(); break;
                    case 8: System.out.println("Thank you for using Expense Tracker!"); break;
                    default: System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 8);

            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addExpense() throws SQLException {
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine().trim();
        System.out.print("Enter Category: ");
        String category = sc.nextLine().trim();
        double amount = 0;
        while (true) {
            try {
                System.out.print("Enter Amount: ₹");
                amount = Double.parseDouble(sc.nextLine().trim());
                if (amount < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Enter a positive number.");
            }
        }
        System.out.print("Enter Note: ");
        String note = sc.nextLine().trim();

        String sql = "INSERT INTO expenses(date, category, amount, note) VALUES(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, date);
        ps.setString(2, category);
        ps.setDouble(3, amount);
        ps.setString(4, note);
        int rows = ps.executeUpdate();

        if (rows > 0) {
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("✅ Expense added successfully! (ID: " + id + ")");
            }
        } else {
            System.out.println("❌ Failed to add expense.");
        }
    }

    private static void viewAll() throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM expenses");
        System.out.println("\n------ All Expenses ------");
        boolean found = false;
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") +
                    " | Date: " + rs.getString("date") +
                    " | Category: " + rs.getString("category") +
                    " | Amount: ₹" + rs.getDouble("amount") +
                    " | Note: " + rs.getString("note"));
            found = true;
        }
        if (!found) System.out.println("No expenses found in the database.");
    }

    private static void viewTotal() throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT SUM(amount) AS total FROM expenses");
        if (rs.next()) {
            double total = rs.getDouble("total");
            System.out.println("💰 Total Expenses in database: ₹" + total);
        }
    }

    private static void filterByCategory() throws SQLException {
        System.out.print("Enter category to filter: ");
        String category = sc.nextLine().trim();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM expenses WHERE category=?");
        ps.setString(1, category);
        ResultSet rs = ps.executeQuery();

        boolean found = false;
        System.out.println("\n------ Expenses in Category: " + category + " ------");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") +
                    " | Date: " + rs.getString("date") +
                    " | Category: " + rs.getString("category") +
                    " | Amount: ₹" + rs.getDouble("amount") +
                    " | Note: " + rs.getString("note"));
            found = true;
        }
        if (!found) System.out.println("No expenses found in this category.");
    }

    private static void deleteExpense() throws SQLException {
        viewAll();
        System.out.print("Enter the ID of expense to delete: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        PreparedStatement ps = conn.prepareStatement("DELETE FROM expenses WHERE id=?");
        ps.setInt(1, id);
        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("🗑️ Expense deleted successfully! (ID: " + id + ")");
        else System.out.println("❌ Expense ID not found.");
    }

    private static void sortByDate() throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM expenses ORDER BY date ASC");
        System.out.println("\n------ Expenses Sorted by Date ------");
        boolean found = false;
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") +
                    " | Date: " + rs.getString("date") +
                    " | Category: " + rs.getString("category") +
                    " | Amount: ₹" + rs.getDouble("amount") +
                    " | Note: " + rs.getString("note"));
            found = true;
        }
        if (!found) System.out.println("No expenses found in the database.");
    }

    private static void sortByAmount() throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM expenses ORDER BY amount ASC");
        System.out.println("\n------ Expenses Sorted by Amount ------");
        boolean found = false;
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") +
                    " | Date: " + rs.getString("date") +
                    " | Category: " + rs.getString("category") +
                    " | Amount: ₹" + rs.getDouble("amount") +
                    " | Note: " + rs.getString("note"));
            found = true;
        }
        if (!found) System.out.println("No expenses found in the database.");
    }
}
