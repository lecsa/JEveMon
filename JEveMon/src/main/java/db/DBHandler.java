/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import API.APIHandler;
import data.location.Station;
import data.type.Type;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author lecsa
 */
public class DBHandler {
    private Connection connection = null;
    private static final int SHIP_CATEGORY_ID = 6;
    
    public DBHandler() {

        try {
            Class.forName("org.sqlite.JDBC");
            //relative to jar location
            connection = DriverManager.getConnection("jdbc:sqlite:database/eve.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(System.out);
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
        Station station;
        station = APIHandler.getStationByID(locationID);
        if( station.getName().startsWith("Unknown") ){//NPC station
            station = this.getStationByID(locationID);
        }
        if( station.getName().startsWith("Unknown") ){//not found
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
            e.printStackTrace(System.out);
        }
    return h;
    }
    
    public int getCategoryByGroupID(int groupID){
        int retval = 0;
        try{
            PreparedStatement st = connection.prepareStatement("SELECT * FROM invgroups WHERE groupID='"+Integer.toString(groupID)+"'");
            ResultSet rs = st.executeQuery();
            String name=null;
        while(rs.next()!=false){
            retval = rs.getInt("categoryID");
        }
        }catch(SQLException e){
            //JOptionPane.showMessageDialog(null, "Adatbázis lekérés hiba. getStationByID()", "Hiba", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(System.out);
        }
        return retval;
    }
    
    public boolean isShip(int typeID){
    boolean ship = false;
        try{
            Type t = getTypeByID(typeID);
            PreparedStatement st = connection.prepareStatement("SELECT * FROM invgroups WHERE groupID='"+Integer.toString(t.getGroupID())+"'");
            ResultSet rs = st.executeQuery();
            String name=null;
        while(rs.next()!=false){
            ship = SHIP_CATEGORY_ID == rs.getInt("categoryID");
        }
        }catch(SQLException e){
            //JOptionPane.showMessageDialog(null, "Adatbázis lekérés hiba. getStationByID()", "Hiba", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(System.out);
        }
    return ship;
    }
    
    public static void main(String[] args) {
        DBHandler db = new DBHandler();
        Type[] t = db.getTypeByName("");
        for(int i=0;i<t.length;i++){
            System.out.println(t[i].getName()+" - "+t[i].getId()+" - "+t[i].getGroupID());
        }
//        Type t = db.getTypeByID(586);
//        System.out.println(t.getName());
//        System.out.println(db.getGroupNameByID(t.getGroupID()));
    }
}
