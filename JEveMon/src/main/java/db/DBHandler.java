/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import API.APIHandler;
import data.Station;
import data.Type;

import java.sql.*;
import java.util.ArrayList;

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
        Type t = new Type(typeID, "Unknown item: "+typeID, "Unknown", 0, 0);
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
    public Type[] getTypeByName(String typeNamePart) {
        ArrayList<Type> t = new ArrayList();
        
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM invtypes WHERE typeName LIKE '%" + typeNamePart + "%'");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                t.add(new Type(rs.getInt("typeID"), rs.getString("typeName"), rs.getString("description"), rs.getInt("marketGroupID"), rs.getInt("groupID")));

            }
        } catch (SQLException ex) {
            System.out.println("SQLE: " + ex.getMessage());
        }
        return t.toArray(new Type[t.size()]);
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
        if( station.name.startsWith("Unknown") ){//NPC station
            station = this.getStationByID(locationID);
        }
        if( station.name.startsWith("Unknown") ){//not found
            station = new Station(locationID,"Unknown location: "+locationID);
        }
        return station;
    }
    public Station getStationByID(long stationID){
    
    Station h=new Station(stationID,"Unknown station: "+stationID);
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
    public static void main(String[] args) {
        DBHandler db = new DBHandler();
        Type[] t = db.getTypeByName("Spaceship");
        for(int i=0;i<t.length;i++){
            System.out.println(t[i].name+" - "+t[i].id+" - "+t[i].groupID);
        }
        
    }
}
