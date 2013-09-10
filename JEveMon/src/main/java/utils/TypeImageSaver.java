/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import settings.Settings;

/**
 *
 * @author lecsa
 */
public class TypeImageSaver implements Runnable{
    
    private static ArrayList<Integer> locked = new ArrayList();
    private static final int MAX_DOWNLOAD_THREADS = 8;
    private static int currentDownLoads = 0;
    private static final int MAX_ATTEMPTS = 600;
    private int attempts = 0;
    private int typeID;
    
    public TypeImageSaver(int typeID){
        this.typeID = typeID;
    }
    
    @Override
    public void run() {
        ImageIcon icon = null;
        if(!isLocked(this.typeID)){
            File f = new File(FileSystem.getPath("cache/img/type/"+typeID+"_32.png"));
            if(!f.exists()){
                try{
                while(icon == null && attempts < MAX_ATTEMPTS){
                    if(currentDownLoads < MAX_DOWNLOAD_THREADS){
                        currentDownLoads++;
                        //System.out.println(typeID+" - downloading");
                        lock();
                        icon = ImageHandler.getTypeIMG(typeID);
                        unlock();
                        currentDownLoads--;
                    }else{
                        attempts++;
                        //System.out.println(typeID+" - attempts: "+attempts);
                        Thread.sleep(1500+(int)(Math.random()*3000));
                    }

                }
                }catch(InterruptedException ex){
                    if(Settings.isDebug) System.out.println("IEX: "+ex.getMessage());
                }
            }
        }
    }
    private void lock(){
        locked.add(typeID);
    }
    
    private void unlock(){
        locked.remove(new Integer(typeID));
    }
    
    public static boolean isLocked(int typeID){
        boolean isLocked = false;
            for(int i=0;i<locked.size() && !isLocked;i++){
                if( locked.get(i) != null ){
                    if( locked.get(i).intValue() == typeID ){
                        isLocked = true;
                    }
                }
            }
        return isLocked;
    }
}
