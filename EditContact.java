/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author tabas
 */
public class EditContact extends javax.swing.JFrame {

    /**
     * Creates new form EditContact
     */
    
    public EditContact(int id, String groupNames) {
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.setResizable(false);
    initComponents();
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    loadContactDetails(id);
    populateGroups(groupNames);
    
    savebtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            // Get text from input fields
            String firstName = FirstNameField.getText();
            String lastName = LastNamField.getText();
            String phone = PhoneField.getText();
            String email = emailField.getText();  
            String address = AddressField.getText();
            updateContactGroup(id); // Pass the current contact's ID


            // SQL query to update the contact
            String query = "UPDATE contacts SET first_name = ?, last_name = ?, phone_number = ?, email = ?, address = ? WHERE id = ?";

            // Execute the update query inside a try-with-resources block
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                
                // Set parameters for the prepared statement
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, phone);
                stmt.setString(4, email);
                stmt.setString(5, address);
                stmt.setInt(6, id);
               
                // Execute the update
                int rowsUpdated = stmt.executeUpdate();
                
                // Check if the update was successful
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "Contact updated successfully.");
                    setVisible(false);  // Optionally, hide the form or refresh the main contact list
                } else {
                    JOptionPane.showMessageDialog(null, "Error updating contact.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    });
}
    
   private void populateGroups(String chosenGroup) {
    String query = """
        SELECT name
        FROM (
            SELECT ? AS name, 0 AS sort_order
            UNION ALL
            SELECT 'No Group' AS name, 1 AS sort_order
            UNION ALL
            SELECT name, 2 AS sort_order
            FROM groupps
            WHERE name != ?
        ) AS groupps
        ORDER BY sort_order, name
    """;

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        // Set the chosen group parameter in the query
        pstmt.setString(1, chosenGroup);
        pstmt.setString(2, chosenGroup);

        try (ResultSet rs = pstmt.executeQuery()) {
            GroupComboBox.removeAllItems();
            while (rs.next()) {
                GroupComboBox.addItem(rs.getString("name"));
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading groups: " + ex.getMessage());
    }
}


private void loadContactDetails(int id) {
    String query = "SELECT first_name, last_name, phone_number, email, address FROM contacts WHERE id = ?";
    
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setInt(1, id);
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            FirstNameField.setText(rs.getString("first_name"));
            LastNamField.setText(rs.getString("last_name"));
            PhoneField.setText(rs.getString("phone_number"));
            emailField.setText(rs.getString("email"));
            AddressField.setText(rs.getString("address"));
          
        } else {
            JOptionPane.showMessageDialog(this, "Contact not found.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

 private Contact getContactByName(String name) {
    String query = "SELECT id, first_name, last_name, phone_number, email, address FROM contacts WHERE CONCAT(first_name, ' ', last_name) = ?";
    Contact contact = null;
    
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, name); // Set the contact's full name in the query
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String phone = rs.getString("phone_number");
            String email = rs.getString("email");
            String address = rs.getString("address");
            
            // Create and return a Contact object with all details
            contact = new Contact(id, firstName, lastName, phone, email, address);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
    return contact;
}
 
private void updateContactGroup(int contactId) {
    String selectedGroupName = (String) GroupComboBox.getSelectedItem(); // Get selected group name from ComboBox
    if (selectedGroupName == null || selectedGroupName.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a valid group.");
        return;
    }

    // SQL queries
    String getGroupIdQuery = "SELECT id FROM groupps WHERE name = ?";
    String updateGroupQuery = "UPDATE contact_groups SET group_id = ? WHERE contact_id = ?";
    String deleteGroupQuery = "DELETE FROM contact_groups WHERE contact_id = ?";
    String insertGroupQuery = "INSERT INTO contact_groups (contact_id, group_id) VALUES (?, ?)";

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
         PreparedStatement getGroupIdStmt = conn.prepareStatement(getGroupIdQuery);
         PreparedStatement updateGroupStmt = conn.prepareStatement(updateGroupQuery);
         PreparedStatement deleteGroupStmt = conn.prepareStatement(deleteGroupQuery);
         PreparedStatement insertGroupStmt = conn.prepareStatement(insertGroupQuery)) {

        // Check if the selected group is "No Group"
        if (selectedGroupName.equals("No Group")) {
            // Delete the record from contact_groups if the contact has a group assigned
            // Check if the contact already has a group
            String checkGroupQuery = "SELECT * FROM contact_groups WHERE contact_id = ?";
            try (PreparedStatement checkGroupStmt = conn.prepareStatement(checkGroupQuery)) {
                checkGroupStmt.setInt(1, contactId);
                try (ResultSet rs = checkGroupStmt.executeQuery()) {
                    if (rs.next()) {
                        // If the contact is in a group, delete the record
                        deleteGroupStmt.setInt(1, contactId);
                        deleteGroupStmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Group removed from contact.");
                    }
                }
            }
        } else {
            // Get group ID for the selected group
            getGroupIdStmt.setString(1, selectedGroupName);
            try (ResultSet rs = getGroupIdStmt.executeQuery()) {
                if (rs.next()) {
                    int groupId = rs.getInt("id"); // Retrieve group ID

                    // Check if the contact is already associated with a group
                    String checkGroupQuery = "SELECT * FROM contact_groups WHERE contact_id = ?";
                    try (PreparedStatement checkGroupStmt = conn.prepareStatement(checkGroupQuery)) {
                        checkGroupStmt.setInt(1, contactId);
                        try (ResultSet checkRs = checkGroupStmt.executeQuery()) {
                            if (checkRs.next()) {
                                // If the contact already has a group, update the record
                                updateGroupStmt.setInt(1, groupId);
                                updateGroupStmt.setInt(2, contactId);
                                int rowsUpdated = updateGroupStmt.executeUpdate();
                                if (rowsUpdated > 0) {
                                    JOptionPane.showMessageDialog(this, "Group updated for contact.");
                                } else {
                                    JOptionPane.showMessageDialog(this, "No record found to update.");
                                }
                            } else {
                                // If no record exists, insert a new record
                                insertGroupStmt.setInt(1, contactId);
                                insertGroupStmt.setInt(2, groupId);
                                insertGroupStmt.executeUpdate();
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Group not found in the database.");
                }
            }
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error updating group: " + ex.getMessage());
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jDialog3 = new javax.swing.JDialog();
        jDialog4 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        FirstNameField = new javax.swing.JTextField();
        LastNamField = new javax.swing.JTextField();
        PhoneField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();
        AddressField = new javax.swing.JTextField();
        savebtn = new javax.swing.JButton();
        cancelbtn = new javax.swing.JButton();
        GroupComboBox = new javax.swing.JComboBox<>();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(59, 30, 84));

        jLabel1.setBackground(new java.awt.Color(59, 30, 84));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Edit Contact");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel2.setText("First name");

        jLabel3.setText("Last name");

        jLabel4.setText("Phone");

        jLabel5.setText("Group");

        jLabel6.setText("Email");

        jLabel7.setText("Address");

        emailField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailFieldActionPerformed(evt);
            }
        });

        savebtn.setBackground(new java.awt.Color(212, 190, 228));
        savebtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        savebtn.setText("Save");
        savebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebtnActionPerformed(evt);
            }
        });

        cancelbtn.setBackground(new java.awt.Color(212, 190, 228));
        cancelbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cancelbtn.setText("Cancel");
        cancelbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelbtnActionPerformed(evt);
            }
        });

        GroupComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        GroupComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelbtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(savebtn))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(37, 37, 37)
                            .addComponent(FirstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7))
                            .addGap(37, 37, 37)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(LastNamField)
                                .addComponent(PhoneField)
                                .addComponent(AddressField)
                                .addComponent(GroupComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(177, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(FirstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LastNamField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(GroupComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(savebtn)
                    .addComponent(cancelbtn))
                .addGap(0, 28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void savebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebtnActionPerformed
        
    }//GEN-LAST:event_savebtnActionPerformed

    private void emailFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailFieldActionPerformed

    private void GroupComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GroupComboBoxActionPerformed

    private void cancelbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelbtnActionPerformed
        this.hide();        // TODO add your handling code here:
    }//GEN-LAST:event_cancelbtnActionPerformed

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AddressField;
    private javax.swing.JTextField FirstNameField;
    private javax.swing.JComboBox<String> GroupComboBox;
    private javax.swing.JTextField LastNamField;
    private javax.swing.JTextField PhoneField;
    private javax.swing.JButton cancelbtn;
    private javax.swing.JTextField emailField;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton savebtn;
    // End of variables declaration//GEN-END:variables
}
