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
    public int id;
    public String name;
    public long groupSP = 0;
    public int lvl5 = 0;
    public int lvl4 = 0;
    
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
        groupSP+=s.skillpoints;
        if(s.skillevel == 5){
            lvl5++;
        }else if(s.skillevel == 4){
            lvl4++;
        }
    }
    
}
