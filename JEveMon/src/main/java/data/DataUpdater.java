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
    
    public DataUpdater(){
        
    }
    
    public void addListener(DataUpdateFinishedListener listener){
        System.out.println("Adding listener: "+listener.getClass());
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
                    fillAccountsAndCharacters();
                    try {
                        Thread.sleep(1000*60*5);//update in every 5 minutes
                    } catch (InterruptedException ex) {
                        System.out.println("IEX: "+ex.getMessage());
                    }
                }
            }
        });
        updateThread.start();
        
    }
    private void fillAccountsAndCharacters(){
        isUpdating = true;
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
