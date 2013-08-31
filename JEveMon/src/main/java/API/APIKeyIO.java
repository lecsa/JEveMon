/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author lecsa
 */
public class APIKeyIO {
    
    public static final String API_DIR = "apikeys";
    public static final String EXTENSION = ".key";
    
    public static boolean saveAPIKey(APIKey key, File f){
        boolean retval = true;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            File dircheck = new File(API_DIR);
            if( !dircheck.exists() ){
                dircheck.mkdirs();
            }
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(key);
            
        }catch(IOException ex){
            retval = false;
            System.out.println(ex.getMessage());
        }finally{
            try{
            fos.close();
            oos.close();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return retval;
    }
    
    public static APIKey loadAPIKey(File f){
    APIKey retval = null;
    FileInputStream fis = null;    
    ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            if(o instanceof APIKey){
                retval = (APIKey)o;
            }
        }catch(IOException ex){
            retval = null;
            System.out.println(ex.getMessage());
        }catch(Exception ex){
            retval = null;
            System.out.println(ex.getMessage());
        }finally{
            try{
            fis.close();
            ois.close();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    return retval;
    }
}
