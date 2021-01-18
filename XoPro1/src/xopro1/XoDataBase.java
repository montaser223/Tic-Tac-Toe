/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xopro1;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author black horse
 */
public class XoDataBase {

    int ID;
    String firstname;
    String lastname;
    String username;
    String password;
    int score;
    Connection con = null;
    Statement stmt = null;
    String queryString = null;
    ResultSet rs = null;

    public XoDataBase() {

        try {
            // String q = new String("select * from student");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/xo_db", "root", "");
            stmt = con.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void selectplayer() {

        try {
            queryString = new String("select *from player");
            rs = stmt.executeQuery(queryString);
            //rs.first();

            while (rs.next()) {
                ID = rs.getInt("ID");
                System.out.println(rs.getInt("ID"));
                System.out.println(rs.getString("firstname"));
                System.out.println(rs.getString("lastname"));
                System.out.println(rs.getString("username"));
                System.out.println(rs.getString("password"));
                System.out.println(rs.getInt("score"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(XoDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sign_up(String _firstname, String _lastname, String _username, String _password) {

        queryString = new String("select username from player where username='" + _username + "'");
        try {
            rs = stmt.executeQuery(queryString);
            //  while(rs.next())
            if (!rs.next()) {

                queryString = " insert into player (firstname,lastname, username, password)"
                        + " values (?, ?, ?, ?)";

                PreparedStatement preparedStmt = con.prepareStatement(queryString);

                preparedStmt.setString(1, _firstname);
                preparedStmt.setString(2, _lastname);
                preparedStmt.setString(3, _username);
                preparedStmt.setString(4, _password);

                preparedStmt.execute();
            } else {
                System.out.println("please Enter anther username this actually used ");
            }
        } catch (SQLException ex) {
            Logger.getLogger(XoDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int check_password(String _password) {

        try {
            queryString = new String("select password from player where password='" + _password + "'");

            rs = stmt.executeQuery(queryString);
            //  the password doesn't exist
            if (!rs.next()) {
                return 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(XoDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        //  the password exists
        return 1;
    }

    public int check_username(String _username) {

        try {
            queryString = new String("select username from player where username='" + _username + "'");

            rs = stmt.executeQuery(queryString);
            //  username doesn't exist 
            if (!rs.next()) {
                return 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(XoDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        // username exists
        return 1;
    }

    public int check_username_password(String _password, String _username) {

        try {
            queryString = new String("select username ,password from player where username='" + _username + "' and password='" + _password + "'");

            rs = stmt.executeQuery(queryString);
            //  username doesn't exist 
            if (!rs.next()) {
                return 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(XoDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        // username exists
        return 1;
    }

    public int sign_in(String _username, String _password) {
        if (check_username_password(_password, _username) == 1) {
            {

                return 1;
            }
        }
//false
        return 0;
    }

    @Override
    public void finalize() {

        try {
            stmt.close();
            con.close();
            //System.out.println("closed");

        } catch (SQLException e) {
            System.out.println("Uh, oh ... could not close db connection");
        }

    }

    public static void main(String[] args) {
        XoDataBase o = new XoDataBase();

        System.out.println(o.sign_in("emysoliman", "6789"));
        System.out.println(o.sign_in("ahemreda", "6789"));
        o.sign_up("khaled", "ashraf", "kaledAshraf", "102030");
        // o.selectplayer();
        //o.sign_up("eman","soliman","emysoliman","2345");
        //o.sign_up("eman","soliman","omnyamostafa","2345");
        //o.sign_up("eman","soliman","omnyamostaf","2345");
        // System.out.println(o.check_password("123"));
        //System.out.println(o.check_password("2345"));
        //System.out.println(o.check_username("emysoliman"));
        //System.out.println(o.check_username("emyso"));
        o.finalize();

    }
}
