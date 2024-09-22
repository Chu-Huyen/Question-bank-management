/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Database.DBConnection;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author ADMIN
 */
public class Home extends javax.swing.JFrame {

    /**
     * Creates new form Homes
     * @param email
     */
    DBConnection connection;
    Connection con;
    Statement stm;
    ResultSet rs;
    String maCH="";
    ArrayList<Integer> list=new ArrayList<>();
    int row;
    static String email;
    public Home(String email) {
        initComponents();
        connection=new DBConnection();
        Home.email=email;
        hienthi();
        listUser();
        getrow();
        cbFilter.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
        Connection conn;
        try{
            if(cbFilter.getSelectedItem().equals("Tất cả")){
                hienthi();
                getrow();
        } 
        else{
            String user=cbFilter.getSelectedItem().toString();
            conn=connection.DBConnection();
            String sql="select * from QuestInfor where Username='"+user+"' and (QuestTitle like '%"+txtTimkiem.getText()+"%' or QuestTag like '%"+txtTimkiem.getText()+"%')";
            stm=conn.createStatement();
            rs=stm.executeQuery(sql);
            int i=1;
            DefaultTableModel dtm=new DefaultTableModel();
            dtm.addColumn("STT");
            dtm.addColumn("Mã câu hỏi");
            dtm.addColumn("Tag");
            dtm.addColumn("Tiêu đề");
            dtm.addColumn("Nội dung");
            dtm.addColumn("Người đăng");
            dtm.addColumn("Báo cáo");
            dtm.setRowCount(0);
            while(rs.next()){
                dtm.addRow(new Object[]{
                    i,
                    rs.getString("QuestID"),
                    rs.getString("QuestTag"),
                    rs.getString("QuestTitle"),
                    rs.getString("QuestContent"),
                    rs.getString("Username"),
                    rs.getString("Report")
                });
                i++;
            }
            tbView.setModel(dtm);
            getrow();
        }
        }catch(ClassNotFoundException|SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }       
            }
        });
        txtTimkiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                    try {
            // TODO add your handling code here:
            con=connection.DBConnection();
            stm=con.createStatement();
            String tim=txtTimkiem.getText();
            String sql;
            if(cbFilter.getSelectedItem().toString().equals("Tất cả")) sql="select * from QuestInfor where (QuestTitle like '%"+tim+"%' or QuestTag like '%"+tim+"%')";
            else sql="select * from QuestInfor where (QuestTitle like '%"+tim+"%' or QuestTag like '%"+tim+"%') and Username='"+cbFilter.getSelectedItem().toString()+"'";
            ResultSet rs=stm.executeQuery(sql);
            int i=0;
            DefaultTableModel dtm=new DefaultTableModel();
            dtm.addColumn("STT");
            dtm.addColumn("Mã câu hỏi");
            dtm.addColumn("Tag");
            dtm.addColumn("Tiêu đề");
            dtm.addColumn("Nội dung");
            dtm.addColumn("Người đăng");
            dtm.addColumn("Báo cáo");
            list.clear();
            dtm.setRowCount(0);
            while(rs.next()){
                dtm.addRow(new Object[]{
                    i,
                    rs.getString("QuestID"),
                    rs.getString("QuestTag"),
                    rs.getString("QuestTitle"),
                    rs.getString("QuestContent"),
                    rs.getString("Username"),
                    rs.getString("Report")
                });
                i++;
                list.add(Integer.parseInt(rs.getString("Report")));
            }
            tbView.setModel(dtm);
            row=tbView.getRowCount();
            lbPage.setText("1/"+row);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
                }
        });
        tbView.setAutoCreateRowSorter(true);
        tbView.getSelectionModel().setSelectionInterval( 0, 0 );
        tbView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tbView.getSelectedRow()>=0){
                    DefaultTableModel dtm=(DefaultTableModel)tbView.getModel();
                    if(tbView.getSelectedRow()==row) maCH=dtm.getValueAt(0, 0).toString();
                    else maCH=dtm.getValueAt(tbView.getSelectedRow(), 1).toString();
                }
                else{
                    maCH="";
                }
            }
        });
    }

    private void getrow(){
        row=tbView.getRowCount();
        lbPage.setText("1/"+row);
    }
    private void listUser(){
        try{
            cbFilter.removeAll();
            con=connection.DBConnection();
            String sql="select * from UserInfor";
            stm=con.createStatement();
            rs=stm.executeQuery(sql);
            cbFilter.addItem("Tất cả");
            while(rs.next()){
                cbFilter.addItem(rs.getString("Username"));
            }
            connection.Close();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(rootPane, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void hienthi(){
        try{
            con=connection.DBConnection();
            int i=1;
            String sql="select * from QuestInfor";
            stm=con.createStatement();
            rs=stm.executeQuery(sql);
            DefaultTableModel dtm=new DefaultTableModel();
            dtm.addColumn("STT");
            dtm.addColumn("Mã câu hỏi");
            dtm.addColumn("Tag");
            dtm.addColumn("Tiêu đề");
            dtm.addColumn("Nội dung");
            dtm.addColumn("Người đăng");
            dtm.addColumn("Báo cáo");
            list.clear();
            dtm.setRowCount(0);
            while(rs.next()){
                dtm.addRow(new Object[]{
                    i,
                    rs.getString("QuestID"),
                    rs.getString("QuestTag"),
                    rs.getString("QuestTitle"),
                    rs.getString("QuestContent"),
                    rs.getString("Username"),
                    rs.getString("Report")
                });
                i++;
                list.add(Integer.parseInt(rs.getString("Report")));
            }
            tbView.setModel(dtm);
            connection.Close();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(rootPane, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tbView = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnView = new javax.swing.JButton();
        cbFilter = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtTimkiem = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnDau = new javax.swing.JButton();
        btnTruoc = new javax.swing.JButton();
        lbPage = new javax.swing.JLabel();
        btnSau = new javax.swing.JButton();
        btnCuoi = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Trang chủ");
        setResizable(false);

        tbView.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tbView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbView.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbView);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N

        btnView.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnView.setText("Xem câu hỏi");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        cbFilter.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel1.setText("Lọc");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setText("Tìm kiếm");

        btnDau.setText("<<");
        btnDau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDauActionPerformed(evt);
            }
        });

        btnTruoc.setText("<");
        btnTruoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTruocActionPerformed(evt);
            }
        });

        lbPage.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbPage.setText("jLabel3");

        btnSau.setText(">");
        btnSau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSauActionPerformed(evt);
            }
        });

        btnCuoi.setText(">>");
        btnCuoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCuoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnView)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDau, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTruoc, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSau, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimkiem)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnView)
                    .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPage)
                    .addComponent(btnTruoc)
                    .addComponent(btnDau)
                    .addComponent(btnSau)
                    .addComponent(btnCuoi))
                .addContainerGap())
        );

        jMenuBar1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jMenu2.setText("Quản lý tài khoản");
        jMenu2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jMenuItem1.setText("Quản lý tài khoản");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jMenuItem2.setText("Đăng xuất");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tbViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbViewMouseClicked
        // TODO add your handling code here:
        DefaultTableModel dtm=(DefaultTableModel)tbView.getModel();
        if(tbView.getSelectedRow()==row) {
            maCH=dtm.getValueAt(0, 0).toString();
        }
        else maCH=dtm.getValueAt(tbView.getSelectedRow(), 1).toString();
        lbPage.setText((tbView.getSelectedRow()+1)+"/"+row);
    }//GEN-LAST:event_tbViewMouseClicked

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        try{
            if(maCH.isBlank()){
                JOptionPane.showMessageDialog(rootPane, "Chưa chọn câu hỏi");
            }
            else{
                ChitietCH chitietCH=new ChitietCH(maCH, email);
                //this.setVisible(false);
                chitietCH.setVisible(true);
            }
        }catch(HeadlessException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_btnViewActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        Login l=new Login();
        this.setVisible(false);
        l.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        if(email.equals("admin@gmail.com")){
            Admin admin=new Admin();
            this.setVisible(false);
            admin.setVisible(true);
        }
        else{
            User u=new User(email);
            this.setVisible(false);
            u.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnSauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSauActionPerformed
        // TODO add your handling code here:
        int dongchon=tbView.getSelectedRow();
        tbView.getSelectionModel().setSelectionInterval(row, dongchon+1);
        if(dongchon==row-1){
            tbView.getSelectionModel().setSelectionInterval(row, 0);
        }
        lbPage.setText((tbView.getSelectedRow()+1)+"/"+row);
    }//GEN-LAST:event_btnSauActionPerformed

    private void btnCuoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCuoiActionPerformed
        // TODO add your handling code here:
        tbView.getSelectionModel().setSelectionInterval(0, row-1);
        lbPage.setText((tbView.getSelectedRow()+1)+"/"+row);
    }//GEN-LAST:event_btnCuoiActionPerformed

    private void btnTruocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTruocActionPerformed
        // TODO add your handling code here:
        int dongchon=tbView.getSelectedRow();
        tbView.getSelectionModel().setSelectionInterval(row, dongchon-1);
        if(dongchon==0){
            tbView.getSelectionModel().setSelectionInterval(0, row-1);
        }
        lbPage.setText((tbView.getSelectedRow()+1)+"/"+row);
    }//GEN-LAST:event_btnTruocActionPerformed

    private void btnDauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDauActionPerformed
        // TODO add your handling code here:
        tbView.getSelectionModel().setSelectionInterval(row, 0);
        lbPage.setText((tbView.getSelectedRow()+1)+"/"+row);
    }//GEN-LAST:event_btnDauActionPerformed

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Home(email).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCuoi;
    private javax.swing.JButton btnDau;
    private javax.swing.JButton btnSau;
    private javax.swing.JButton btnTruoc;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbPage;
    private javax.swing.JTable tbView;
    private javax.swing.JTextField txtTimkiem;
    // End of variables declaration//GEN-END:variables
}
