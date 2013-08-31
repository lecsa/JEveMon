/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import data.Station;
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
        Type t = new Type(0, "Unknown", "Unknown", 0, 0);
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
    
    public Station getStationByLocationID(long locationID){
        Station station=null;
        long realID=0;
        if(Long.toString(locationID).startsWith("66")){//station
            realID=locationID-6000001;
            station=getStationByID(realID);
        }else if(Long.toString(locationID).startsWith("67")){//outpost
            realID=locationID-6000000;
//            EveAPIOutposts api = new EveAPIOutposts();
//            station=api.getStationByID(realID);
        }
        return station;
    }
    public Station getStationByID(long stationID){
    
    Station h=null;
        try{
            PreparedStatement st = c.prepareStatement("SELECT * FROM staStations WHERE stationID='"+Long.toString(stationID)+"'");
            ResultSet rs = st.executeQuery();
            String name=null;
        while(rs.next()!=false){
            name=rs.getString("stationName");
        }
        if(name!=null){
            h=new Station(stationID, name);
        }
        }catch(SQLException e){
            //JOptionPane.showMessageDialog(null, "Adatbázis lekérés hiba. getStationByID()", "Hiba", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    return h;
    }
    
}
