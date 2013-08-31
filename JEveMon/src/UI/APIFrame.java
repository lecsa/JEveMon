/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import UI.evechar.CharPanel;
import API.APIHandler;
import API.APIKey;
import API.APIKeyIO;
import data.EVECharacter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author lecsa
 */
public class APIFrame extends JFrame implements ActionListener{
    private final int DW = 700, DH = 680;
    private JButton btOpen = new JButton("Open");
    private JButton btSave = new JButton("Save");
    private JButton btDelete = new JButton("Delete");
    private JButton btFetch = new JButton("Fetch characters");
    private JTextField tfName = new JTextField();
    private JTextField tfKeyID = new JTextField();
    private JTextField tfVCode = new JTextField();
    private final JPanel pnChars = new JPanel(new GridLayout(3, 1, 5, 5));
    
    public APIFrame(){
        APIHandler.createdirs();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setBounds(100, 100, DW, DH);
        setTitle("API management");
        initAPIPanels();
        initCharPanels();
        btSave.addActionListener(this);
        btOpen.addActionListener(this);
        btDelete.addActionListener(this);
        setVisible(true);
    }
    
    private void initAPIPanels(){
        JPanel pnMain = new JPanel(new GridLayout(4, 1,5,5));
        pnMain.setBorder(BorderFactory.createTitledBorder("API settings"));
        JPanel pnButtons = new JPanel(new FlowLayout());
        JPanel pnName = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pnKeyID = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pnVCode = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnButtons.add(btSave);
        pnButtons.add(btOpen);
        pnButtons.add(btDelete);
        
        tfName.setPreferredSize(new Dimension(600, 20));
        tfKeyID.setPreferredSize(new Dimension(600, 20));
        tfVCode.setPreferredSize(new Dimension(600, 20));
        
        pnName.add(tfName);
        pnName.add(new JLabel("name"));
        pnKeyID.add(tfKeyID);
        pnKeyID.add(new JLabel("keyID"));
        pnVCode.add(tfVCode);
        pnVCode.add(new JLabel("vCode"));
        
        pnMain.add(pnName);
        pnMain.add(pnKeyID);
        pnMain.add(pnVCode);
        pnMain.add(pnButtons);
        this.add(pnMain,BorderLayout.NORTH);
    }
    private void initCharPanels(){
        JPanel pnMain = new JPanel(new BorderLayout());
        
        JPanel btPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btPanel.add(btFetch);
        btFetch.addActionListener(this);
        pnMain.add(btPanel,BorderLayout.NORTH);
        pnMain.add(pnChars,BorderLayout.CENTER);
        this.add(pnMain,BorderLayout.CENTER);
        
    }
    private void save(){
        String name = tfName.getText().trim();
        String keyID = tfKeyID.getText().trim();
        String vCode = tfVCode.getText().trim();
        if(name.equals("") || keyID.equals("") || vCode.equals("")){
            Msg.errorMsg("You need to fill every fields.");
        }else{
            File f = new File(APIKeyIO.API_DIR+"/"+name+APIKeyIO.EXTENSION);
            boolean save = true;
            if( f.exists() ){
                int answer = JOptionPane.showConfirmDialog(this, "File ("+f.getName()+") already exists. Would you like to overwrite?", "File exists", JOptionPane.YES_NO_OPTION);
                if(answer == JOptionPane.YES_OPTION){
                    f.delete();
                }else{
                    save = false;
                }
            }
            if(save){
                APIKey savable = new APIKey(name, keyID, vCode);
                boolean retval = APIKeyIO.saveAPIKey(savable, f);
                if(retval){
                    Msg.infoMsg("File saved successfully.");
                }else{
                    Msg.errorMsg("Error while saving file.");
                }
            }
        }
    }
    
    private void open(){
        JFileChooser jfc = new JFileChooser(new File("apikeys"));
        FileFilter ff = new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(APIKeyIO.EXTENSION);
            }

            @Override
            public String getDescription() {
                return "API key ( "+APIKeyIO.EXTENSION+" file)";
            }
        };
        jfc.addChoosableFileFilter( ff );
        jfc.setMultiSelectionEnabled(false);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setFileFilter(ff);
        int answer = jfc.showOpenDialog(this);
        if(answer == JFileChooser.APPROVE_OPTION){
            File f = jfc.getSelectedFile();
            APIKey key = APIKeyIO.loadAPIKey(f);
            this.tfName.setText(key.getName());
            this.tfKeyID.setText(key.getKeyID());
            this.tfVCode.setText(key.getvCode());
        }
    
    }
    
    private void delete(){
        JFileChooser jfc = new JFileChooser(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()));
        FileFilter ff = new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(APIKeyIO.EXTENSION);
            }

            @Override
            public String getDescription() {
                return "API key ( "+APIKeyIO.EXTENSION+" file)";
            }
        };
        jfc.addChoosableFileFilter( ff );
        jfc.setMultiSelectionEnabled(false);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setFileFilter(ff);
        int answer = jfc.showOpenDialog(this);
        if(answer == JFileChooser.APPROVE_OPTION){
            File f = jfc.getSelectedFile();
            int confirm = JOptionPane.showConfirmDialog(this, "You are about to delete the following file: "+f.getName()+". \nAre you sure?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION){
                f.delete();
                Msg.infoMsg("File ("+f.getName()+") was deleted successfully.");
            }
        }
    }
    
    private void fetch(){
        final String name = tfName.getText().trim();
        final String keyID = tfKeyID.getText().trim();
        final String vCode = tfVCode.getText().trim();
        if(name.equals("") || keyID.equals("") || vCode.equals("")){
            Msg.errorMsg("You need to fill every fields.");
        }else{
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    EVECharacter[] chars = APIHandler.getCharacters(new APIKey(name, keyID, vCode));
                    pnChars.removeAll();
                    for(int i=0;i<chars.length;i++){
                        if(chars[i]!=null){
                            pnChars.add(new CharPanel(chars[i]));
                        }
                    }
                    pnChars.revalidate();
                }
            });
            t.start();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btSave)){
            save();
        }else if(e.getSource().equals(btOpen)){
            open();
        }else if(e.getSource().equals(btDelete)){
            delete();
        }else if(e.getSource().equals(btFetch)){
            fetch();
        }
    }
    
    public static void main(String[] args) {
        new APIFrame();
    }
}
