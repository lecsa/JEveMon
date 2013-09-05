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
public class Item extends Type implements Comparable{

    private int quantity = 0;
    private ArrayList<Item> containedItems = new ArrayList();
    private int flag;
    
    public Item(int id, String name, String description, int marketGroupID, int groupID, int flag) {
        super(id, name, description, marketGroupID, groupID);
        this.flag = flag;
    }
    
    public Item(Type t,int flag){
        super(t.id, t.name, t.description, t.marketGroupID, t.groupID);
        this.flag = flag;
    }
    
    public Item(Type t, int quantity, int flag){
        super(t.id, t.name, t.description, t.marketGroupID, t.groupID);
        this.quantity = quantity;
        this.flag = flag;
    }
    @Override
    public String toString() {
        return name;
    }
    @Override
    public int compareTo(Object o) {
        int retval = 0;
        if( o != null ){
            if( o instanceof Item ){
                Item c = (Item)o;
                if( c.containedItems.size() > 0 && this.containedItems.isEmpty() ){
                    retval = 1;
                }else if( c.containedItems.isEmpty() && this.containedItems.size() > 0 ){
                    retval = -1;
                }else{
                    retval = this.name.compareTo(c.name);
                }
            }
        }
        return retval;
    }

    public int getQuantity() {
        return quantity;
    }

    public ArrayList<Item> getContainedItems() {
        return containedItems;
    }
    
    
    
}
