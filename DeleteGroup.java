/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author tabas
 */
public class DeleteGroup extends javax.swing.JFrame {

    /**
     * Creates new form DeleteGroup
     */
    
    private int groupid;
    public DeleteGroup(int groupid) {

        this.groupid = groupid;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
        jLabel2 = new javax.swing.JLabel();
        yesbtn = new javax.swing.JButton();
        nobtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(59, 30, 84));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Delete Group");

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
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Are you sure you want to delete this group?");

        yesbtn.setBackground(new java.awt.Color(212, 190, 228));
        yesbtn.setText("Yes");
        yesbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesbtnActionPerformed(evt);
            }
        });

        nobtn.setBackground(new java.awt.Color(212, 190, 228));
        nobtn.setText("No");
        nobtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nobtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nobtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(yesbtn))
                    .addComponent(jLabel2))
                .addGap(86, 86, 86))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel2)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yesbtn)
                    .addComponent(nobtn))
                .addGap(0, 38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void yesbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesbtnActionPerformed
         try {
            // Connect to the database
            String deleteSQLGroup = "DELETE FROM groupps WHERE id = ?";
            String deleteContactGroupsSQL = "DELETE FROM contact_groups WHERE group_id = ?";

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook", "Ayesha", "password123");
                 PreparedStatement deleteGroupStmt = connection.prepareStatement(deleteSQLGroup);
                 PreparedStatement deleteContactGroupsStmt = connection.prepareStatement(deleteContactGroupsSQL)) {

                // Set group ID for all queries
                deleteGroupStmt.setInt(1, groupid);
                deleteContactGroupsStmt.setInt(1, groupid);

                // Begin the transaction
                connection.setAutoCommit(false);

                // Execute all the queries
                int rowsAffectedGroup = deleteGroupStmt.executeUpdate();
                int rowsAffectedContactGroups = deleteContactGroupsStmt.executeUpdate();

                // Commit the transaction if all queries were successful
                if (rowsAffectedGroup > 0 && rowsAffectedContactGroups >= 0) {
                    connection.commit();
                    JOptionPane.showMessageDialog(this, "Group deleted successfully.");
                    this.dispose();
                } else {
                    connection.rollback();
                    JOptionPane.showMessageDialog(this, "Error deleting group. Please try again.");
                }

            } catch (SQLException e) {
                // Handle SQL exceptions (e.g., connection issues, query errors)
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_yesbtnActionPerformed

    private void nobtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nobtnActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_nobtnActionPerformed

    /**
     * @param args the command line arguments
     */
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton nobtn;
    private javax.swing.JButton yesbtn;
    // End of variables declaration//GEN-END:variables
}
