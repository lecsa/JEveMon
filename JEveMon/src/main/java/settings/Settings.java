/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
        oo.writeObject(isDebug);
    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException {
        isDebug = (boolean) oi.readObject();
    }
}
