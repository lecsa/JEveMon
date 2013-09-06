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
import data.type.Ship;
import db.DBHandler;
import java.util.ArrayList;
import java.util.Date;
import settings.Settings;
import utils.Utils;

/**
 * Representation of 1 EVE character.
 * @author lecsa
 */
public class EVECharacter {
    /**
     * Character id.
     */
    private int id;
    /**
     * Character name.
     */
    private String name;
    /**
     * Balance.
     */
    private double balance;
    /**
     * Corporation id of the character.
     */
    private int corpID;
    /**
     * Corporation name of the character.
     */
    private String corpName;
    /**
     * The name of the currently active clone.
     */
    private String cloneName;
    /**
     * Day of birth.
     */
    private String dayOfBirth;
    /**
     * Skills of the character.
     */
    private ArrayList<Skill> skills = new ArrayList();
    /**
     * Training queue of the character.
     */
    private ArrayList<SkillInTraining> trainingQueue = new ArrayList();
    /**
     * is the character training?
     */
    private boolean isTraining;
    /**
     * Skillpoints of the character.
     */
    private int skillpoints = 0;
    /**
     * Skillpoints held by the currently active clone.
     */
    private int cloneSkillpoints = 0;
    /**
     * Character attributes.
     */
    private Attributes attributes;
    /**
     * APIKey of the character.
     */
    private APIKey key;
    /**
     * Assets of the character.
     */
    private ArrayList<Station> assets = new ArrayList();
    /**
     * WalletJournal.
     */
    private ArrayList<JournalElement> walletJournal = new ArrayList();
    
    public EVECharacter(int id, String name, APIKey key){
        attributes = new Attributes();
        this.id = id;
        this.name=name;
        this.key = key;
    }
    /**
     * Get the currently trained skill.
     * @return 
     */
    public SkillInTraining getSkillInTraining(){
        SkillInTraining retval = null;
            for(int i=0;i<trainingQueue.size() && retval == null;i++){
                if( trainingQueue.get(i) != null ){
                    if( !trainingQueue.get(i).getEndTime().equals("") ){
                        Date now = Utils.getNowUTC();
                        Date fin = Utils.getUTC(trainingQueue.get(i).getEndTime());
                        if( fin.after(now) ){
                            retval = trainingQueue.get(i);
                        }
                    }
                }
            }
        return retval;
    }
    /**
     * Add non-ship items to assets (specified station).
     * @param i the item
     * @param stationID id of the station
     */
    public void addItemToAssets(Item i,long stationID){
        Station s = getStation(stationID);
        
        if( s == null ){
            DBHandler db = new DBHandler();
            s = db.getStationByLocationID(stationID);
            assets.add(s);
        }
        s.getItems().add(i);
        if ( Settings.isDebug ) System.out.println("Adding: "+i.getName()+" x"+i.getQuantity()+" to "+s.getName());
    }
    /**
     * Add ship items to assets (specified station).
     * @param i ship
     * @param stationID id of the station
     */
    public void addShipToAssets(Ship i,long stationID){
        Station s = getStation(stationID);
        
        if( s == null ){
            DBHandler db = new DBHandler();
            s = db.getStationByLocationID(stationID);
            assets.add(s);
        }
        s.getShips().add(i);
        if ( Settings.isDebug ) System.out.println("Adding: "+i.getName()+" x"+i.getQuantity()+" to "+s.getName());
    }
    /**
     * Get the station object of the given id if it exists in the assets arraylist.
     * @param stationID id of the station
     * @return existing station object or null
     */
    private Station getStation(long stationID){
    Station sta = null;
        for(int i=0;i<assets.size() && sta == null;i++){
            if( assets.get(i).getStationID() == stationID ){
                sta = assets.get(i);
            }
        }
    return sta;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public int getCorpID() {
        return corpID;
    }

    public String getCorpName() {
        return corpName;
    }

    public String getCloneName() {
        return cloneName;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public ArrayList<SkillInTraining> getTrainingQueue() {
        return trainingQueue;
    }

    public boolean isTraining() {
        return isTraining;
    }
    public void setTraining(boolean training){
        isTraining = training;
    }
    public int getSkillpoints() {
        return skillpoints;
    }

    public int getCloneSkillpoints() {
        return cloneSkillpoints;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public APIKey getKey() {
        return key;
    }

    public ArrayList<Station> getAssets() {
        return assets;
    }

    public ArrayList<JournalElement> getWalletJournal() {
        return walletJournal;
    }

    public void setAssets(ArrayList<Station> assets) {
        this.assets = assets;
    }

    public void setWalletJournal(ArrayList<JournalElement> walletJournal) {
        this.walletJournal = walletJournal;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public void setSkillpoints(int skillpoints) {
        this.skillpoints = skillpoints;
    }

    public void setIsTraining(boolean isTraining) {
        this.isTraining = isTraining;
    }

    public void setCloneName(String cloneName) {
        this.cloneName = cloneName;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCorpID(int corpID) {
        this.corpID = corpID;
    }

    public void setCloneSkillpoints(int cloneSkillpoints) {
        this.cloneSkillpoints = cloneSkillpoints;
    }
    
}
