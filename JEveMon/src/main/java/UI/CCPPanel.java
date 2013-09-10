/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utils.ImageHandler;

/**
 *
 * @author lecsa
 */
public class CCPPanel extends JPanel{
    private final static String CCP_COPYRIGHT = "EVE Online, the EVE logo, "
            + "EVE and all associated logos and designs are the intellectual property of CCP hf. "
            + "All artwork, screenshots, characters, vehicles, storylines, "
            + "world facts or other recognizable features of the intellectual property relating "
            + "to these trademarks are likewise the intellectual property of CCP hf. "
            + "EVE Online and the EVE logo are the registered trademarks of CCP hf. "
            + "All rights are reserved worldwide. "
            + "All other trademarks are the property of their respective owners. "
            + "CCP is in no way responsible for the content on or functioning of this application, "
            + "nor can it be liable for any damage arising from the use of this application.";
    
    public CCPPanel(){
        setLayout(new BorderLayout());
        JLabel copy = new JLabel();
        ImageIcon copyicon = ImageHandler.getCopyRightImage();
        
        
        if( copyicon != null ){
            copy.setIcon(ImageHandler.getCopyRightImage());
            copy.setText("<html><p>"+CCP_COPYRIGHT+"</p></html>");
        }else{
            copy.setText("<html><p>© CCP<br />"+CCP_COPYRIGHT+"</p></html>");
        }
        add(copy,BorderLayout.CENTER);
    }
}
