/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author lecsa
 */
public class Station{
    public long stationID;
    public String name;
      

    public Station(long stationID, String name) {
        this.stationID = stationID;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Hangar{" + "stationID=" + stationID + ", name=" + name + '}';
    }
    
    
    
}
