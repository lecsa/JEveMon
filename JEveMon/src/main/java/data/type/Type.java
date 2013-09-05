/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.type;

/**
 *
 * @author lecsa
 */
public class Type {
    public int id;
    public String name;
    public String description;
    public int marketGroupID;
    public int groupID;

    public Type(int id, String name, String description, int marketGroupID, int groupID) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.marketGroupID = marketGroupID;
        this.groupID = groupID;
    }
    @Override
    public String toString() {
        return name;
    }
    
}
