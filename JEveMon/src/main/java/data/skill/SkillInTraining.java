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
public class SkillInTraining extends Skill{

    public int startSP = 0;
    public int endSP = 0;
    public String startTime;
    public String endTime;
    
    public SkillInTraining(Type type, long skillpoints, int skillevel, String startTime, String endTime, int startSP, int endSP) {
        super(type, skillpoints, skillevel);
        this.endSP = endSP;
        this.startSP = startSP;
        this.endTime = endTime;
        this.startTime = startTime;
    }
    
}
