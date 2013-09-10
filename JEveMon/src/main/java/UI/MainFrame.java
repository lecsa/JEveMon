/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import UI.evechar.CharSummeryPanel;
import API.APIHandler;
import data.DataProvider;
import data.DataUpdater;
import data.character.EVECharacter;
import event.done.DataUpdateFinishedEvent;
import event.done.DataUpdateFinishedListener;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import settings.Settings;
import utils.FileSystem;
import utils.ImageHandler;
/*
 * TODO: 
 * - Account expires DoB helyett
 * - SP / hour
 * - Skill queue gui
 * - Comparable when we want to compare Ship to Item
 * - Modal msg after the 5min update cycle is annoying
 * - Notification class + notification settings. Unique id generation for serializable notifications.
 * - Account expires notification
 * - separated station db
 */
/**
 *
 * @author lecsa
 */
public class MainFrame extends JFrame implements ActionListener, DataUpdateFinishedListener{
    /**
     * for screen size information.
     */
    private Toolkit tk;
    /**
     * default width and height values.
     */
    private final static int DW=1360,DH=960;
    /**
     * menu bar.
     */
    private JMenuBar jmb = new JMenuBar();
    /**
     * Settings menu.
     */
    private JMenu menuSettings = new JMenu("Settings");
    /**
     * API keys menuitem in the settings menu.
     */
    private JMenuItem menuItemAPI = new JMenuItem("API keys");
    /**
     * API frame.
     */
    private APIFrame frAPI = null;
    /**
     * Notification panel.
     */
    public static NotificationPanel noti = new NotificationPanel();
    /**
     * Character summery panel. Shows genereal informations.
     */
    private CharSummeryPanel csp;
    /**
     * Main data updater class.
     */
    private static DataUpdater updater;
    public static DataProvider dp;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public MainFrame(){
        if ( !Settings.load("settings.conf") ) Settings.save("settings.conf");
        if ( Settings.isDebug ) {
            System.out.println("App Name: " + Settings.APPNAME);
            System.out.println("User Home Path: " + FileSystem.USERPATH);
        }
        updater = new DataUpdater();
        updater.addListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        APIHandler.createdirs();
        ImageHandler.createdirs();
        setTitle(Settings.APPNAME);
        setLayout(new BorderLayout());
        // Set Application Icon
        setIconImage(ImageHandler.getApplicationIcon().getImage());
        tk=getToolkit();
        if( tk.getScreenSize().width >= DW && tk.getScreenSize().height >= DH ){
            setBounds((tk.getScreenSize().width-DW)/4, (tk.getScreenSize().height-DH)/2, DW, DH);
        }else{
            setBounds(5, 5, DW, DH);
        }
        add(new CCPPanel(), BorderLayout.SOUTH);
        add(noti,BorderLayout.NORTH);
        initMenu();
        setVisible(true);
        updater.start();
    }
    /**
     * Data update finished event handling. Updates character panels.
     * @param e 
     */
    @Override
    public void dataUpdateFinishedEvent(DataUpdateFinishedEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<EVECharacter> allCharacters = new ArrayList();
                dp = updater.getDataProvider();
                for(int i=0;i<dp.getAccounts().size();i++){
                    for(int n=0;n<dp.getAccounts().get(i).getCharacters().size();n++){
                        if( dp.getAccounts().get(i).getCharacters().get(n) != null ){
                            allCharacters.add(dp.getAccounts().get(i).getCharacters().get(n));
                            if( Settings.isDebug ) System.out.println("character added: "+dp.getAccounts().get(i).getCharacters().get(n).getName());
                        }
                    }
                }
                if( csp != null ){
                    remove(csp);
                }
                csp = new CharSummeryPanel(allCharacters);
                add(csp,BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });
    }
    /**
     * Init menu.
     */
    private void initMenu(){
        menuSettings.add(menuItemAPI);
        jmb.add(menuSettings);
        setJMenuBar(jmb);
        menuItemAPI.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(menuItemAPI)){
            if(frAPI == null){
                frAPI = new APIFrame(updater);
                setVisible(true);
            }else{
                if(frAPI.isVisible()){
                    frAPI.setVisible(false);
                }else{
                    frAPI.setVisible(true);
                }
            }
        }
    }
    public static void main(String[] args) {
        new MainFrame();
    }
}
