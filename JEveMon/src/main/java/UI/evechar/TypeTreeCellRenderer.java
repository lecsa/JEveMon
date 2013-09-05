/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import data.type.Item;
import data.location.Station;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import utils.ImageHandler;

/**
 *
 * @author lecsa
 */
public class TypeTreeCellRenderer implements TreeCellRenderer{

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        JLabel label = new JLabel();
            if (o instanceof Item) {
                String amount = "";
                Item item = (Item)o;
                if( item.containedItems.isEmpty() && item.quantity != 0 ){
                    amount = " x"+item.quantity;
                }
                label.setIcon(ImageHandler.getTypeIMG(item.id));
                label.setText(item.name+amount);
            } else if( o instanceof Station ) {
                Station station = (Station)o;
                label = new JLabel(ImageHandler.getTypeIMG(17366));
                label.setIcon(ImageHandler.getTypeIMG(17366));
                label.setText(station.name);
                //17366
            }else{
                label.setIcon(null);
                label.setText("" + value);
            }
            return label;
    }
    
}
