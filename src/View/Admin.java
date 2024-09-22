/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Database.DBConnection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class Admin extends javax.swing.JFrame {

    /**
     * Creates new form Admin
     */
    int rowTK,rowCH,rowTL;
    DBConnection connection;
    Connection con;
    Statement stm;ResultSet rs;
    DefaultTableModel dtm;
    String maCH,maTL;
    public Admin() {
        try {
            initComponents();
            connection=new DBConnection();
            con=connection.DBConnection();
            setDefaultCloseOperation(Admin.HIDE_ON_CLOSE);
            setting();
            tbViewTK.setModel(TK(bang("select* from UserInfor")));
            tbViewCH.setModel(CH(bang("select* from QuestInfor")));
            tbViewTL.setModel(TL(bang("select* from AnswerInfor")));
            rowTK=tbViewTK.getRowCount();rowCH=tbViewCH.getRowCount();rowTL=tbViewTL.getRowCount();
            getrow(lbPageTK, rowTK);
            getrow(lbPageCH, rowCH);
            getrow(lbPageTL, rowTL);
            settable(tbViewTK);settable(tbViewCH);settable(tbViewTL);
            dtm=(DefaultTableModel)tbViewTK.getModel();
            txtTK.setText(dtm.getValueAt(0, 1).toString());
            txtEmailTK.setText(dtm.getValueAt(0, 2).toString());
            txtPassTK.setText(dtm.getValueAt(0, 3).toString());
            maCH=tbViewCH.getValueAt(tbViewCH.getSelectedRow(), 1).toString();
            maTL=tbViewTL.getValueAt(tbViewTL.getSelectedRow(), 1).toString();
            txtTimTK.getDocument().addDocumentListener(new DocumentListener() {
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
                        stm=con.createStatement();
                        String tim=txtTimTK.getText();
                        String sql="select * from UserInfor where Username like '%"+tim+"%' or UserEmail like '%"+tim+"%'";
                        tbViewTK.setModel(TK(bang(sql)));
                        rowTK=tbViewTK.getRowCount();
                        tbViewTK.getSelectionModel().setSelectionInterval( 0, 0 );
                        lbPageTK.setText("1/"+rowTK);
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            txtTimCH.getDocument().addDocumentListener(new DocumentListener() {
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
                        stm=con.createStatement();
                        String tim=txtTimCH.getText();
                        String sql="select * from QuestInfor where QuestTitle like '%"+tim+"%' or QuestTag like '%"+tim+"%'";
                        tbViewCH.setModel(CH(bang(sql)));
                        rowCH=tbViewCH.getRowCount();
                        tbViewCH.getSelectionModel().setSelectionInterval( 0, 0 );
                        lbPageCH.setText("1/"+rowCH);
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            txtTimTL.getDocument().addDocumentListener(new DocumentListener() {
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
                        stm=con.createStatement();
                        String tim=txtTimTL.getText();
                        String sql="select * from AnswerInfor where Answer like '%"+tim+"%' or QuestID like '%"+tim+"%'";
                        tbViewTL.setModel(TL(bang(sql)));
                        rowTL=tbViewTL.getRowCount();
                        tbViewTL.getSelectionModel().setSelectionInterval( 0, 0 );
                        lbPageTL.setText("1/"+rowTL);
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            tbViewTK.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if(tbViewTK.getSelectedRow()>=0){
                        DefaultTableModel dtm=(DefaultTableModel)tbViewTK.getModel();
                        if(tbViewTK.getSelectedRow()!=rowTK){
                            txtTK.setText(dtm.getValueAt(tbViewTK.getSelectedRow(), 1).toString());
                            txtEmailTK.setText(dtm.getValueAt(tbViewTK.getSelectedRow(), 2).toString());
                            txtPassTK.setText(dtm.getValueAt(tbViewTK.getSelectedRow(), 3).toString());
                        }
                        else {
                            txtTK.setText(dtm.getValueAt(0, 1).toString());
                            txtEmailTK.setText(dtm.getValueAt(1, 2).toString());
                            txtPassTK.setText(dtm.getValueAt(2, 3).toString());
                        }
                    }
                    else{
                        txtTK.setText("");
                        txtEmailTK.setText("");
                        txtPassTK.setText("");
                    }
                    try {
                        stm=con.createStatement();
                        rs=stm.executeQuery("select count(*) from QuestInfor where Username='"+txtTK.getText()+"'");
                        while(rs.next()) txtCountQ.setText(rs.getString(1));
                        stm=con.createStatement();
                        rs=stm.executeQuery("select count(*) from AnswerInfor where Username='"+txtTK.getText()+"'");
                        while(rs.next()) txtCountA.setText(rs.getString(1));
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //hien thi du lieu len cac bang
    private ResultSet bang(String sql){
        try {
            stm=con.createStatement();
            rs=stm.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    private DefaultTableModel TK(ResultSet RSet){
        try {
            stm=con.createStatement();
            dtm=new DefaultTableModel();
            int i=1;
            dtm.addColumn("STT");
            dtm.addColumn("Tên người dùng");
            dtm.addColumn("Email");
            dtm.addColumn("Mật khẩu");
            dtm.setRowCount(0);
            while(RSet.next()){
                dtm.addRow(new Object[]{
                    i,
                    RSet.getString(1),
                    RSet.getString(2),
                    RSet.getString(3)
                });
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dtm;
    }
    private DefaultTableModel CH(ResultSet RSet){
        try {
            dtm=new DefaultTableModel();
            int i=1;
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
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dtm;
    }
    private DefaultTableModel TL(ResultSet RSet){
        try {
            dtm=new DefaultTableModel();
            int i=1;
            dtm.addColumn("STT");
            dtm.addColumn("Mã câu trả lời");
            dtm.addColumn("Người đăng");
            dtm.addColumn("Mã câu hỏi");
            dtm.addColumn("Nội dung");
            dtm.addColumn("Báo cáo");
            dtm.setRowCount(0);
            while(rs.next()){
                dtm.addRow(new Object[]{
                    i,
                    rs.getString("AnswerID"),
                    rs.getString("Username"),
                    rs.getString("QuestID"),
                    rs.getString("Answer"),
                    rs.getString("Report")
                });
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dtm;
    }
    
    private void settable(JTable table){
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().setSelectionInterval( 0, 0 );
    }
    //lay so ban ghi cua moi bang
    private void getrow(JLabel label, int row){
        label.setText("1/"+row);
    }
    private void first(JTable table,JLabel label, int row){
        table.getSelectionModel().setSelectionInterval(row,0);
        label.setText((table.getSelectedRow()+1)+"/"+row);
    }
    private void before(JTable table,JLabel label, int row){
        int dongchon=table.getSelectedRow();
        table.getSelectionModel().setSelectionInterval(row, dongchon-1);
        if(dongchon==0){
            table.getSelectionModel().setSelectionInterval(0, row-1);
        }
        label.setText((table.getSelectedRow()+1)+"/"+row);
    }
    private void after(JTable table,JLabel label,int row){
        int dongchon=table.getSelectedRow();
        table.getSelectionModel().setSelectionInterval(row, dongchon+1);
        if(dongchon==row-1){
            table.getSelectionModel().setSelectionInterval(row, 0);
        }
        label.setText((table.getSelectedRow()+1)+"/"+row);
    }
    private void last(JTable table,JLabel label,int row){
        table.getSelectionModel().setSelectionInterval(0, row-1);
        label.setText((table.getSelectedRow()+1)+"/"+row);
    }
    
    private void setting(){
        try {
            txtCH.setLineWrap(true);
            txtNdCH.setLineWrap(true);
            txtNdTL.setLineWrap(true);
            ckbPass.setSelected(false);
            ckbChangePass.setSelected(false);
            jPanel15.setVisible(false);
            txtUsername.setText("Admin");
            txtEmail.setText("admin@gmail.com");
            stm=con.createStatement();
            rs=stm.executeQuery("select Password from UserInfor where Username='Admin'");
            while(rs.next()){
                txtPassword.setText(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private boolean checkChangePass(){
        String pass=new String(txtPassword.getPassword());
        if(txtOldPass.getText().isBlank()){
            JOptionPane.showMessageDialog(null, "Chưa nhập mật khẩu cũ");
            return false;
        }
        if(!txtOldPass.getText().equals(pass)){
            JOptionPane.showMessageDialog(null, "Mật khẩu cũ chưa khớp");
            return false;
        }
        if(txtNewPass.getText().isBlank()){
            JOptionPane.showMessageDialog(null, "Chưa nhập mật khẩu mới");
            return false;
        }
        if(txtRenewPass.getText().isBlank()){
            JOptionPane.showMessageDialog(null, "Chưa nhập lại mật khẩu");
            return false;
        }
        if(txtNewPass.getText().equals(" ")){
            JOptionPane.showMessageDialog(null, "Mật khẩu không được chứa dấu cách");
            return false;
        }
        if(txtNewPass.getText().length()>20){
            JOptionPane.showMessageDialog(null, "Độ dài mật khẩu không quá 20 ký ");
            return false;
        }
        if(txtOldPass.getText().equals(txtNewPass.getText())){
            JOptionPane.showMessageDialog(null, "Mật khẩu mới bị trùng với mật khẩu cũ");
            return false;
        }
        if(!txtRenewPass.getText().equals(txtNewPass.getText())){
            JOptionPane.showMessageDialog(null, "Nhập lại mật khẩu không khớp");
            return false;
        }
        return true;
    }
    private void Home(){
        Home f=new Home("admin@gmail.com");
        this.setVisible(false);
        f.setVisible(true);
    }
    private boolean checkQuestion(){
        String tag=txtTagCH.getText();
        String tieude=txtCH.getText();
        if(tag.contains(" ")){
            JOptionPane.showMessageDialog(null, "Tag câu hỏi không được chứa dấu cách");
            return false;
        }
        if(tag.charAt(0)!='#'){
            JOptionPane.showMessageDialog(null, "Tag câu hỏi phải bắt đầu bằng #");
            return false;
        }
        if(tag.isBlank()){
            JOptionPane.showMessageDialog(null, "Không được để trống phần tag câu hỏi");
            return false;
        }
        if(tieude.isBlank()){
            JOptionPane.showMessageDialog(null, "Không được để trống tiêu đề");
            return false;
        }
        return true;
    }
    private void suaCH(){
        btnSuaCH.setText("Sửa câu hỏi");
        btnBackCH.setText("Quay lại");
        btnThemCH.setEnabled(true);
        btnXoaCH.setEnabled(true);
        txtMaCH.setText("");
        txtTagCH.setText("");
        txtUserCH.setText("");
        txtCH.setText("");
        txtNdCH.setText("");
    }
    private void suaTL(){
        btnSuaTL.setText("Sửa câu trả lời");
        btnBackTL.setText("Quay lại");
        btnXoaTL.setEnabled(true);
        txtMaCH1.setText("");
        txtMaTL.setText("");
        txtUserTL.setText("");
        txtNdTL.setText("");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbViewTK = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTK = new javax.swing.JTextField();
        txtEmailTK = new javax.swing.JTextField();
        txtPassTK = new javax.swing.JTextField();
        txtCountQ = new javax.swing.JTextField();
        txtCountA = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnResetTK = new javax.swing.JButton();
        btnXoaTK = new javax.swing.JButton();
        btnBackTK = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnDauTK = new javax.swing.JButton();
        btnTruocTK = new javax.swing.JButton();
        lbPageTK = new javax.swing.JLabel();
        btnSauTK = new javax.swing.JButton();
        btnCuoiTK = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtTimTK = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbViewCH = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        btnDauCH = new javax.swing.JButton();
        btnTruocCH = new javax.swing.JButton();
        lbPageCH = new javax.swing.JLabel();
        btnSauCH = new javax.swing.JButton();
        btnCuoiCH = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtTimCH = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtMaCH = new javax.swing.JTextField();
        txtTagCH = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtCH = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        txtUserCH = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtNdCH = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        btnThemCH = new javax.swing.JButton();
        btnSuaCH = new javax.swing.JButton();
        btnXoaCH = new javax.swing.JButton();
        btnBackCH = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbViewTL = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        btnSuaTL = new javax.swing.JButton();
        btnXoaTL = new javax.swing.JButton();
        btnBackTL = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        btnDauTL = new javax.swing.JButton();
        btnTruocTL = new javax.swing.JButton();
        lbPageTL = new javax.swing.JLabel();
        btnSauTL = new javax.swing.JButton();
        btnCuoiTL = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtTimTL = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtMaTL = new javax.swing.JTextField();
        txtMaCH1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtUserTL = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtNdTL = new javax.swing.JTextArea();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        ckbPass = new javax.swing.JCheckBox();
        txtPassword = new javax.swing.JPasswordField();
        jPanel15 = new javax.swing.JPanel();
        btnConfirm = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtOldPass = new javax.swing.JTextField();
        txtNewPass = new javax.swing.JTextField();
        txtRenewPass = new javax.swing.JTextField();
        ckbChangePass = new javax.swing.JCheckBox();
        btnBack = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin");
        setResizable(false);

        jTabbedPane5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        tbViewTK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tbViewTK.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbViewTK.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbViewTK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbViewTKMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbViewTK);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel1.setText("Tên người dùng");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setText("Email");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setText("Mật khẩu ");

        txtTK.setEditable(false);
        txtTK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtTK.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        txtEmailTK.setEditable(false);
        txtEmailTK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtEmailTK.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        txtPassTK.setEditable(false);
        txtPassTK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtPassTK.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        txtCountQ.setEditable(false);
        txtCountQ.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtCountA.setEditable(false);
        txtCountA.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel22.setText("Số câu hỏi");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel23.setText("Số câu trả lời");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmailTK, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txtTK, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(txtPassTK))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(txtCountQ, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCountA, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtEmailTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPassTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCountQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCountA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        btnResetTK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnResetTK.setText("Reset");
        btnResetTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetTKActionPerformed(evt);
            }
        });

        btnXoaTK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXoaTK.setText("Xóa");
        btnXoaTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTKActionPerformed(evt);
            }
        });

        btnBackTK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnBackTK.setText("Quay lại");
        btnBackTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackTKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBackTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoaTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(btnResetTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnXoaTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnBackTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        btnDauTK.setText("<<");
        btnDauTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDauTKActionPerformed(evt);
            }
        });

        btnTruocTK.setText("<");
        btnTruocTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTruocTKActionPerformed(evt);
            }
        });

        lbPageTK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbPageTK.setText("jLabel3");

        btnSauTK.setText(">");
        btnSauTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSauTKActionPerformed(evt);
            }
        });

        btnCuoiTK.setText(">>");
        btnCuoiTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCuoiTKActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setText("Tìm kiếm");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDauTK, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTruocTK, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbPageTK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSauTK, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCuoiTK, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTimTK, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPageTK)
                    .addComponent(btnTruocTK)
                    .addComponent(btnDauTK)
                    .addComponent(btnSauTK)
                    .addComponent(btnCuoiTK)
                    .addComponent(jLabel4)
                    .addComponent(txtTimTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Quản lý người dùng", jPanel1);

        tbViewCH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbViewCH.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbViewCH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbViewCHMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbViewCH);

        btnDauCH.setText("<<");
        btnDauCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDauCHActionPerformed(evt);
            }
        });

        btnTruocCH.setText("<");
        btnTruocCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTruocCHActionPerformed(evt);
            }
        });

        lbPageCH.setText("jLabel3");

        btnSauCH.setText(">");
        btnSauCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSauCHActionPerformed(evt);
            }
        });

        btnCuoiCH.setText(">>");
        btnCuoiCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCuoiCHActionPerformed(evt);
            }
        });

        jLabel5.setText("Tìm kiếm");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDauCH, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTruocCH, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbPageCH)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSauCH, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCuoiCH, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTimCH, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPageCH)
                    .addComponent(btnTruocCH)
                    .addComponent(btnDauCH)
                    .addComponent(btnSauCH)
                    .addComponent(btnCuoiCH)
                    .addComponent(jLabel5)
                    .addComponent(txtTimCH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Chi tiết"));
        jPanel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setText("Mã câu hỏi");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setText("Tag");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setText("Tiêu đề");

        txtMaCH.setEditable(false);
        txtMaCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtTagCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtCH.setColumns(20);
        txtCH.setRows(5);
        txtCH.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txtCH);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("Tên người dùng");

        txtUserCH.setEditable(false);
        txtUserCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("Nội dung");

        txtNdCH.setColumns(20);
        txtNdCH.setRows(5);
        txtNdCH.setWrapStyleWord(true);
        jScrollPane4.setViewportView(txtNdCH);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaCH, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTagCH, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUserCH, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addComponent(jScrollPane3))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtMaCH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtTagCH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtUserCH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addGap(35, 35, 35))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Chức năng"));
        jPanel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        btnThemCH.setText("Thêm câu hỏi");
        btnThemCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemCHActionPerformed(evt);
            }
        });

        btnSuaCH.setText("Sửa câu hỏi");
        btnSuaCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaCHActionPerformed(evt);
            }
        });

        btnXoaCH.setText("Xóa câu hỏi");
        btnXoaCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaCHActionPerformed(evt);
            }
        });

        btnBackCH.setText("Quay lại");
        btnBackCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackCHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBackCH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSuaCH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnXoaCH, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                    .addComponent(btnThemCH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnThemCH)
                .addGap(18, 18, 18)
                .addComponent(btnSuaCH)
                .addGap(18, 18, 18)
                .addComponent(btnXoaCH)
                .addGap(18, 18, 18)
                .addComponent(btnBackCH)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );

        jTabbedPane5.addTab("Quản lý câu hỏi", jPanel5);

        tbViewTL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbViewTL.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbViewTL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbViewTLMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tbViewTL);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Chức năng"));
        jPanel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        btnSuaTL.setText("Sửa câu trả lời");
        btnSuaTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaTLActionPerformed(evt);
            }
        });

        btnXoaTL.setText("Xóa câu trả lời");
        btnXoaTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTLActionPerformed(evt);
            }
        });

        btnBackTL.setText("Quay lại");
        btnBackTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackTLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSuaTL, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(btnXoaTL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBackTL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnSuaTL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(33, 33, 33)
                .addComponent(btnXoaTL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(38, 38, 38)
                .addComponent(btnBackTL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        btnDauTL.setText("<<");
        btnDauTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDauTLActionPerformed(evt);
            }
        });

        btnTruocTL.setText("<");
        btnTruocTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTruocTLActionPerformed(evt);
            }
        });

        lbPageTL.setText("jLabel3");

        btnSauTL.setText(">");
        btnSauTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSauTLActionPerformed(evt);
            }
        });

        btnCuoiTL.setText(">>");
        btnCuoiTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCuoiTLActionPerformed(evt);
            }
        });

        jLabel11.setText("Tìm kiếm");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDauTL, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTruocTL, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbPageTL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSauTL, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCuoiTL, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTimTL, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPageTL)
                    .addComponent(btnTruocTL)
                    .addComponent(btnDauTL)
                    .addComponent(btnSauTL)
                    .addComponent(btnCuoiTL)
                    .addComponent(jLabel11)
                    .addComponent(txtTimTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setText("Mã trả lời");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setText("Mã câu hỏi");

        txtMaTL.setEditable(false);
        txtMaTL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtMaCH1.setEditable(false);
        txtMaCH1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel15.setText("Tên người dùng");

        txtUserTL.setEditable(false);
        txtUserTL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel16.setText("Nội dung");

        txtNdTL.setColumns(20);
        txtNdTL.setRows(5);
        txtNdTL.setWrapStyleWord(true);
        jScrollPane7.setViewportView(txtNdTL);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(txtMaTL, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaCH1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUserTL, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane7))
                .addGap(18, 18, 18))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtMaTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtMaCH1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtUserTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane5.addTab("Quản lý câu trả lời", jPanel9);
        jPanel9.getAccessibleContext().setAccessibleParent(this);

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel14.setText("Tên người dùng");

        txtUsername.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtUsername.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtUsername.setEnabled(false);

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel17.setText("Email");

        txtEmail.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtEmail.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtEmail.setEnabled(false);

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel18.setText("Mật khẩu");

        ckbPass.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        ckbPass.setText("Hiện mật khẩu");
        ckbPass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ckbPassMouseClicked(evt);
            }
        });

        txtPassword.setEditable(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ckbPass, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                    .addComponent(txtUsername)
                    .addComponent(txtPassword))
                .addGap(160, 160, 160))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(ckbPass)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        btnConfirm.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnConfirm.setText("Xác nhận");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel19.setText("Nhập lại mật khẩu");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel20.setText("Mật khẩu cũ");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel21.setText("Mật khẩu mới");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtOldPass, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addComponent(txtNewPass)
                    .addComponent(txtRenewPass))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnConfirm)
                .addGap(235, 235, 235))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtOldPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtNewPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtRenewPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnConfirm)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        ckbChangePass.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        ckbChangePass.setText("Đổi mật khẩu");
        ckbChangePass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ckbChangePassMouseClicked(evt);
            }
        });

        btnBack.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnBack.setText("Quay lại");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnLogout.setText("Đăng xuất");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(180, Short.MAX_VALUE)
                .addComponent(ckbChangePass)
                .addGap(43, 43, 43)
                .addComponent(btnBack)
                .addGap(29, 29, 29)
                .addComponent(btnLogout)
                .addGap(149, 149, 149))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbChangePass)
                    .addComponent(btnBack)
                    .addComponent(btnLogout))
                .addGap(23, 23, 23)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane5.addTab("Quản lý tài khoản", jPanel13);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDauTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDauTKActionPerformed
        // TODO add your handling code here:
        first(tbViewTK, lbPageTK, rowTK);
    }//GEN-LAST:event_btnDauTKActionPerformed

    private void btnDauCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDauCHActionPerformed
        // TODO add your handling code here:
        first(tbViewCH, lbPageCH, rowCH);
    }//GEN-LAST:event_btnDauCHActionPerformed

    private void btnDauTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDauTLActionPerformed
        // TODO add your handling code here:
        first(tbViewTL, lbPageTL, rowTL);
    }//GEN-LAST:event_btnDauTLActionPerformed

    private void btnTruocTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTruocTLActionPerformed
        // TODO add your handling code here:
        before(tbViewTL, lbPageTL, rowTL);
    }//GEN-LAST:event_btnTruocTLActionPerformed

    private void btnTruocCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTruocCHActionPerformed
        // TODO add your handling code here:
        before(tbViewCH, lbPageCH, rowCH);
    }//GEN-LAST:event_btnTruocCHActionPerformed

    private void btnTruocTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTruocTKActionPerformed
        // TODO add your handling code here:
        before(tbViewTK, lbPageTK, rowTK);
    }//GEN-LAST:event_btnTruocTKActionPerformed

    private void btnSauTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSauTKActionPerformed
        // TODO add your handling code here:
        after(tbViewTK, lbPageTK, rowTK);
    }//GEN-LAST:event_btnSauTKActionPerformed

    private void btnSauCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSauCHActionPerformed
        // TODO add your handling code here:
        after(tbViewCH, lbPageCH, rowCH);
    }//GEN-LAST:event_btnSauCHActionPerformed

    private void btnSauTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSauTLActionPerformed
        // TODO add your handling code here:
        after(tbViewTL, lbPageTL, rowTL);
    }//GEN-LAST:event_btnSauTLActionPerformed

    private void btnCuoiTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCuoiTKActionPerformed
        // TODO add your handling code here:
        last(tbViewTK, lbPageTK, rowTK);
    }//GEN-LAST:event_btnCuoiTKActionPerformed

    private void btnCuoiCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCuoiCHActionPerformed
        // TODO add your handling code here:
        last(tbViewCH, lbPageCH, rowCH);
    }//GEN-LAST:event_btnCuoiCHActionPerformed

    private void btnCuoiTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCuoiTLActionPerformed
        // TODO add your handling code here:
        last(tbViewTL, lbPageTL, rowTL);
    }//GEN-LAST:event_btnCuoiTLActionPerformed

    private void ckbPassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ckbPassMouseClicked
        // TODO add your handling code here:
        if(ckbPass.isSelected()){
            txtPassword.setEchoChar((char)0);
        }
        else txtPassword.setEchoChar('*');
    }//GEN-LAST:event_ckbPassMouseClicked

    private void ckbChangePassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ckbChangePassMouseClicked
        // TODO add your handling code here:
        if(ckbChangePass.isSelected()){
            jPanel15.setVisible(true);
            txtOldPass.setText("");
            txtNewPass.setText("");
            txtRenewPass.setText("");
        }
        else jPanel15.setVisible(false);
    }//GEN-LAST:event_ckbChangePassMouseClicked

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        try {
            // TODO add your handling code here:
            if(checkChangePass()==true){
                String pass=txtNewPass.getText();
                stm=con.createStatement();
                stm.executeUpdate("update UserInfor set Password='"+pass+"' where Username='Admin'");
                setting();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        Login f=new Login();
        f.setVisible(true);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        Home();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnBackTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackTKActionPerformed
        // TODO add your handling code here:
        Home();
    }//GEN-LAST:event_btnBackTKActionPerformed

    private void btnBackCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackCHActionPerformed
        // TODO add your handling code here:
        if(btnBackCH.getText().equals("Quay lại")){
        Home();
        }
        else {
            suaCH();
        }
    }//GEN-LAST:event_btnBackCHActionPerformed

    private void btnBackTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackTLActionPerformed
        // TODO add your handling code here:
        if(btnBackTL.getText().equals("Quay lại")){
            Home();
        }
        else {
            suaTL();
        }
    }//GEN-LAST:event_btnBackTLActionPerformed

    private void btnResetTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetTKActionPerformed
        try {
            // TODO add your handling code here:
            stm=con.createStatement();
            stm.executeUpdate("update UserInfor set Password='default' where Username='"+txtTK.getText()+"'");
            tbViewTK.setModel(TK(bang("select* from UserInfor")));
            settable(tbViewTK);
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }//GEN-LAST:event_btnResetTKActionPerformed

    private void btnXoaTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTKActionPerformed
        try {
            if(JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa người dùng này","Thông báo",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
            // TODO add your handling code here:
            if(!txtTK.getText().equals("Admin")&&!txtTK.getText().equals("default")){
                stm=con.createStatement();
                stm.executeUpdate("update AnswerInfor set Username='default' where Username='"+txtTK.getText()+"'");
                stm.executeUpdate("update QuestInfor set Username='default' where Username='"+txtTK.getText()+"'");
                stm.executeUpdate("delete from UserInfor where Username='"+txtTK.getText()+"'");
                tbViewTK.setModel(TK(bang("select* from UserInfor")));
                tbViewCH.setModel(CH(bang("select* from QuestInfor")));
                tbViewTL.setModel(TL(bang("select* from AnswerInfor")));
                rowTK=tbViewTK.getRowCount();
                getrow(lbPageTK, rowTK);
                settable(tbViewTK);
            }
            else{
                JOptionPane.showMessageDialog(null, "Không thể xóa người dùng này");
            }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnXoaTKActionPerformed

    private void btnThemCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemCHActionPerformed
        try {
            // TODO add your handling code here:
            if(checkQuestion()){
                stm=con.createStatement();
                rs=stm.executeQuery("SELECT Max(QuestID) as LastID FROM QuestInfor");
                int ma=0;
                while(rs.next()) ma=Integer.parseInt(rs.getString(1))+1;
                String tag=txtTagCH.getText();
                String tieude=txtCH.getText().replace("\n", "\\n%");
                String noidung=txtNdCH.getText().replace("\n", "\\n%");
                stm.executeUpdate("insert into QuestInfor values('"+ma+"','"+tag+"',N'"+tieude+"',N'"+noidung+"','Admin',0)");
                tbViewCH.setModel(CH(bang("select* from QuestInfor")));
                rowCH=tbViewCH.getRowCount();
                getrow(lbPageCH, rowCH);
                settable(tbViewCH);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnThemCHActionPerformed

    private void btnSuaCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaCHActionPerformed
        // TODO add your handling code here:
        int dc=tbViewCH.getSelectedRow();
        if(btnSuaCH.getText().equals("Sửa câu hỏi")){
            btnSuaCH.setText("Lưu");
            btnBackCH.setText("Hủy");
            btnThemCH.setEnabled(false);
            btnXoaCH.setEnabled(false);
            txtMaCH.setText(tbViewCH.getValueAt(dc, 1).toString());
            txtTagCH.setText(tbViewCH.getValueAt(dc, 2).toString());
            txtUserCH.setText(tbViewCH.getValueAt(dc, 5).toString());
            txtCH.setText(tbViewCH.getValueAt(dc, 3).toString().replace("\\n%", "\n"));
            txtNdCH.setText(tbViewCH.getValueAt(dc, 4).toString().replace("\\n%", "\n"));
        }
        else{
            if(checkQuestion()){
                try {
                    stm=con.createStatement();
                    String sql="update QuestInfor set QuestTag='"+txtTagCH.getText()+"',QuestTitle=N'"+txtCH.getText()+"',QuestContent=N'"+txtNdCH.getText()+"' where QuestID='"+maCH+"'";
                    stm.executeUpdate(sql);
                    suaCH();
                    tbViewCH.setModel(CH(bang("select* from QuestInfor")));
                    tbViewCH.getSelectionModel().setSelectionInterval(0,dc);
                } catch (SQLException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnSuaCHActionPerformed

    private void tbViewCHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbViewCHMouseClicked
        // TODO add your handling code here:
        if(tbViewCH.getSelectedRow()>=0){
            if(tbViewCH.getSelectedRow()!=rowCH){
                maCH=tbViewCH.getValueAt(tbViewCH.getSelectedRow(), 1).toString();
                lbPageCH.setText((tbViewCH.getSelectedRow()+1)+"/"+rowCH);
            }
            else maCH=tbViewCH.getValueAt(0, 1).toString();
        }
        suaCH();
    }//GEN-LAST:event_tbViewCHMouseClicked

    private void btnXoaCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaCHActionPerformed
        // TODO add your handling code here:
        try {
            if(JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa câu hỏi này","Thông báo",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
            // TODO add your handling code here:
                stm=con.createStatement();
                stm.executeUpdate("delete from AnswerInfor where QuestID='"+maCH+"'");
                stm.executeUpdate("delete from QuestInfor where QuestID='"+maCH+"'");
                tbViewCH.setModel(CH(bang("select* from QuestInfor")));
                tbViewTL.setModel(TL(bang("select* from AnswerInfor")));
                rowCH=tbViewCH.getRowCount();
                rowTL=tbViewTL.getRowCount();
                getrow(lbPageCH, rowCH);
                getrow(lbPageTL, rowTL);
                settable(tbViewCH);
                settable(tbViewTL);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnXoaCHActionPerformed

    private void btnXoaTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTLActionPerformed
        // TODO add your handling code here:
        try {
            if(JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa câu hỏi này","Thông báo",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
            // TODO add your handling code here:
                stm=con.createStatement();
                stm.executeUpdate("delete from AnswerInfor where AnswerID='"+maTL+"'");
                tbViewTL.setModel(TL(bang("select* from AnswerInfor")));
                rowTL=tbViewTL.getRowCount();
                getrow(lbPageTL, rowTL);
                settable(tbViewTL);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnXoaTLActionPerformed

    private void tbViewTLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbViewTLMouseClicked
        // TODO add your handling code here:
        if(tbViewTL.getSelectedRow()>=0){
            if(tbViewTL.getSelectedRow()!=rowTL){
                maTL=tbViewTL.getValueAt(tbViewTL.getSelectedRow(), 1).toString();
                
            }
            else maTL=tbViewTL.getValueAt(0, 1).toString();
            lbPageTL.setText((tbViewTL.getSelectedRow()+1)+"/"+rowTL);
        }
        suaTL();
    }//GEN-LAST:event_tbViewTLMouseClicked

    private void btnSuaTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaTLActionPerformed
        // TODO add your handling code here:
        int dc=tbViewTL.getSelectedRow();
        if(btnSuaTL.getText().equals("Sửa câu trả lời")){
            btnSuaTL.setText("Lưu");
            btnBackTL.setText("Hủy");
            btnXoaTL.setEnabled(false);
            txtMaCH1.setText(tbViewTL.getValueAt(dc, 3).toString());
            txtMaTL.setText(tbViewTL.getValueAt(dc, 1).toString());
            txtUserTL.setText(tbViewTL.getValueAt(dc, 2).toString());
            txtNdTL.setText(tbViewTL.getValueAt(dc, 4).toString().replace("\\n%", "\n"));
        }
        else{
                try {
                    stm=con.createStatement();
                    String sql="update AnswerInfor set Answer=N'"+txtNdTL.getText()+"' where AnswerID='"+maTL+"'";
                    stm.executeUpdate(sql);
                    suaTL();
                    tbViewTL.setModel(TL(bang("select* from AnswerInfor")));
                    tbViewTL.getSelectionModel().setSelectionInterval(0,dc);
                } catch (SQLException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }//GEN-LAST:event_btnSuaTLActionPerformed

    private void tbViewTKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbViewTKMouseClicked
        // TODO add your handling code here:
        lbPageTK.setText((tbViewTK.getSelectedRow()+1)+"/"+rowTK);
    }//GEN-LAST:event_tbViewTKMouseClicked

    
    
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Admin().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBackCH;
    private javax.swing.JButton btnBackTK;
    private javax.swing.JButton btnBackTL;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnCuoiCH;
    private javax.swing.JButton btnCuoiTK;
    private javax.swing.JButton btnCuoiTL;
    private javax.swing.JButton btnDauCH;
    private javax.swing.JButton btnDauTK;
    private javax.swing.JButton btnDauTL;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnResetTK;
    private javax.swing.JButton btnSauCH;
    private javax.swing.JButton btnSauTK;
    private javax.swing.JButton btnSauTL;
    private javax.swing.JButton btnSuaCH;
    private javax.swing.JButton btnSuaTL;
    private javax.swing.JButton btnThemCH;
    private javax.swing.JButton btnTruocCH;
    private javax.swing.JButton btnTruocTK;
    private javax.swing.JButton btnTruocTL;
    private javax.swing.JButton btnXoaCH;
    private javax.swing.JButton btnXoaTK;
    private javax.swing.JButton btnXoaTL;
    private javax.swing.JCheckBox ckbChangePass;
    private javax.swing.JCheckBox ckbPass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JLabel lbPageCH;
    private javax.swing.JLabel lbPageTK;
    private javax.swing.JLabel lbPageTL;
    private javax.swing.JTable tbViewCH;
    private javax.swing.JTable tbViewTK;
    private javax.swing.JTable tbViewTL;
    private javax.swing.JTextArea txtCH;
    private javax.swing.JTextField txtCountA;
    private javax.swing.JTextField txtCountQ;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmailTK;
    private javax.swing.JTextField txtMaCH;
    private javax.swing.JTextField txtMaCH1;
    private javax.swing.JTextField txtMaTL;
    private javax.swing.JTextArea txtNdCH;
    private javax.swing.JTextArea txtNdTL;
    private javax.swing.JTextField txtNewPass;
    private javax.swing.JTextField txtOldPass;
    private javax.swing.JTextField txtPassTK;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtRenewPass;
    private javax.swing.JTextField txtTK;
    private javax.swing.JTextField txtTagCH;
    private javax.swing.JTextField txtTimCH;
    private javax.swing.JTextField txtTimTK;
    private javax.swing.JTextField txtTimTL;
    private javax.swing.JTextField txtUserCH;
    private javax.swing.JTextField txtUserTL;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
