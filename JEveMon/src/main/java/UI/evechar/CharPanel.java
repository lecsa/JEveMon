/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import API.APIHandler;
import data.EVECharacter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

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
        pnData.add(new JLabel(character.name));
        pnData.add(new JLabel(character.corpName));
        this.add(pnData);
    }
    
    private void initLabel(){
        JPanel pnIMG = new JPanel(new BorderLayout());
        pnIMG.add(APIHandler.getCharacterIMG(character.id),BorderLayout.CENTER);
        this.add(pnIMG);
    }
    
}
