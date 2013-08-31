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
public class Item extends Type{

    public int quantity = 0;
    public ArrayList<Item> containedItems = new ArrayList();
    
    public Item(int id, String name, String description, int marketGroupID, int groupID) {
        super(id, name, description, marketGroupID, groupID);
    }
    
    public Item(Type t){
        super(t.id, t.name, t.description, t.marketGroupID, t.groupID);
    }
    
    public Item(Type t, int quantity){
        super(t.id, t.name, t.description, t.marketGroupID, t.groupID);
        this.quantity = quantity;
    }
}
