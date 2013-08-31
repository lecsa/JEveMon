/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import data.EVECharacter;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 *
 * @author lecsa
 */
public class CharFrame extends JFrame implements WindowListener{
    private Toolkit tk;
    private final static int DW=1024,DH=768;
    private EVECharacter character;
    private ExtendedCharPanel parent;
    
    public CharFrame(ExtendedCharPanel parent){
        this.parent = parent;
        this.character = parent.getCharacter();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle(character.name+" - "+character.corpName);
        tk=getToolkit();
        if( tk.getScreenSize().width >= DW && tk.getScreenSize().height >= DH ){
            setBounds((tk.getScreenSize().width-DW)/4, (tk.getScreenSize().height-DH)/2, DW, DH);
        }else{
            setBounds(5, 5, DW, DH);
        }
        addWindowListener(this);
        setVisible(true);
        SkillsPanel s = new SkillsPanel(character);
        add(s);
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
    
}
