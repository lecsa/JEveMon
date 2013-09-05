/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.type;

import java.util.ArrayList;

/**
 *
 * @author lecsa
 */
public class Ship extends Item{

    private ArrayList<Item> cargoHold = new ArrayList();
    private ArrayList<Item> fittedItems = new ArrayList();
    private ArrayList<Item> droneBay = new ArrayList();
    
//    public Ship(int id, String name, String description, int marketGroupID, int groupID, int flag) {
//        super(id, name, description, marketGroupID, groupID, flag);
//    }

    public Ship(Type t, int flag) {
        super(t, flag);
    }

    public Ship(Type t, int quantity, int flag) {
        super(t, quantity, flag);
    }
    
    public Ship(Item i, int quantity, int flag) {
        super((Type)i, quantity,flag);
    }
    public ArrayList<Item> getCargoHold() {
        return cargoHold;
    }

    public ArrayList<Item> getFittedItems() {
        return fittedItems;
    }

    public ArrayList<Item> getDroneBay() {
        return droneBay;
    }

}
