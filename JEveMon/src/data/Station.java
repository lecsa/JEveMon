/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;

/**
 *
 * @author lecsa
 */
public class Station{
    public long stationID;
    public String name;
    public ArrayList<Item> items = new ArrayList();
    
    public Station(long stationID, String name) {
        this.stationID = stationID;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Hangar{" + "stationID=" + stationID + ", name=" + name + '}';
    }
    
    
    
}
