/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import data.character.EVECharacter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import utils.ImageHandler;

/**
 *
 * @author lecsa
 */
public class CharPanel extends JPanel{
    private EVECharacter character;
    
    public CharPanel(EVECharacter character){
        this.character = character;
        setLayout(new GridLayout(1,2,5,5));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        setPreferredSize(new Dimension(600, 100));
        initLabel();
        initData();
        
    }
    
    private void initData(){
        JPanel pnData = new JPanel(new GridLayout(2, 1,10,10));
        pnData.add(new JLabel(character.getName()));
        pnData.add(new JLabel(character.getCorpName()));
        this.add(pnData);
    }
    
    private void initLabel(){
        JPanel pnIMG = new JPanel(new BorderLayout());
        pnIMG.add(ImageHandler.getCharacterIMGLabel(character.getId()),BorderLayout.CENTER);
        this.add(pnIMG);
    }
    
}
