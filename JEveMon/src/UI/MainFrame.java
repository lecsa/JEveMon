/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import UI.evechar.CharSummeryPanel;
import API.APIHandler;
import API.APIKey;
import data.EVECharacter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;

/**
 *
 * @author lecsa
 */
public class MainFrame extends JFrame implements ActionListener, Runnable{
    private Toolkit tk;
    private final static int DW=1360,DH=960;
    private JMenuBar jmb = new JMenuBar();
    private JMenu menuSettings = new JMenu("Settings");
    private JMenuItem menuItemAPI = new JMenuItem("API keys");
    private APIFrame frAPI = null;
    private ArrayList<APIKey> keys = new ArrayList();
    private ArrayList<EVECharacter> characters = new ArrayList();
    public static boolean isDebug = true;
    public static JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL, 0, 0);
    
    private boolean isRunning = false;
    private boolean inited = false;
    public static NotificationPanel noti = new NotificationPanel();
    
    public MainFrame(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        APIHandler.createdirs();
        setTitle("JEVE");
        setLayout(new BorderLayout());
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
        
        initMenu();
        setVisible(true);
        Thread initThread = new Thread(new Runnable() {

            @Override
            public void run() {
                fillKeys();
                fillChars();
                CharSummeryPanel csp = new CharSummeryPanel(characters);
                
                add(noti,BorderLayout.NORTH);
                add(csp,BorderLayout.CENTER);
                revalidate();
                repaint();
                inited = true;
            }
        });
        initThread.start();
        isRunning = true;
        Thread updater = new Thread(this);
        updater.start();
    }
    
    @Override
    public void run() {
        //update thread
        while( isRunning ){
            try {
                if( inited ){
                    initProgress(characters.size());
                    for(int i=0;i<characters.size();i++){
                        try{
                            APIHandler.fillCharacterData(characters.get(i));
                        }catch(NullPointerException ex){
                        }
                        setProgress(i+1);
                    }
                    finishProgress();
                }
                //TODO: check account training
                Thread.sleep(1000*60*10);//10 min
            } catch (InterruptedException ex) {
                System.out.println("IEX: "+ex.getMessage());
            }
        }
    }
    
    private void fillKeys(){
        File keysdir = new File("apikeys");
        if( keysdir.exists() && keysdir.isDirectory() ){
            File[] list = keysdir.listFiles();
            initProgress(list.length);
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            for( int i=0;i<list.length;i++ ){
                try{
                    fis = new FileInputStream(list[i]);
                    ois = new ObjectInputStream(fis);
                    Object o = ois.readObject();
                    if( o instanceof APIKey ){
                        if( isDebug ) System.out.println("Adding key: "+((APIKey)o).getName());
                        keys.add((APIKey)o);
                        setProgress(i+1);
                    }
                }catch(IOException ex){
                    System.out.println("IOE: "+ex.getMessage());
                }catch(ClassNotFoundException ex){
                    System.out.println("CNFE: "+ex.getMessage());
                }finally{
                    try{
                        ois.close();
                        fis.close();
                        finishProgress();
                    }catch(Exception e){
                    
                    }
                }
            }
        }
    }
    private void fillChars(){
        initProgress(keys.size());
        for(int i=0;i<keys.size();i++){
            
            EVECharacter[] k = APIHandler.getCharacters(keys.get(i));
            
            for(int n=0;n<k.length;n++){
                try{
                    if( isDebug ) System.out.println("Adding character: "+k[n].name);
                    k[n] = APIHandler.fillCharacterData(k[n]);
                    characters.add(k[n]);
                }catch(NullPointerException ex){
                }
            }
            setProgress(i+1);
            
        }
        finishProgress();
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
                frAPI = new APIFrame();
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
        setProgress(0);
    }
    
    public static void setProgress(int val){
        jpb.setValue(val);
        if( jpb.getMaximum() != val ){
            jpb.setString(Integer.toString(val)+" / "+jpb.getMaximum());
        }else{
            jpb.setString("Done");
        }
    }
    public static void finishProgress(){
        setProgress(jpb.getMaximum());
    }
    public static void main(String[] args) {
        new MainFrame();
    }
}
