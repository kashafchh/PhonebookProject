import java.sql.PreparedStatement;
import javax.swing.JOptionPane;


public class ContactDAO {

    public boolean insertContact(Contact contact) {
        try {
            String query = "INSERT INTO contacts VALUES (null, ?, ?, ?, ?, ?)";
            PreparedStatement ps = DBconn.conn.prepareStatement(query);
            
            ps.setString(1, contact.getfName());
            ps.setString(2, contact.getlName());
            ps.setString(3, contact.getPhone());
            ps.setString(4, contact.getEmail());
            ps.setString(5, contact.getAddress());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Contact added successfully!");
                MAinpage mainpage = new MAinpage();
                mainpage.loadContacts();
                return true; // Success
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred while inserting data: " + e.getMessage());
        }
        return false; // Failure
    }
}
