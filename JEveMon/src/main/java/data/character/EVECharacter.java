/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.character;

import API.APIKey;
import data.type.Item;
import data.journal.JournalElement;
import data.skill.Skill;
import data.skill.SkillInTraining;
import data.location.Station;
import data.character.Attributes;
import db.DBHandler;
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
    public Attributes attributes;
    public APIKey key;
    public ArrayList<Station> assets = new ArrayList();
    public ArrayList<JournalElement> walletJournal = new ArrayList();
    
    public EVECharacter(int id, String name, APIKey key){
        attributes = new Attributes();
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
    
    public void addAsset(Item i,long stationID){
        Station s = getStation(stationID);
        
        if( s == null ){
            DBHandler db = new DBHandler();
            s = db.getStationByLocationID(stationID);
            assets.add(s);
        }
        s.items.add(i);
        //System.out.println("Adding: "+i.name+" x"+i.quantity+" to "+s.name);
    }
    
    private Station getStation(long stationID){
    Station sta = null;
        for(int i=0;i<assets.size() && sta == null;i++){
            if( assets.get(i).stationID == stationID ){
                sta = assets.get(i);
            }
        }
    return sta;
    }
    
}
