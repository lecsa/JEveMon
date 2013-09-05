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

    private int startSP = 0;
    private int endSP = 0;
    private String startTime;
    private String endTime;
    
    public SkillInTraining(Type type, long skillpoints, int skillevel, String startTime, String endTime, int startSP, int endSP) {
        super(type, skillpoints, skillevel);
        this.endSP = endSP;
        this.startSP = startSP;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public int getStartSP() {
        return startSP;
    }

    public int getEndSP() {
        return endSP;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
    
}
