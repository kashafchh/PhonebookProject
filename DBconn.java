import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBconn {
            public static Connection conn = null;

    public static Connection connect() {
        String url = "jdbc:mysql://localhost:3306/phonebook";
        String user = "Ayesha"; // Or your new username
        String password = "password123";

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database Connected successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection failed: " + e.getMessage());
        }
        return conn;
    }
}
