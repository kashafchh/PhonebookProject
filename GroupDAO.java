/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
/**
 *
 * @author tabas
 */
public class GroupDAO {
    public boolean insertGroup(Group group) {
    try {
        String query = "INSERT INTO `groupps` (name, description) VALUES (?, ?)";
        PreparedStatement ps = DBconn.conn.prepareStatement(query);

        // Set the group attributes
        ps.setString(1, group.getName());
        ps.setString(2, group.getDescription());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Group added successfully!");
            return true; // Success
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error occurred while inserting group: " + e.getMessage());
    }
    return false; // Failure
}

}
