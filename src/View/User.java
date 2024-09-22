/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Database.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class User extends javax.swing.JFrame {

    /**
     * Creates new form User
     */
    static String email;
    int rowTK,rowCH,rowTL;
    DBConnection connection;
    Connection con;
    Statement stm;ResultSet rs;
    DefaultTableModel dtm;
    String maCH,maTL,user;
    Map<String, String> list=new HashMap<>();
    public User(String email) {
        initComponents();
        connection=new DBConnection();
        try {
            User.email=email;
            con=connection.DBConnection();
            stm=con.createStatement();
            rs=stm.executeQuery("select Username from UserInfor where UserEmail='"+email+"'");
            while(rs.next()) this.user=rs.getString(1);
            connection=new DBConnection();
            con=connection.DBConnection();
            setDefaultCloseOperation(Admin.HIDE_ON_CLOSE);
            tbViewCH.setModel(CH(bang("select* from QuestInfor where Username='"+user+"'")));
            tbViewTL.setModel(TL(bang("select* from AnswerInfor where Username='"+user+"'")));
            rowCH=tbViewCH.getRowCount();rowTL=tbViewTL.getRowCount();
            getrow(lbPageCH, rowCH);getrow(lbPageTL, rowTL);
            settable(tbViewCH);settable(tbViewTL);
            //maCH=tbViewCH.getValueAt(tbViewCH.getSelectedRow(), 1).toString();
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
                        String sql="select * from QuestInfor where (QuestTitle like '%"+tim+"%' or QuestTag like '%"+tim+"%') and Username='"+user+"'";
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
                        String sql="select * from AnswerInfor where (Answer like '%"+tim+"%' or QuestID like '%"+tim+"%') and Username='"+user+"'";
                        tbViewTL.setModel(TL(bang(sql)));
                        rowTL=tbViewTL.getRowCount();
                        tbViewTL.getSelectionModel().setSelectionInterval( 0, 0 );
                        lbPageTL.setText("1/"+rowTL);
                    } catch (SQLException ex) {
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            setting();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void Home(){
        Home f=new Home(email);
        this.setVisible(false);
        f.setVisible(true);
    }
    private ResultSet bang(String sql){
        try {
            stm=con.createStatement();
            rs=stm.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
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
            dtm.addColumn("Báo cáo");
            dtm.setRowCount(0);
            while(rs.next()){
                dtm.addRow(new Object[]{
                    i,
                    rs.getString("QuestID"),
                    rs.getString("QuestTag"),
                    rs.getString("QuestTitle"),
                    rs.getString("QuestContent"),
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
            dtm.addColumn("Mã câu hỏi");
            dtm.addColumn("Nội dung");
            dtm.addColumn("Báo cáo");
            dtm.setRowCount(0);
            while(rs.next()){
                dtm.addRow(new Object[]{
                    i,
                    rs.getString("AnswerID"),
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
    
    private void settable(JTable table){
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().setSelectionInterval( 0, 0 );
    }private boolean checkQuestion(){
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
        txtTagCH.setText("");
        txtCH.setText("");
        txtNdCH.setText("");
    }
    private void suaTL(){
        btnSuaTL.setText("Sửa câu trả lời");
        btnBackTL.setText("Quay lại");
        btnXoaTL.setEnabled(true);
        txtMaCH1.setText("");
        txtMaTL.setText("");
        txtNdTL.setText("");
    }
    
    private void setting(){
        try {
            listUser();
            txtCH.setLineWrap(true);
            txtNdCH.setLineWrap(true);
            txtNdTL.setLineWrap(true);
            ckbPass.setSelected(false);
            ckbChangePass.setSelected(false);
            ckbChangeEmail.setSelected(false);
            btnOK.setEnabled(false);
            jPanel15.setVisible(false);
            txtUsername.setText(user);
            txtEmail.setText(email);
            stm=con.createStatement();
            rs=stm.executeQuery("select Password from UserInfor where Username='"+user+"'");
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
    private boolean checkChangeEmail(){
        String mail=txtEmail.getText();
        if(checkEmail()==false){
            JOptionPane.showMessageDialog(null, "Nhập sai định dạng email", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(list.containsValue(mail)){
            JOptionPane.showMessageDialog(null, "Email đã được sử dụng", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    private void listUser(){
        try {
            String email1,tentk1;
            String sql="select Username,UserEmail from UserInfor";
            stm=con.createStatement();
            rs=stm.executeQuery(sql);
            while(rs.next()){
                tentk1=rs.getString("Username");
                email1=rs.getString("UserEmail");
                list.put(tentk1, email1);
            }
            //connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    boolean checkEmail(){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(txtEmail.getText()).matches();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbViewCH = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        btnThemCH = new javax.swing.JButton();
        btnSuaCH = new javax.swing.JButton();
        btnXoaCH = new javax.swing.JButton();
        btnBackCH = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnDauCH = new javax.swing.JButton();
        btnTruocCH = new javax.swing.JButton();
        lbPageCH = new javax.swing.JLabel();
        btnSauCH = new javax.swing.JButton();
        btnCuoiCH = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtTimCH = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTagCH = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtCH = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtNdCH = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
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
        jLabel16 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtNdTL = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        ckbPass = new javax.swing.JCheckBox();
        txtPassword = new javax.swing.JPasswordField();
        ckbChangeEmail = new javax.swing.JCheckBox();
        btnOK = new javax.swing.JButton();
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
        setTitle("User");
        setResizable(false);

        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        tbViewCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        btnThemCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnThemCH.setText("Thêm câu hỏi");
        btnThemCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemCHActionPerformed(evt);
            }
        });

        btnSuaCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnSuaCH.setText("Sửa câu hỏi");
        btnSuaCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaCHActionPerformed(evt);
            }
        });

        btnXoaCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXoaCH.setText("Xóa câu hỏi");
        btnXoaCH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaCHActionPerformed(evt);
            }
        });

        btnBackCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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

        lbPageCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("Tìm kiếm");

        txtTimCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

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

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setText("Tag");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setText("Tiêu đề");

        txtTagCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtCH.setColumns(20);
        txtCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtCH.setRows(5);
        txtCH.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txtCH);

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("Nội dung");

        txtNdCH.setColumns(20);
        txtNdCH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                            .addComponent(jScrollPane3)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTagCH, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTagCH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );

        jTabbedPane1.addTab("Quản lý câu hỏi", jPanel1);

        tbViewTL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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
        jScrollPane5.setViewportView(tbViewTL);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        btnSuaTL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnSuaTL.setText("Sửa câu trả lời");
        btnSuaTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaTLActionPerformed(evt);
            }
        });

        btnXoaTL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXoaTL.setText("Xóa câu trả lời");
        btnXoaTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTLActionPerformed(evt);
            }
        });

        btnBackTL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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

        lbPageTL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setText("Tìm kiếm");

        txtTimTL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel16.setText("Nội dung");

        txtNdTL.setColumns(20);
        txtNdTL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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
                        .addComponent(txtMaCH1, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                        .addGap(270, 270, 270))
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
                    .addComponent(txtMaCH1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Quản lý câu trả lời", jPanel2);

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
        txtPassword.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        ckbChangeEmail.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        ckbChangeEmail.setText("Đổi email");
        ckbChangeEmail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ckbChangeEmailMouseClicked(evt);
            }
        });

        btnOK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnOK.setText("Xác nhận");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

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
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                    .addComponent(txtUsername)
                    .addComponent(txtPassword))
                .addGap(51, 51, 51)
                .addComponent(btnOK)
                .addGap(70, 70, 70))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addComponent(ckbPass, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(166, 166, 166)
                .addComponent(ckbChangeEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOK))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbPass)
                    .addComponent(ckbChangeEmail))
                .addContainerGap())
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

        txtOldPass.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtNewPass.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtRenewPass.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(182, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnConfirm)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtNewPass, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtOldPass, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtRenewPass, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(171, 171, 171))
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
                .addContainerGap(17, Short.MAX_VALUE))
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

        btnLogout.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnLogout.setText("Đăng xuất");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ckbChangePass)
                        .addGap(18, 18, 18)
                        .addComponent(btnBack)
                        .addGap(18, 18, 18)
                        .addComponent(btnLogout)
                        .addGap(155, 155, 155)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbChangePass)
                    .addComponent(btnBack)
                    .addComponent(btnLogout))
                .addGap(23, 23, 23)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Quản lý tài khoản", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        Home();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        Login f=new Login();
        f.setVisible(true);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnDauCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDauCHActionPerformed
        // TODO add your handling code here:
        first(tbViewCH, lbPageCH, rowCH);
    }//GEN-LAST:event_btnDauCHActionPerformed

    private void btnDauTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDauTLActionPerformed
        // TODO add your handling code here:
        first(tbViewTL, lbPageTL, rowTL);
    }//GEN-LAST:event_btnDauTLActionPerformed

    private void btnTruocCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTruocCHActionPerformed
        // TODO add your handling code here:
        before(tbViewCH, lbPageCH, rowCH);
    }//GEN-LAST:event_btnTruocCHActionPerformed

    private void btnTruocTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTruocTLActionPerformed
        // TODO add your handling code here:
        before(tbViewTL, lbPageTL, rowTL);
    }//GEN-LAST:event_btnTruocTLActionPerformed

    private void btnSauCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSauCHActionPerformed
        // TODO add your handling code here:
        after(tbViewCH, lbPageCH, rowCH);
    }//GEN-LAST:event_btnSauCHActionPerformed

    private void btnSauTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSauTLActionPerformed
        // TODO add your handling code here:
        after(tbViewTL, lbPageTL, rowTL);
    }//GEN-LAST:event_btnSauTLActionPerformed

    private void btnCuoiCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCuoiCHActionPerformed
        // TODO add your handling code here:
        last(tbViewCH, lbPageCH, rowCH);
    }//GEN-LAST:event_btnCuoiCHActionPerformed

    private void btnCuoiTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCuoiTLActionPerformed
        // TODO add your handling code here:
        last(tbViewTL, lbPageTL, rowTL);
    }//GEN-LAST:event_btnCuoiTLActionPerformed

    private void btnThemCHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemCHActionPerformed
        // TODO add your handling code here:
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
                stm.executeUpdate("insert into QuestInfor values('"+ma+"','"+tag+"',N'"+tieude+"',N'"+noidung+"','"+user+"',0)");
                tbViewCH.setModel(CH(bang("select* from QuestInfor where Username='"+user+"'")));
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
            txtTagCH.setText(tbViewCH.getValueAt(dc, 2).toString());
            txtCH.setText(tbViewCH.getValueAt(dc, 3).toString().replace("\\n%", "\n"));
            txtNdCH.setText(tbViewCH.getValueAt(dc, 4).toString().replace("\\n%", "\n"));
        }
        else{
            if(checkQuestion()){
                try {
                    stm=con.createStatement();
                    maCH=tbViewCH.getValueAt(tbViewCH.getSelectedRow(), 1).toString();
                    String sql="update QuestInfor set QuestTag='"+txtTagCH.getText()+"',QuestTitle=N'"+txtCH.getText()+"',QuestContent=N'"+txtNdCH.getText()+"' where QuestID='"+maCH+"'";
                    stm.executeUpdate(sql);
                    suaCH();
                    tbViewCH.setModel(CH(bang("select* from QuestInfor where Username='"+user+"'")));
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
                stm=con.createStatement();
                stm.executeUpdate("delete from AnswerInfor where QuestID='"+maCH+"'");
                stm.executeUpdate("delete from QuestInfor where QuestID='"+maCH+"'");
                tbViewCH.setModel(CH(bang("select* from QuestInfor where Username='"+user+"'")));
                tbViewTL.setModel(TL(bang("select* from AnswerInfor where Username='"+user+"'")));
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

    private void btnSuaTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaTLActionPerformed
        // TODO add your handling code here:
        int dc=tbViewTL.getSelectedRow();
        if(btnSuaTL.getText().equals("Sửa câu trả lời")){
            btnSuaTL.setText("Lưu");
            btnBackTL.setText("Hủy");
            btnXoaTL.setEnabled(false);
            txtMaCH1.setText(tbViewTL.getValueAt(dc, 3).toString());
            txtMaTL.setText(tbViewTL.getValueAt(dc, 1).toString());
            txtNdTL.setText(tbViewTL.getValueAt(dc, 4).toString().replace("\\n%", "\n"));
        }
        else{
                try {
                    stm=con.createStatement();
                    String sql="update AnswerInfor set Answer=N'"+txtNdTL.getText()+"' where AnswerID='"+maTL+"'";
                    stm.executeUpdate(sql);
                    suaTL();
                    tbViewTL.setModel(TL(bang("select* from AnswerInfor where Username='"+user+"'")));
                    tbViewTL.getSelectionModel().setSelectionInterval(0,dc);
                } catch (SQLException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }//GEN-LAST:event_btnSuaTLActionPerformed

    private void btnXoaTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTLActionPerformed
        try {
            if(JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa câu hỏi này","Thông báo",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
            // TODO add your handling code here:
                stm=con.createStatement();
                stm.executeUpdate("delete from AnswerInfor where AnswerID='"+txtMaTL.getText()+"'");
                tbViewTL.setModel(TL(bang("select* from AnswerInfor where Username='"+user+"'")));
                rowTL=tbViewTL.getRowCount();
                getrow(lbPageTL, rowTL);
                settable(tbViewTL);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnXoaTLActionPerformed

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
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            if(checkChangePass()==true){
                String pass=txtNewPass.getText();
                stm=con.createStatement();
                stm.executeUpdate("update UserInfor set Password='"+pass+"' where Username='"+user+"'");
                setting();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void ckbChangeEmailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ckbChangeEmailMouseClicked
        // TODO add your handling code here:
        if(ckbChangeEmail.isSelected()){
            btnOK.setEnabled(true);
            txtEmail.setEnabled(true);
        }
        else {
            btnOK.setEnabled(false);
            txtEmail.setEnabled(false);
        }
    }//GEN-LAST:event_ckbChangeEmailMouseClicked

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        try {
            // TODO add your handling code here:
            if(checkChangeEmail()==true){
                String mail=txtNewPass.getText();
                stm=con.createStatement();
                stm.executeUpdate("update UserInfor set UserEmail='"+mail+"' where Username='"+user+"'");
                JOptionPane.showConfirmDialog(null, "Ðổi thành công. Đăng nhập lại","Thoát",JOptionPane.OK_OPTION);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnOKActionPerformed

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
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new User(email).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBackCH;
    private javax.swing.JButton btnBackTL;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnCuoiCH;
    private javax.swing.JButton btnCuoiTL;
    private javax.swing.JButton btnDauCH;
    private javax.swing.JButton btnDauTL;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSauCH;
    private javax.swing.JButton btnSauTL;
    private javax.swing.JButton btnSuaCH;
    private javax.swing.JButton btnSuaTL;
    private javax.swing.JButton btnThemCH;
    private javax.swing.JButton btnTruocCH;
    private javax.swing.JButton btnTruocTL;
    private javax.swing.JButton btnXoaCH;
    private javax.swing.JButton btnXoaTL;
    private javax.swing.JCheckBox ckbChangeEmail;
    private javax.swing.JCheckBox ckbChangePass;
    private javax.swing.JCheckBox ckbPass;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbPageCH;
    private javax.swing.JLabel lbPageTL;
    private javax.swing.JTable tbViewCH;
    private javax.swing.JTable tbViewTL;
    private javax.swing.JTextArea txtCH;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMaCH1;
    private javax.swing.JTextField txtMaTL;
    private javax.swing.JTextArea txtNdCH;
    private javax.swing.JTextArea txtNdTL;
    private javax.swing.JTextField txtNewPass;
    private javax.swing.JTextField txtOldPass;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtRenewPass;
    private javax.swing.JTextField txtTagCH;
    private javax.swing.JTextField txtTimCH;
    private javax.swing.JTextField txtTimTL;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
