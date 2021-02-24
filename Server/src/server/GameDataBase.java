/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Eman
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import libs.Game;
public class GameDataBase {
    
    int ID;
    int player1_id, player2_id, game_id;
    String positions[] = new String[9];
    Connection con = null;
    Statement stmt = null;
    String queryString = null;
    ResultSet rs = null;
    
    GameDataBase(){
    
    
    try {
            // String q = new String("select * from student");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/xo_db", "root", "");

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
public Game return_positions_of_tow_players(Game newGame){
        try {
            select_player1_id(newGame.getPlayerX());
            select_player2_id(newGame.getPlayerO());
            select_game_id_specific_player(player1_id,player2_id);
            System.out.println(game_id);
            queryString = " select bn1,bn2,bn3,bn4,bn5,bn6,bn7,bn8,bn9,playerX,playerO from game where ID='"+game_id+"'";
            
            rs = stmt.executeQuery(queryString);
            
            
            while (rs.next()) {
                positions[0] = rs.getString("bn1");
                positions[1] = rs.getString("bn2");
                positions[2] = rs.getString("bn3");
                positions[3] = rs.getString("bn4");
                positions[4] = rs.getString("bn5");
                positions[5] = rs.getString("bn6");
                positions[6] = rs.getString("bn7");
                positions[7] = rs.getString("bn8");
                positions[8] = rs.getString("bn9");
                
                
                //System.out.println(rs.getInt("ID"));
            }
            newGame.setRecordedGamePosition(positions);
            
            
        }catch (SQLException ex) {
            System.out.println("server.GameDataBase.return_positions_of_tow_players()");
        }
//            queryString = "delete from game where  ID='"+game_id+"'";
//            stmt = con.createStatement();
//            stmt.executeUpdate(queryString);
//            System.out.println("Record deleted successfully");
//            
//            queryString = "delete from players_record_games where  game_id='"+game_id+"'";
//            stmt = con.createStatement();
//            stmt.executeUpdate(queryString);
//            System.out.println("Record deleted successfully");
//            Logger.getLogger(game.class.getName()).log(Level.SEVERE, null, ex);
              return  newGame;
    
    }
    
    
    
    public void select_player1_id(String username1) {
        try {
            queryString = "select ID from player where username ='" + username1 + "' ";
            rs = stmt.executeQuery(queryString);
            while (rs.next()) {

                set_player1_id(rs.getInt("ID"));

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void select_player2_id(String username2) {

        try {
            queryString = "select ID from player where username ='" + username2 + "' ";
            rs = stmt.executeQuery(queryString);
            while (rs.next()) {

                set_player2_id(rs.getInt("ID"));

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
    
       public void select_game_id_specific_player(int player1_id,int player2_id) {

        try {
            queryString = "select game_id from players_record_games where player1_id ='"+player1_id+"'and player2_id='"+player2_id+"'";
            rs = stmt.executeQuery(queryString);

            while (rs.next()) {
                set_game_id(rs.getInt("game_id"));
                //System.out.println(rs.getInt("ID"));
            }
        } catch (SQLException ex) {
           System.out.println(ex.getMessage());
        }

    }

    public void select_game_id() {

        try {
            queryString = "select *from game";
            rs = stmt.executeQuery(queryString);

            while (rs.next()) {
                set_game_id(rs.getInt("ID"));
                //System.out.println(rs.getInt("ID"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void select_Positions() {
     

        try {
            queryString ="select bn1,bn2,bn3,bn4,bn5,bn6,bn7,bn8,bn9 from game where ID='" + get_game_id() + "'";
            rs = stmt.executeQuery(queryString);

            while (rs.next()) {
                positions[0] = rs.getString("bn1");
                positions[1] = rs.getString("bn2");
                positions[2] = rs.getString("bn3");
                positions[3] = rs.getString("bn4");
                positions[4] = rs.getString("bn5");
                positions[5] = rs.getString("bn6");
                positions[6] = rs.getString("bn7");
                positions[7] = rs.getString("bn8");
                positions[8] = rs.getString("bn9");

                //System.out.println(rs.getInt("ID"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void insert_into_recorded_game() {

        try {
            queryString = " insert into players_record_games(player1_id,player2_id,game_id)"
                    + " values (?,?,?)";
            PreparedStatement preparedStmt = con.prepareStatement(queryString);

            preparedStmt.setInt(1, player1_id);
            preparedStmt.setInt(2, player2_id);
            preparedStmt.setInt(3, game_id);
            preparedStmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public boolean select_from_recorded_game() {

        try {
            //queryString = "select ID from players_record_games where username ='" + player1_id + "' ";

            queryString = new String("select game_id from players_record_games where player1_id='" + player1_id + "' and player2_id='" + player2_id + "'");

            rs = stmt.executeQuery(queryString);
            //  username doesn't exist 

            if (!rs.next()) {
                System.out.println("no");
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(XoDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        // username exist
        return true;

    }

    public void update_into_recorded_game(String[] _posstions) {

        try {
            queryString = "update game set bn1=?, bn2=?, bn3=?, bn4=? ,bn5=? ,bn6=? ,bn7=?,bn8=?,bn9=? where ID='" + game_id + "' ";

            PreparedStatement preparedStmt = con.prepareStatement(queryString);

            System.out.println("Database updated successfully ");
            for (int i = 0; i < 9; i++) {
                preparedStmt.setString(i + 1, _posstions[i]);
                // System.out.println(i);

            }
            preparedStmt.executeUpdate();
            preparedStmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    

    public void set_player1_id(int _id) {
        player1_id = _id;

    }

    public void set_player2_id(int _id) {
        player2_id = _id;

    }

    public void set_game_id(int _id) {
        game_id = _id;

    }

    public int get_game_id() {
        return game_id;

    }

    public int get_player1_id() {
        return player1_id;

    }

    public int get_player2_id() {
        return player2_id;

    }

    public void set_position(String _positions[]) {

        for (int i = 0; i < 9; i++) {
            positions[i] = _positions[i];

        }

    }

    public String[] get_position() {
        return positions;

    }

   public static void main(String args[]) {
//
//        GameDataBase g = new GameDataBase();
//        String _postions[] = {"", "o", "o", "o", "o", "x", "x", "x", "x"};
//        System.out.println(_postions[0]);
//        g.game_record(_postions, "heba", "eman");


//        g.game_record(_postions, "mohamed", "baker");
//        System.out.println(g.get_player1_id()+"     " + g.get_player2_id()+ "    " + g.get_game_id());
//        g.game_record(_postions, "ahemreda", "omnyamostafa");
//        g.game_record(_postions, "ahemreda", "kaledAshraf");
//        
//        g.select_Positions();
//       _postions=g.get_position();
     
        
//      String _postions[]=g.return_positions_of_tow_players( "heba", "baker");
//        if(_postions != null){
//        System.out.println("Eman Null");
//           }
//        else
//        {
//               System.out.println("Not Null"); 
//        }
//         for(int i=0 ;i<9;i++)
//       {
//           
//           System.out.println(_postions[i]);
//       }
//

//        Game gh = new Game();
//        gh.setPlayerX("heba");
//        gh.setPlayerO("baker");
//        gh = g.return_positions_of_tow_players(gh);
//        for(int i = 0 ; i< gh.getRecordedGamePosition().length ;i++)
//        {
//            System.out.println(gh.getRecordedGamePosition()[i]);
//        }
        


    }

}

    

