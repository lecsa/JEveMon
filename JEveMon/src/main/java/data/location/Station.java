/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.location;

import data.type.Item;
import java.util.ArrayList;

/**
 *
 * @author lecsa
 */
public class Station implements Comparable{
    private long stationID;
    private String name;
    private ArrayList<Item> items = new ArrayList();
    
    public Station(long stationID, String name) {
        this.stationID = stationID;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        int retval = 0;
        if( o != null ){
            if( o instanceof Station ){
                Station c = (Station)o;
                retval = this.name.compareTo(c.name);
            }
        }
        return retval;
    }

    public long getStationID() {
        return stationID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    
    
    
    
}
