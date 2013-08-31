/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

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

}
