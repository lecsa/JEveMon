/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import API.APIKey;
import java.util.ArrayList;
import java.util.Date;
import utils.Utils;

/**
 *
 * @author lecsa
 */
public class EVECharacter {
    
    public int id;
    public String name;
    public double balance;
    public int corpID;
    public String corpName;
    public String cloneName;
    public String dayOfBirth;
    public ArrayList<Skill> skills = new ArrayList();
    public ArrayList<SkillInTraining> trainingQueue = new ArrayList();
    public boolean isTraining;
    public int skillpoints = 0;
    public int cloneSkillpoints = 0;
    public int intelligence,memory,perception,charisma,willpower;
    public APIKey key;
    
    public EVECharacter(int id, String name, APIKey key){
        this.id = id;
        this.name=name;
        this.key = key;
    }
    
    public SkillInTraining getSkillInTraining(){
        SkillInTraining retval = null;
            for(int i=0;i<trainingQueue.size() && retval == null;i++){
                if( trainingQueue.get(i) != null ){
                    if( !trainingQueue.get(i).endTime.equals("") ){
                        Date now = Utils.getNowUTC();
                        Date fin = Utils.getUTC(trainingQueue.get(i).endTime);
                        if( fin.after(now) ){
                            retval = trainingQueue.get(i);
                        }
                    }
                }
            }
        return retval;
    }
}
