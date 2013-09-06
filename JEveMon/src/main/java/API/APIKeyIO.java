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
import utils.FileSystem;

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
            File dircheck = FileSystem.getFile(API_DIR);
            if( !dircheck.exists() ) FileSystem.createDir(API_DIR);
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(key);
            
        }catch(IOException ex){
            retval = false;
            System.out.println(ex.getMessage());
        }finally{
            try{
            if ( fos != null ) fos.close();
            if ( oos != null ) oos.close();
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
            if ( fis != null ) fis.close();
            if ( ois != null ) ois.close();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    return retval;
    }
}
