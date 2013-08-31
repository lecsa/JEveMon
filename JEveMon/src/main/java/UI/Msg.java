package UI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Modális ablakok megjelenítésére használt osztály.
 * @author lecsa
 */
public class Msg {
    
    private static JFrame parent;
    
    public static void setParent(JFrame parent){
        Msg.parent = parent;
    }
    /**
     * Hibaüzenet megjelenítése modális ablakban.
     * @param msg 
     */
    public static void errorMsg(String msg){
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Információ megjelenítése modális ablakban.
     * @param msg 
     */
    public static void infoMsg(String msg){
        JOptionPane.showMessageDialog(parent, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Figyelmeztetés megjelenítése modális ablakban.
     * @param msg 
     */
    public static void warningMsg(String msg){
        JOptionPane.showMessageDialog(parent, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
