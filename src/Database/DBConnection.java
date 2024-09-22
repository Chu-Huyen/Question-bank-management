/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ADMIN
 */
public class DBConnection {
    Connection con;
    ResultSet rs=null;
    Statement stm;

    String server="CHUTNGOCHANH\\CTTH";
    String user="sa2";
    String pass="indosoudo1117";
    String db="QuanLyCauHoi";
    int port=1433;
    public Connection DBConnection() throws ClassNotFoundException, SQLException {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        //con=DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlycauhoi","root","");
        SQLServerDataSource ds=new SQLServerDataSource();
        ds.setUser(user);
        ds.setPassword(pass);
        ds.setDatabaseName(db);
        ds.setPortNumber(port);
        ds.setServerName(server);
        return con=ds.getConnection();
    }
    
    /*public ResultSet GetData(String tbName) throws SQLException{
        this.stm= this.con.createStatement();
        String sql = "select * from "+tbName;
        rs=stm.executeQuery(sql);
        return rs;
    }*/
    public void Close() throws SQLException {
        if (this.con!=null) this.con.close();
    }
}
