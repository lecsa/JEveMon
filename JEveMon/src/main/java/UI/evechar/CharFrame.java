/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import API.APIHandler;
import UI.APIFrame;
import data.EVECharacter;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author lecsa
 */
public class CharFrame extends JFrame implements WindowListener{
    private Toolkit tk;
    private final static int DW=1024,DH=768;
    private ExtendedCharPanel parent;
    private JTabbedPane tabbedPane = new JTabbedPane();
    
    public CharFrame(ExtendedCharPanel parent){
        this.parent = parent;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle(parent.getCharacter().name+" - "+parent.getCharacter().corpName);
        tk=getToolkit();
        if( tk.getScreenSize().width >= DW && tk.getScreenSize().height >= DH ){
            setBounds((tk.getScreenSize().width-DW)/4, (tk.getScreenSize().height-DH)/2, DW, DH);
        }else{
            setBounds(5, 5, DW, DH);
        }
        addWindowListener(this);
        setVisible(true);
        SkillsPanel s = new SkillsPanel(parent.getCharacter());
        tabbedPane.addTab("Skills", s);
        tabbedPane.addTab("Assets", new AssetListPanel(parent.getCharacter()));
        add(tabbedPane);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        parent.changeFrameVisibility(false);
        dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }
    public EVECharacter getCharacter(){
        return this.parent.getCharacter();
    }
}
