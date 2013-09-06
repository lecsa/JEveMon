/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar.asset;

import data.type.Item;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import utils.ImageHandler;

/**
 *
 * @author lecsa
 */
public class ItemTreeElement {
    private Item item;
    private JLabel label;

    public ItemTreeElement(Item item, JLabel label) {
        this.item = item;
        this.label = label;
        updateIcon(ImageHandler.getTypeIMG(1));//not found image
    }

    public Item getItem() {
        return item;
    }

    public JLabel getLabel() {
        return label;
    }
    public void updateIcon(final ImageIcon icon){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                label.setIcon(icon);
                label.revalidate();
                label.repaint();
            }
        });
    }
}
