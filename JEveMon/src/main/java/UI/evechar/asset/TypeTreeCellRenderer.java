/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar.asset;

import API.AssetListFlags;
import data.type.Item;
import data.location.Station;
import java.awt.Component;
import java.net.MalformedURLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import settings.Settings;
import utils.FileSystem;
import utils.ImageHandler;
import utils.TypeImageSaver;

/**
 * Tree cell renderer for items.
 * @author lecsa
 */
public class TypeTreeCellRenderer implements TreeCellRenderer{

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        JLabel label = new JLabel();
            if(o instanceof Item){
                    String amount = "";
                    Item item = (Item)o;
                    if( item.getContainedItems().isEmpty() && item.getQuantity() != 0 ){
                        amount = " x"+item.getQuantity();
                    }
                    String flagStr = "";
                    if(AssetListFlags.Flag_t.isFlagExists(item.getFlag())){
                        if( item.isFitted() ){
                            flagStr = " ("+AssetListFlags.Flag_t.getFlagName(item.getFlag())+")";
                        }
                    }
                    ImageIcon icon = null;
                    if(!TypeImageSaver.isLocked(item.getId())){
                        icon = ImageHandler.getImage(FileSystem.getPath("cache/img/type/"+item.getId()+"_32.png"));
                    }else{
                        icon = ImageHandler.getImage(FileSystem.getPath("cache/img/type/1_32.png"));
                    }
                    if(icon == null){
                        icon = ImageHandler.getImage(FileSystem.getPath("cache/img/type/1_32.png"));
                    }
                    label.setText(item.getName()+amount+flagStr);
                    if(icon != null){
                        label.setIcon(icon);
                    }
            } else if( o instanceof Station ) {
                Station station = (Station)o;
                label = new JLabel(ImageHandler.getTypeIMG(17366));
                label.setIcon(ImageHandler.getTypeIMG(17366));
                label.setText(station.getName());
                //17366
            }else if(o instanceof String){
                String theString = (String)o;
                ImageIcon icon;
                if( theString.toLowerCase().equals("fitted") ){
                    icon = ImageHandler.getImage("img/fitted.png");
                }else if( theString.toLowerCase().equals("drone bay")){
                    icon = ImageHandler.getImage("img/drone.png");
                }else{
                    icon = ImageHandler.getImage("img/cargo.png");
                }
                if( icon!=null ){
                    label.setIcon(icon);
                }
                label.setText(theString);
            }else{
                label.setIcon(null);
                label.setText("" + value);
            }
            return label;
    }
    
}
