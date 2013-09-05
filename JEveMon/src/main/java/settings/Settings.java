/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 *
 * @author lecsa
 */
public class Settings implements Externalizable{
    
    /*
     * Static Settings
     */
    public final static String APPNAME = "JEveMon";
    public final static String USERPATH = System.getProperty("user.home") + "/." + APPNAME;
    
    /*
     * Settings
     */
    public transient static boolean isDebug = true;

    @Override
    public void writeExternal(ObjectOutput oo) throws IOException {
        oo.writeObject(Settings.isDebug);
    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException {
        Settings.isDebug = (boolean) oi.readObject();
    }
    
    /** 
     * Saves Settings to fileName
     * @param fileName File Name ( with path ) to save to
     * @return true on success, false on fail
     */
    public static boolean save(String fileName) {
        Settings instance = new Settings();
        if ( Settings.isDebug ) System.out.println("Saving Settings (File Name: " + fileName + ") ...");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream( USERPATH + "/" + fileName ))) {
            oos.writeObject(instance);
        } catch( FileNotFoundException ex ) {
            File dir = new File(USERPATH);
            if (!dir.exists()) {
                if ( Settings.isDebug ) System.out.println("Creating Directory: " + USERPATH );
                boolean result = dir.mkdirs();
                if (result) save(fileName);
                if ( Settings.isDebug ) System.out.println("Failed to create Directory: " + USERPATH );
                return false;
            }
        } catch (Exception ex ) {
            if ( Settings.isDebug ) System.out.println("Error saving Settings: " + ex.getLocalizedMessage());
            return false;                
        }
        return true;
    }
    
    /**
     * Loades Settings from fileName
     * @param fileName File Name ( with path ) to load from
     * @return true on success, false on fail
     */
    public static boolean load(String fileName) {
        if ( Settings.isDebug ) System.out.println("Loading Settings (File Name: " + fileName + ") ...");
        try ( ObjectInputStream ois = new ObjectInputStream(new FileInputStream( USERPATH + "/" + fileName )) ) {
            Settings instance = (Settings) ois.readObject();
        } catch( Exception ex ) {
            if ( Settings.isDebug ) System.out.println("Error loading Settings: " + ex.getLocalizedMessage());
            return false;
        }
        return true;
    }
}
