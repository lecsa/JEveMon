/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.skill;

import data.type.Type;

/**
 *
 * @author lecsa
 */
public class Skill {
    private Type type;
    private long skillpoints;
    private int skillLevel;

    public Skill(Type type, long skillpoints, int skillevel) {
        this.type = type;
        this.skillpoints = skillpoints;
        this.skillLevel = skillevel;
    }
    
    @Override
    public String toString() {
        return type.getName();
    }

    public Type getType() {
        return type;
    }

    public long getSkillpoints() {
        return skillpoints;
    }

    public int getSkillLevel() {
        return skillLevel;
    }
    
}
