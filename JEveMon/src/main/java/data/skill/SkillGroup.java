/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.skill;

import java.util.ArrayList;

/**
 *
 * @author lecsa
 */
public class SkillGroup {
    private int id;
    private String name;
    private long groupSP = 0;
    private int lvl5 = 0;
    private int lvl4 = 0;
    
    private ArrayList<Skill> skills = new ArrayList();

    public SkillGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public ArrayList<Skill> getSkills(){
        return this.skills;
    }
    
    public void addSkill(Skill s){
        skills.add(s);
        groupSP+=s.getSkillpoints();
        if(s.getSkillLevel() == 5){
            lvl5++;
        }else if(s.getSkillLevel() == 4){
            lvl4++;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getGroupSP() {
        return groupSP;
    }

    public int getLvl5() {
        return lvl5;
    }

    public int getLvl4() {
        return lvl4;
    }

    
    
}
