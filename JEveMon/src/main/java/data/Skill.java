/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author lecsa
 */
public class Skill {
    public Type type;
    public long skillpoints;
    public int skillevel;

    public Skill(Type type, long skillpoints, int skillevel) {
        this.type = type;
        this.skillpoints = skillpoints;
        this.skillevel = skillevel;
    }
    @Override
    public String toString() {
        return type.name;
    }
}
