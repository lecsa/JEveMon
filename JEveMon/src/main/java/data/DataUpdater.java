/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import API.APIHandler;
import API.APIKey;
import UI.MainFrame;
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

/**
 *
 * @author lecsa
 */
public class DataUpdater {
    
    private DataProvider dp = new DataProvider();
    public static boolean isUpdating = false;
    private DataUpdateFinishedNotifier notifier = new DataUpdateFinishedNotifier();
    private int updateIn = 5*60;//seconds
    private int waiting = 0;
    private boolean forceUpdate = true;
    
    public DataUpdater(){
        
    }
    
    public void addListener(DataUpdateFinishedListener listener){
        notifier.addListener(listener);
    }
    
    public DataProvider getDp() {
        return dp;
    }
    
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
                            //Thread.sleep(4950);//testing
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
        
        File keysdir = new File("apikeys");
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
                        if( MainFrame.settings.isDebug ) System.out.println("Adding key: "+((APIKey)o).getName());
                        Account currentAccount = APIHandler.getAccount((APIKey)o);
                        EVECharacter[] characters = APIHandler.getCharacters((APIKey)o);
                        for(int n=0;n<characters.length;n++){
                            APIHandler.fillCharacterData(characters[n]);
                            //reload saves asset and journal data
                            for(int k=0;k<assetAndJournalBackup.size();k++){
                                if( characters[n] != null ){
                                    if( assetAndJournalBackup.get(k).id == characters[n].id ){
                                        characters[n].assets = assetAndJournalBackup.get(k).assets;
                                        characters[n].walletJournal = assetAndJournalBackup.get(k).walletJournal;
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
                        ois.close();
                        fis.close();
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
