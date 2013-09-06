/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import API.APIHandler;
import API.APIKey;
import data.account.Account;
import data.character.EVECharacter;
import event.done.DataUpdateFinishedEvent;
import event.done.DataUpdateFinishedListener;
import event.done.DataUpdateFinishedNotifier;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import settings.Settings;
import utils.FileSystem;

/**
 *
 * @author lecsa
 */
public class DataUpdater {
    /**
     * Data source for the UI.
     */
    private DataProvider dp = new DataProvider();
    /**
     * is update in progress.
     */
    public static boolean isUpdating = false;
    /**
     * This object will notify all of the listeners after a data update.
     */
    private DataUpdateFinishedNotifier notifier = new DataUpdateFinishedNotifier();
    /**
     * update in every updateIn seconds.
     */
    private int updateIn = 5*60;//seconds
    /**
     * elapsed seconds since the last update.
     */
    private int waiting = 0;
    /**
     * force next update.
     */
    private boolean forceUpdate = true;
    
    public DataUpdater(){
        
    }
    
    public void addListener(DataUpdateFinishedListener listener){
        notifier.addListener(listener);
    }
    
    public DataProvider getDataProvider() {
        return dp;
    }
    /**
     * Starts the updating thread.
     */
    public void start(){
        Thread updateThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while( true ){
                    if( waiting >= updateIn ){
                        fillAccountsAndCharacters();
                        waiting = 0;
                    }else{
                        try {
                            Thread.sleep(1000);
                            waiting++;
                            if( forceUpdate ){
                                waiting = updateIn;
                                forceUpdate = false;
                            }
                        } catch (InterruptedException ex) {
                            System.out.println("IEX: "+ex.getMessage());
                        }
                    }
                    
                }
            }
        });
        updateThread.start();
    }
    /**
     * update in the next second.
     */
    public void forceNextUpdate(){
        forceUpdate = true;
    }
    /**
     * Fills the account and character objects in the DataProvider instance and notifies all listeners after the update is done.
     */
    private void fillAccountsAndCharacters(){
        isUpdating = true;
        //backup asset and journal data
        ArrayList<EVECharacter> assetAndJournalBackup = new ArrayList();
        for(int i=0;i<dp.accounts.size();i++){
            for(int n=0;n<dp.accounts.get(i).getCharacters().size();n++){
                if( dp.accounts.get(i).getCharacters().get(n) != null ){
                    assetAndJournalBackup.add(dp.accounts.get(i).getCharacters().get(n));
                }
            }
        }
        
        File keysdir = FileSystem.getFile("apikeys");
        if( keysdir.exists() && keysdir.isDirectory() ){
            File[] list = keysdir.listFiles();
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            ArrayList<Account> accounts = new ArrayList();
            for( int i=0;i<list.length;i++ ){
                try{
                    fis = new FileInputStream(list[i]);
                    ois = new ObjectInputStream(fis);
                    Object o = ois.readObject();
                    if( o instanceof APIKey ){
                        if( Settings.isDebug ) System.out.println("Adding key: "+((APIKey)o).getName());
                        Account currentAccount = APIHandler.getAccount((APIKey)o);
                        EVECharacter[] characters = APIHandler.getCharacters((APIKey)o);
                        if ( Settings.isDebug ) System.out.println( "Number of Characters on this Account: " + characters.length);
                        for(int n=0;n<characters.length;n++){
                            if ( Settings.isDebug ) System.out.println( "Filling Character Data for Number #" + n + 1);
                            APIHandler.fillCharacterData(characters[n]);
                            //reload saves asset and journal data
                            for(int k=0;k<assetAndJournalBackup.size();k++){
                                if( characters[n] != null ){
                                    if( assetAndJournalBackup.get(k).getId() == characters[n].getId() ){
                                        characters[n].setAssets( assetAndJournalBackup.get(k).getAssets() );
                                        characters[n].setWalletJournal( assetAndJournalBackup.get(k).getWalletJournal() );
                                    }
                                }
                            }
                            currentAccount.addCharacter(characters[n]);
                        }
                        accounts.add(currentAccount);
                    }
                }catch(IOException ex){
                    System.out.println("IOE: "+ex.getMessage());
                }catch(ClassNotFoundException ex){
                    System.out.println("CNFE: "+ex.getMessage());
                }finally{
                    try{
                        if ( ois != null ) ois.close();
                        if ( fis != null ) fis.close();
                    }catch(Exception e){
                    
                    }
                }
            }
            this.dp.accounts = accounts;
        }
        
        
        notifier.notifyListeners(new DataUpdateFinishedEvent(this));
        isUpdating = false;
    }
    
    
    
}
