/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import UI.evechar.CharSummeryPanel;
import API.APIHandler;
import data.DataUpdater;
import data.character.EVECharacter;
import event.done.DataUpdateFinishedEvent;
import event.done.DataUpdateFinishedListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import settings.Settings;
import utils.ImageHandler;
/*
 * TODO: 
 * - Account expires DoB helyett
 * - SP / hour
 * - Skill queue gui
 * 
 * 
 * 
 */
/**
 *
 * @author lecsa
 */
public class MainFrame extends JFrame implements ActionListener, DataUpdateFinishedListener{
    private Toolkit tk;
    private final static int DW=1360,DH=960;
    private JMenuBar jmb = new JMenuBar();
    private JMenu menuSettings = new JMenu("Settings");
    private JMenuItem menuItemAPI = new JMenuItem("API keys");
    private APIFrame frAPI = null;
    public static boolean isDebug = true;
    public static volatile JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL, 0, 0);
    public static Settings settings = new Settings();
    public static NotificationPanel noti = new NotificationPanel();
    private CharSummeryPanel csp;
    private DataUpdater updater;
    
    public MainFrame(){
        updater = new DataUpdater();
        updater.addListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        APIHandler.createdirs();
        ImageHandler.createdirs();
        setTitle("JEveMon");
        setLayout(new BorderLayout());
        // Set Application Icon
        setIconImage(ImageHandler.getApplicationIcon().getImage());
        tk=getToolkit();
        if( tk.getScreenSize().width >= DW && tk.getScreenSize().height >= DH ){
            setBounds((tk.getScreenSize().width-DW)/4, (tk.getScreenSize().height-DH)/2, DW, DH);
        }else{
            setBounds(5, 5, DW, DH);
        }
        jpb.setBackground(Color.WHITE);
        
        jpb.setForeground(new Color(50,70,50));
        jpb.setStringPainted(true);
        add(jpb, BorderLayout.SOUTH);
        add(noti,BorderLayout.NORTH);
        initMenu();
        setVisible(true);
        updater.start();
    }

    @Override
    public void dataUpdateFinishedEvent(DataUpdateFinishedEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<EVECharacter> allCharacters = new ArrayList();
                for(int i=0;i<updater.getDp().getAccounts().size();i++){
                    for(int n=0;n<updater.getDp().getAccounts().get(i).getCharacters().size();n++){
                        if( updater.getDp().getAccounts().get(i).getCharacters().get(n) != null ){
                            allCharacters.add(updater.getDp().getAccounts().get(i).getCharacters().get(n));
                            if( MainFrame.settings.isDebug ) System.out.println("character added: "+updater.getDp().getAccounts().get(i).getCharacters().get(n).getName());
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
    
    public static void initProgress(int max){
        jpb.setMaximum(max);
        setProgressBar(0);
    }
    
    public static void setProgressBar(int val){
        jpb.setValue(val);
        if( jpb.getMaximum() != val ){
            jpb.setString(Integer.toString(val)+" / "+jpb.getMaximum());
        }else{
            jpb.setString("Done");
        }
    }
    public static void finishProgress(){
        setProgressBar(jpb.getMaximum());
    }
    public static void main(String[] args) {
        new MainFrame();
    }
}
