/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import UI.Msg;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author lecsa
 */
public class ImageHandler {
    
    private static boolean msgImgServerOffline = true;
    
    public static void createdirs(){
        File f = new File("cache/img/char");
        f.mkdirs();
        f = new File("cache/img/type");
        f.mkdirs();
    }
    private static boolean cache(File f, URL url){
        boolean success = false;
        if( !f.exists() ){//cache
            FileOutputStream fos = null;
            try{
                if(isURLexists(url)){
                    ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                    fos = new FileOutputStream(f);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    success = true;
                }else{
                    System.out.println("Image server unavailable.");
                    if( msgImgServerOffline ){
                        Msg.errorMsg("<html>Image server unreachable.<br />This message won't appear again until you restart the application.</html>");
                        msgImgServerOffline = false;
                    }
                }
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }finally{
                try{
                    fos.close();
                }catch(Exception ex){
                    
                }
            }
        }
        return success;
    }
    
    public static JLabel getCharacterIMGLabel(int characterID){
    File f = new File("cache/img/char/"+Integer.toString(characterID)+"_128.jpg");
    JLabel retval = new JLabel("IMG unavailable");
    if( !f.exists() ){
        try{
            URL url = new URL("http://image.eveonline.com/character/"+Integer.toString(characterID)+"_128.jpg");
            cache(f,url);
        }catch(MalformedURLException ex){
            System.out.println("MAE: "+ex.getMessage());
        }
    }
    if( f.exists() ){
        try{
            BufferedImage img = ImageIO.read(f);
            retval = new JLabel(new ImageIcon(img));

        }catch(IOException ex){
            System.out.println("IOE: "+ex.getMessage());
        }
    }
    return retval;
    }
    
    public static ImageIcon getApplicationIcon() {
        File f = new File("img/logo.png");
        if( f.exists() ){
            try {
                BufferedImage img = ImageIO.read(f);
                return new ImageIcon(img);
            } catch(Exception ex) {
                System.out.println("Can't find Icon File!");
            }
        }
        return new ImageIcon();
    }
    public static ImageIcon getCopyRightImage() {
        File f = new File("img/copyright.jpg");
        if( f.exists() ){
            try {
                BufferedImage img = ImageIO.read(f);
                return new ImageIcon(img);
            } catch(Exception ex) {
                System.out.println("Can't find Icon File!");
            }
        }
        return new ImageIcon();
    }
    public static ImageIcon getImage(String path) {
        File f = new File(path);
        if( f.exists() ){
            try {
                BufferedImage img = ImageIO.read(f);
                return new ImageIcon(img);
            } catch(Exception ex) {
                System.out.println("Can't find Icon File!");
            }
        }
        return new ImageIcon();
    }
    public static ImageIcon getTypeIMG(int typeID){
    File f = new File("cache/img/type/"+Integer.toString(typeID)+"_32.png");
    ImageIcon retval = null;
    if( !f.exists() ){
        try{
            URL url = new URL("http://image.eveonline.com/type/"+Integer.toString(typeID)+"_32.png");
            cache(f,url);
        }catch(MalformedURLException ex){
            System.out.println("MAE: "+ex.getMessage());
        }
        
    }
    if( f.exists() ){
        try{
            BufferedImage img = ImageIO.read(f);
            retval = new ImageIcon(img);

        }catch(IOException ex){
            System.out.println("IOE: "+ex.getMessage());
        }
    }
    return retval;
    }
    
    private static boolean isURLexists(URL url){
        boolean result = false;
            try {
                InputStream input = url.openStream();
                result = true;
            } catch (Exception ex) {
                System.out.println("EX: "+ex.getMessage());
            }
        return result;
    }
}
