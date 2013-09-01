/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import API.APIHandler;
import data.Item;
import data.Station;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

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
                if(item.containedItems.isEmpty()){
                    amount = " x"+item.containedItems.size();
                }
                label.setIcon(APIHandler.getTypeIMG(item.id));
                label.setText(item.name+amount);
            } else if( o instanceof Station ) {
                Station station = (Station)o;
                label = new JLabel(APIHandler.getTypeIMG(17366));
                label.setIcon(APIHandler.getTypeIMG(17366));
                label.setText(station.name);
                //17366
            }else{
                label.setIcon(null);
                label.setText("" + value);
            }
            return label;
    }
    
}
