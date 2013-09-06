/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import UI.evechar.journal.JournalPanel;
import UI.evechar.asset.AssetListPanel;
import data.character.EVECharacter;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Main character frame.
 * @author lecsa
 */
public class CharFrame extends JFrame implements WindowListener{
    /**
     * for screen size information.
     */
    private Toolkit tk;
    /**
     * default width and height values.
     */
    private final static int DW=1380,DH=768;
    /**
     * \"Parent\" object. The source of the frame creation event (Mouse click).
     */
    private ExtendedCharPanel parent;
    /**
     * Tabbed pane for the subpanels of the main character frame.
     */
    private JTabbedPane tabbedPane = new JTabbedPane();
    
    public CharFrame(ExtendedCharPanel parent){
        this.parent = parent;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle(parent.getCharacter().getName()+" - "+parent.getCharacter().getCorpName());
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
        tabbedPane.addTab("Journal", new JournalPanel(parent.getCharacter()));
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
