/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import data.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lecsa
 */
public class DBHandler {
    Connection c = null;
    public DBHandler(){
        
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:eve.db");
        } catch ( Exception e ) {
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    
    public Type getTypeByID(int typeID){
        Type t = null;
        try{
            PreparedStatement st = c.prepareStatement("SELECT * FROM invtypes WHERE typeid='"+typeID+"'");
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                t = new Type(rs.getInt("typeID"), rs.getString("typeName"), rs.getString("description"), rs.getInt("marketGroupID"), rs.getInt("groupID"));
                
            }
        }catch(SQLException ex){
            System.out.println("SQLE: "+ex.getMessage());
        }
        return t;
    }
    
    public String getGroupNameByID(int groupID){
        String retval = "Unknown";
        try{
            PreparedStatement st = c.prepareStatement("SELECT groupName FROM invgroups WHERE groupID='"+groupID+"'");
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                retval = rs.getString("groupName");
                
            }
        }catch(SQLException ex){
            System.out.println("SQLE: "+ex.getMessage());
        }
        return retval;
    }
    
}
