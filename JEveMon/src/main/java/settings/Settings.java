/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import java.io.Externalizable;
import java.io.FileInputStream;
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
        if ( Settings.isDebug ) System.out.println("Saving Settings ... (File Name: " + fileName + ")");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream( fileName ))) {
            oos.writeObject(instance);
        } catch (Exception ex ) {
            if ( Settings.isDebug ) System.out.println("Error saving Settings: ");
            if ( Settings.isDebug ) ex.printStackTrace(System.out);
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
        try ( ObjectInputStream ois = new ObjectInputStream(new FileInputStream( fileName )) ) {
            Settings instance = (Settings) ois.readObject();
        } catch( Exception ex ) {
            if ( Settings.isDebug ) System.out.println("Error loading Settings: ");
            if ( Settings.isDebug ) ex.printStackTrace(System.out);
            return false;
        }
        return true;
    }
}
