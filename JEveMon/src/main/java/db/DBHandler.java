/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import API.APIHandler;
import data.Station;
import data.Type;

import java.sql.*;

/**
 * @author lecsa
 */
public class DBHandler {
    Connection connection = null;

    public DBHandler() {

        try {
            Class.forName("org.sqlite.JDBC");
            //relative to jar location
            connection = DriverManager.getConnection("jdbc:sqlite:database/eve.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Type getTypeByID(int typeID) {
        Type t = null;
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM invtypes WHERE typeid='" + typeID + "'");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                t = new Type(rs.getInt("typeID"), rs.getString("typeName"), rs.getString("description"), rs.getInt("marketGroupID"), rs.getInt("groupID"));

            }
        } catch (SQLException ex) {
            System.out.println("SQLE: " + ex.getMessage());
        }
        return t;
    }

    public String getGroupNameByID(int groupID) {
        String retval = "Unknown";
        try {
            PreparedStatement st = connection.prepareStatement("SELECT groupName FROM invgroups WHERE groupID='" + groupID + "'");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                retval = rs.getString("groupName");

            }
        } catch (SQLException ex) {
            System.out.println("SQLE: " + ex.getMessage());
        }
        return retval;
    }
    public Station getStationByLocationID(long locationID){
        Station station = null;
        station = APIHandler.getStationByID(locationID);
        if( station.stationID == 0 ){//NPC station
            station = this.getStationByID(locationID);
        }
        if( station.stationID == 0 ){//not found
            station = new Station(0,"Unknown location: "+locationID);
        }
        return station;
    }
    public Station getStationByID(long stationID){
    
    Station h=new Station(0,"Unknown station: "+stationID);
        try{
            PreparedStatement st = connection.prepareStatement("SELECT * FROM staStations WHERE stationID='"+Long.toString(stationID)+"'");
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
