/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author lecsa
 */
public class Item extends Type{

    public int amount = 0;
    
    public Item(int id, String name, String description, int marketGroupID, int groupID) {
        super(id, name, description, marketGroupID, groupID);
    }
    
    public Item(Type t){
        super(t.id, t.name, t.description, t.marketGroupID, t.groupID);
    }
}
