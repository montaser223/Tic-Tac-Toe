/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import libs.Player;
import libs.Respond;
import libs.Player;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Eman
 */
public class XoDataBase implements Serializable {

    ArrayList<Player> players = new ArrayList<Player>();
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
    Player newplayer;

    public XoDataBase() {

        try {
             String q = new String("select * from student");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/xo_db", "root", "");
            stmt = con.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Player> selectplayer() {

        try {
            queryString = new String("select *from player");
            rs = stmt.executeQuery(queryString);
            //rs.first();

            while (rs.next()) {
                Player newPlayer = new Player(rs.getString("username"), rs.getString("score"), rs.getString("status"));
                players.add(newPlayer);
                ID = rs.getInt("ID");
//                System.out.println(rs.getInt("ID"));
//                System.out.println(rs.getString("firstname"));
//                System.out.println(rs.getString("lastname"));
//                System.out.println(rs.getString("username"));
//                System.out.println(rs.getString("password"));
//                System.out.println(rs.getInt("score"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(XoDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return players;
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

    public int updateStatus(String status, String _username) {
        int res = 0;
        queryString = "update player set status='" + status + "' where username='" + _username + "'";
        try {
            res = stmt.executeUpdate(queryString);
//                    executeQuery(queryString);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }

    public Player check_username_password(String _Username, String Password) {
        Player NewPlayer = new Player();
        NewPlayer.setUsername(_Username);
        NewPlayer.setPassword(Password);

        try {
            queryString = new String("select username ,password from player where username='" + NewPlayer.getUsername() + "' and password='" + NewPlayer.getPassword() + "'");

            rs = stmt.executeQuery(queryString);
            //  username doesn't exist 
            if (!rs.next()) {
                NewPlayer.setRespond(Respond.FAILURE);
                NewPlayer.setUsername(null);
                NewPlayer.setPassword(null);
                return NewPlayer;
            } else {

                try {
                    queryString = new String("select *from player where username='" + NewPlayer.getUsername() + "'");
                    rs = stmt.executeQuery(queryString);
                    //rs.first();

                    while (rs.next()) {

                        NewPlayer.setUsername(rs.getString("username"));
                        NewPlayer.setScour((rs.getString("score")));
                        NewPlayer.setState(rs.getString("status"));
                        NewPlayer.setRespond(Respond.SUCCESS);

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(XoDataBase.class.getName()).log(Level.SEVERE, null, ex);
                }
                return NewPlayer;
            }

        } catch (SQLException ex) {
            Logger.getLogger(XoDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        // username exists

        //select_specifec_player(NewPlayer.getUsername());
        NewPlayer.setRespond(Respond.SUCCESS);
        return NewPlayer;

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

    public static void main(String args[]) {

        Player p = new Player();
        // p.setUsername("emansol");
        //p.setPassword("123");
        XoDataBase c = new XoDataBase();
        p = c.check_username_password("emansol", "123");
        System.out.println(p.getUsername());
        System.out.println(p.getRespond());
        System.out.println(p.getScour());
        System.out.println(p.getState());

    }

}
