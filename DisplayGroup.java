
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author tabas
 */
public class DisplayGroup extends javax.swing.JFrame {

    /**
     * Creates new form DisplayGroup
     */
    public DisplayGroup(Group group) {


        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        int groupid = group.getId();
        GroupName.setText(group.getName());
        GroupDescription.setText(group.getDescription());
        loadGroupContacts(groupid);
        GroupContactLst.setCellRenderer(new javax.swing.ListCellRenderer<String>() {
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
        Reloadbtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        loadGroupContacts(groupid);
    }
});
        
        GroupContactLst.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = GroupContactLst.locationToIndex(e.getPoint()); 
                if (index >= 0) {
                    String selectedContact = GroupContactLst.getModel().getElementAt(index);
                    
                   Contact contact = getContactByName(selectedContact);
                    
                    int id = contact.getId();
                    
                    openContactDetail(contact);
                }
            }

            private void openContactDetail(Contact contact) {
                if (contact != null) {
           
            ContactDetail DisplayContactFrame = new ContactDetail(contact);
            DisplayContactFrame.setVisible(true); 
        } else {
            JOptionPane.showMessageDialog(null, "Contact not found!");
        }
         
            }

            private Contact getContactByName(String selectedContact) {
                String query = "SELECT id, first_name, last_name, phone_number, email, address FROM contacts WHERE CONCAT(first_name, ' ', last_name) = ?";
    Contact contact = null;

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
         PreparedStatement stmt = conn.prepareStatement(query)) {

        
        stmt.setString(1, selectedContact);

        
        ResultSet rs = stmt.executeQuery();

        
        if (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String phoneNumber = rs.getString("phone_number");
            String email = rs.getString("email");
            String address = rs.getString("address");

            
            contact = new Contact(id, firstName, lastName, phoneNumber, email, address);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return contact;
}
            
        });
        
        DeleteGroupBtn.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            
            new DeleteGroup(groupid).setVisible(true);
        }
    });
        EditgroupBtn.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
       
        new EditGroup(groupid).setVisible(true);
    }
        
         
});
        
    }
    
  public void loadGroupContacts(int groupId) {
    DefaultListModel<String> model = new DefaultListModel<>();

    try {
        
        Class.forName("com.mysql.cj.jdbc.Driver");

        
        Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");

        String query = "SELECT c.first_name, c.last_name FROM contacts c " + "JOIN contact_groups cg ON c.id = cg.contact_id " + "WHERE cg.group_id = ? ORDER BY c.first_name";

        PreparedStatement ps = conn.prepareStatement(query);

        ps.setInt(1, groupId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
            model.addElement(fullName);
        }

         GroupContactLst.setModel(model);

        
        rs.close();
        ps.close();
        conn.close();

    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
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
        GroupName = new javax.swing.JLabel();
        GroupDescription = new javax.swing.JLabel();
        EditgroupBtn = new javax.swing.JButton();
        DeleteGroupBtn = new javax.swing.JButton();
        Reloadbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        GroupContactLst = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(59, 30, 84));

        GroupName.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        GroupName.setForeground(new java.awt.Color(255, 255, 255));
        GroupName.setText("GroupName");

        GroupDescription.setForeground(new java.awt.Color(255, 255, 255));
        GroupDescription.setText("Group Description");

        EditgroupBtn.setBackground(new java.awt.Color(212, 190, 228));
        EditgroupBtn.setText("Edit");

        DeleteGroupBtn.setBackground(new java.awt.Color(212, 190, 228));
        DeleteGroupBtn.setText("Delete");

        Reloadbtn.setBackground(new java.awt.Color(212, 190, 228));
        Reloadbtn.setText("Reload");
        Reloadbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReloadbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(GroupDescription)
                    .addComponent(GroupName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
                .addComponent(Reloadbtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DeleteGroupBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(EditgroupBtn)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(GroupName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(GroupDescription))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(EditgroupBtn)
                            .addComponent(DeleteGroupBtn)
                            .addComponent(Reloadbtn))))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        GroupContactLst.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(GroupContactLst);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ReloadbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReloadbtnActionPerformed
              
// TODO add your handling code here:
    }//GEN-LAST:event_ReloadbtnActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DeleteGroupBtn;
    private javax.swing.JButton EditgroupBtn;
    private javax.swing.JList<String> GroupContactLst;
    private javax.swing.JLabel GroupDescription;
    private javax.swing.JLabel GroupName;
    private javax.swing.JButton Reloadbtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
