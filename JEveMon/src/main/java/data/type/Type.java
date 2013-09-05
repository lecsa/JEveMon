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
    protected int id;
    protected String name;
    protected String description;
    protected int marketGroupID;
    protected int groupID;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMarketGroupID() {
        return marketGroupID;
    }

    public int getGroupID() {
        return groupID;
    }
    
}
