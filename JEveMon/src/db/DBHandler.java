/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import API.APIHandler;
import data.Station;
import data.Type;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
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
        Station station=new Station(0,"Unknown station");
        long realID=0;
        if(Long.toString(locationID).startsWith("66")){//station
            realID=locationID-6000001;
            station=getStationByID(realID);
        }else if(Long.toString(locationID).startsWith("67")){//outpost
            realID=locationID-6000000;
            station=APIHandler.getStationByID(realID);
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
//    private void createSolarSystems(){
//        String query = "CREATE TABLE staStations(stationID int,security int,dockingCostPerVolume float,maxShipVolumeDockable float,officeRentalCost int,operationID int,stationTypeID int,corporationID int,solarSystemID int,constellationID int,regionID int,stationName varchar(100),x float,y float,z float,reprocessingEfficiency float,reprocessingStationsTake float,reprocessingHangarFlag int,PRIMARY KEY (stationID))";
//        try{
//            PreparedStatement st = c.prepareStatement(query);
//            st.execute();
//        }catch(SQLException e){
//            //JOptionPane.showMessageDialog(null, "Adatbázis lekérés hiba. getStationByID()", "Hiba", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//    private void exe(String q){
//        try{
//            PreparedStatement st = c.prepareStatement(q);
//            st.execute();
//        }catch(SQLException e){
//            //JOptionPane.showMessageDialog(null, "Adatbázis lekérés hiba. getStationByID()", "Hiba", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//    public static void main(String[] args) throws Exception{
//        DBHandler db = new DBHandler();
//        db.createSolarSystems();
//        
//        File f = new File("sta.sql");
////        FileOutputStream fos = new FileOutputStream(new File("sta.sql"));
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
//        String line = "";
//        while((line=br.readLine())!=null){
//            if(line.startsWith("INSERT INTO `staStations`")){
//                db.exe(line);
////                fos.write(line.getBytes());
//            
//            }
//        }
////        fos.flush();
//        br.close();
////        fos.close();
//    }
}
