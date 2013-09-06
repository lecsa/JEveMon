package utils;

import java.io.File;
import settings.Settings;

/**
 * File and Path handling Class for storing Files and Directories in the current users home directory
 * @author bayi
 */
public class FileSystem {
    
    /**
     * The USERPATH is the Path pointing to the current users Home directory
     */
    public final static String USERPATH = System.getProperty("user.home") + "/." + Settings.APPNAME;

    /**
     * Checks and creates - if necessary - application directories
     */
    public static void createDirs() {
        if ( Settings.isDebug ) System.out.println("Checking Application Directories ... ");
        createDir("/cache");
        createDir("/cache/account");
        createDir("/cache/char");
        createDir("/cache/static");
        createDir("/cache/img/char");
        createDir("/cache/img/type");
    }
    
    /**
     * Checks and creates - if necessary - the given directory
     * @param path the directory to check / create
     */
    public static void createDir( String path ) {
        File file = new File(getPath(path));
        if ( !file.exists() ) {
            if ( Settings.isDebug ) System.out.println("Creating Directory: " + getPath(path));
            file.mkdirs();
        }
    }
    
    /**
     * Returns the corrected path to the given relative path, it automatically strips the slashes from the beginning
     * @param path the relative path given to the file or directory
     * @return String with path
     */
    public static String getPath( String path ) {
        path = path.startsWith("/") ? path.substring(1) : path;
        return USERPATH + "/" + path;
    }
    
    /**
     * Returns the complete File object identified by the relative path given.
     * @param path the relative path given to the file or directory
     * @return File object
     */
    public static File getFile( String path ) {
        return new File(getPath(path));
    }
        
}
