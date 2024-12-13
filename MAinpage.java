
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.DefaultListModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author tabas
 */
public class MAinpage extends javax.swing.JFrame {

    /**
     * Creates new form MAinpage
     */
    public MAinpage() throws IOException {
        initComponents();
        this.setResizable(false);
        loadContacts();
        loadGroups();
        
        ContactsList.setCellRenderer(new javax.swing.ListCellRenderer<String>() {
        @Override
        public java.awt.Component getListCellRendererComponent(
            javax.swing.JList<? extends String> list,
            String value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

            // Create a JPanel for the cell
            javax.swing.JPanel panel = new javax.swing.JPanel();
            panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT)); // Align text to the left

            // Set padding inside each contact item
            panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)); // top, left, bottom, right padding

            // Add a label to display the contact name
            javax.swing.JLabel label = new javax.swing.JLabel(value);
            label.setFont(new java.awt.Font("Segoe UI", 0, 18)); // Set the font for the label
            label.setForeground(new java.awt.Color(95, 3, 115)); // Text color

            panel.add(label);

            // Add a border to each item
            if (isSelected) {
                panel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 2)); // Blue border on selection
            } else {
                panel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY, 1)); // Gray border for normal state
            }

            return panel; // Return the customized panel for each list item
        }
    });
        ContactsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = ContactsList.locationToIndex(e.getPoint()); // Get the index of the clicked item
                if (index >= 0) {
                    String selectedContact = ContactsList.getModel().getElementAt(index);
                    
                    // Get the name of the selected contact
                    
                    // Retrieve the full contact details for the selected contact (use a method to fetch details from DB)
                    Contact contact = getContactByName(selectedContact);
                    
                    int id = contact.getId();
                    
                    // Pass contact details to the ContactDetail JFrame
                    openContactDetail(contact);
                }
            }

            private void openContactDetail(Contact contact) {
                if (contact != null) {
            // Create an instance of ContactDetail JFrame and pass the contact details
            ContactDetail DisplayContactFrame = new ContactDetail(contact);
            DisplayContactFrame.setVisible(true); // Display the ContactDetail JFrame
        } else {
            JOptionPane.showMessageDialog(null, "Contact not found!");
        }
         // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            private Contact getContactByName(String selectedContact) {
                String query = "SELECT id, first_name, last_name, phone_number, email, address FROM contacts WHERE CONCAT(first_name, ' ', last_name) = ?";
    Contact contact = null;

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
         PreparedStatement stmt = conn.prepareStatement(query)) {

        // Set the contact's full name in the query
        stmt.setString(1, selectedContact);

        // Execute the query
        ResultSet rs = stmt.executeQuery();

        // If a match is found, create a Contact object
        if (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String phoneNumber = rs.getString("phone_number");
            String email = rs.getString("email");
            String address = rs.getString("address");

            // Create and return a Contact object with all details
            contact = new Contact(id, firstName, lastName, phoneNumber, email, address);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return contact;
}
            
        });
        GroupsLists.setCellRenderer(new javax.swing.ListCellRenderer<String>() {
        @Override
        public java.awt.Component getListCellRendererComponent(
            javax.swing.JList<? extends String> list,
            String value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

            // Create a JPanel for the cell
            javax.swing.JPanel panel = new javax.swing.JPanel();
            panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT)); // Align text to the left

            // Set padding inside each contact item
            panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)); // top, left, bottom, right padding

            // Add a label to display the contact name
            javax.swing.JLabel label = new javax.swing.JLabel(value);
            label.setFont(new java.awt.Font("Segoe UI", 0, 18)); // Set the font for the label
            label.setForeground(new java.awt.Color(95, 3, 115)); // Text color

            panel.add(label);

            // Add a border to each item
            if (isSelected) {
                panel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 2)); // Blue border on selection
            } else {
                panel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY, 1)); // Gray border for normal state
            }

            return panel; // Return the customized panel for each list item
        }
    });
        GroupsLists.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = GroupsLists.locationToIndex(e.getPoint()); // Get the index of the clicked item
                if (index >= 0) {
                    String selectedGroup = GroupsLists.getModel().getElementAt(index);
                    
                    // Get the name of the selected contact
                    
                    // Retrieve the full contact details for the selected contact (use a method to fetch details from DB)
                    Group group = getGroupByName(selectedGroup);
                    
                    int id = group.getId();
                    
                    // Pass contact details to the ContactDetail JFrame
                    openGroupDetail(group);
                }
            }
        });
    }
    
    private Group getGroupByName(String name) {
    String query = "SELECT id, name, description FROM groupps WHERE name = ?";
    Group group = null;
    
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, name); // Set the contact's full name in the query
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            int id = rs.getInt("id");
            String Name = rs.getString("name");
            String description = rs.getString("description");
            
            // Create and return a Contact object with all details
            group = new Group(id, Name, description);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
    return group;
}
    

    // Method to open the ContactDetail JFrame with the retrieved contact details
    private void openGroupDetail(Group group) {
        
        if (group != null) {
            // Create an instance of ContactDetail JFrame and pass the contact details
            DisplayGroup DisplayGroupFrame = new DisplayGroup(group);
            DisplayGroupFrame.setVisible(true); // Display the ContactDetail JFrame
        } else {
            JOptionPane.showMessageDialog(this, "Group not found!");
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        searchbtn = new javax.swing.JButton();
        searchfield = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        GroupsTab = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        AddContactbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ContactsList = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        GroupsLists = new javax.swing.JList<>();
        AddGroupbtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(600, 300));

        jPanel1.setBackground(new java.awt.Color(59, 30, 84));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PhoneBook");

        searchbtn.setText("Search");
        searchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbtnActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(212, 190, 228));
        jButton1.setText("Reload");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchfield, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchbtn)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchbtn)
                    .addComponent(searchfield, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        AddContactbtn.setBackground(new java.awt.Color(212, 190, 228));
        AddContactbtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AddContactbtn.setText("+");
        AddContactbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddContactbtnActionPerformed(evt);
            }
        });

        ContactsList.setBackground(new java.awt.Color(242, 242, 242));
        ContactsList.setBorder(null);
        ContactsList.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ContactsList.setForeground(new java.awt.Color(153, 0, 153));
        ContactsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(ContactsList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AddContactbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(AddContactbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        GroupsTab.addTab("Contacts", jPanel2);

        GroupsLists.setBackground(new java.awt.Color(242, 242, 242));
        GroupsLists.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        GroupsLists.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(GroupsLists);

        AddGroupbtn.setBackground(new java.awt.Color(212, 190, 228));
        AddGroupbtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AddGroupbtn.setText("+");
        AddGroupbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddGroupbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AddGroupbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AddGroupbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        GroupsTab.addTab("Groups", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(GroupsTab, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GroupsTab))
        );

        GroupsTab.getAccessibleContext().setAccessibleName("Contacts");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbtnActionPerformed

String searchText = searchfield.getText().trim();

    DefaultListModel<String> contactModel = new DefaultListModel<>();
    DefaultListModel<String> groupModel = new DefaultListModel<>();
    
    try {
        // Ensure MySQL JDBC Driver is loaded
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Establish database connection
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
        
        // Query to filter contacts
        String contactQuery = "SELECT first_name, last_name FROM contacts WHERE first_name LIKE ? OR last_name LIKE ?";
        PreparedStatement contactStmt = conn.prepareStatement(contactQuery);
        contactStmt.setString(1, "%" + searchText + "%");
        contactStmt.setString(2, "%" + searchText + "%");
        
        ResultSet contactRs = contactStmt.executeQuery();
        while (contactRs.next()) {
            String fullName = contactRs.getString("first_name") + " " + contactRs.getString("last_name");
            contactModel.addElement(fullName);
        }
        
        // Query to filter groups
        String groupQuery = "SELECT name FROM groupps WHERE name LIKE ?";
        PreparedStatement groupStmt = conn.prepareStatement(groupQuery);
        groupStmt.setString(1, "%" + searchText + "%");
        
        ResultSet groupRs = groupStmt.executeQuery();
        while (groupRs.next()) {
            String groupName = groupRs.getString("name");
            groupModel.addElement(groupName);
        }
        
        // Set the filtered models to their respective JLists
        ContactsList.setModel(contactModel); // JList for contacts
        GroupsLists.setModel(groupModel);    // JList for groups
        
        // Close resources
        contactRs.close();
        contactStmt.close();
        groupRs.close();
        groupStmt.close();
        conn.close();
        
    } catch (Exception ex) {
        Logger.getLogger(MAinpage.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_searchbtnActionPerformed

    private void AddGroupbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddGroupbtnActionPerformed
        AddGroup addgroup = new AddGroup();
        addgroup.show();// TODO add your handling code here:
    }//GEN-LAST:event_AddGroupbtnActionPerformed

    private void AddContactbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddContactbtnActionPerformed
        AddContact addcontact = new AddContact();
        addcontact.show();// TODO add your handling code here:
    }//GEN-LAST:event_AddContactbtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            loadContacts();
            loadGroups();// TODO add your handling code here:
        } catch (IOException ex) {
            Logger.getLogger(MAinpage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MAinpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MAinpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MAinpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MAinpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new MAinpage().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(MAinpage.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    
   public void loadContacts() throws IOException {
    DefaultListModel<String> model = new DefaultListModel<>();
    
    try {
        // Ensure MySQL JDBC Driver is loaded
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Establish database connection
        Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
        
        // Create a prepared statement
        String query = "SELECT first_name, last_name FROM contacts ORDER by first_name";
        Statement ps = conn.createStatement();
        
        // Execute query
        ResultSet rs = ps.executeQuery(query);
        
        // Process the result set
        while (rs.next()) {
            String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
            model.addElement(fullName);
        }
        
        // Set the model to the ContactsList (your JList component)
        ContactsList.setModel(model);
        
        // Close resources
        rs.close();
        ps.close();
        conn.close();
        
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

   
    public void loadGroups() throws IOException {
    DefaultListModel<String> model = new DefaultListModel<>();
    
    try {
        // Ensure MySQL JDBC Driver is loaded
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Establish database connection
        Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
        
        // Create a prepared statement
        String query = "SELECT name FROM groupps ORDER by name";
        Statement ps = conn.createStatement();
        
        // Execute query
        ResultSet rs = ps.executeQuery(query);
        
        // Process the result set
        while (rs.next()) {
            String name = rs.getString("name");
            model.addElement(name);
        }
        
        // Set the model to the ContactsList (your JList component)
        GroupsLists.setModel(model);
        
        // Close resources
        rs.close();
        ps.close();
        conn.close();
        
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddContactbtn;
    private javax.swing.JButton AddGroupbtn;
    private javax.swing.JList<String> ContactsList;
    private javax.swing.JList<String> GroupsLists;
    private javax.swing.JTabbedPane GroupsTab;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton searchbtn;
    private javax.swing.JTextField searchfield;
    // End of variables declaration//GEN-END:variables
}
