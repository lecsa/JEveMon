/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import API.APIHandler;
import data.character.EVECharacter;
import data.type.Item;
import data.location.Station;
import data.type.Ship;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author lecsa
 */
public class AssetListPanel extends JPanel implements ActionListener{
    private JTree tree;
    private JButton btFetch = new JButton("Get asset data");
    private EVECharacter character;
    private JScrollPane treeView;
    public AssetListPanel(EVECharacter c){
        this.character = c;
        setLayout(new BorderLayout());
        JPanel flow = new JPanel(new FlowLayout());
        flow.add(btFetch);
        btFetch.addActionListener(this);
        add(flow,BorderLayout.NORTH);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Assets");
        tree = new JTree(top);
        treeView = new JScrollPane(tree);
        treeView.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(treeView,BorderLayout.CENTER);
        updateAssetList();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btFetch)){
            Thread filler = new Thread(new Runnable() {

                @Override
                public void run() {
                    btFetch.setEnabled(false);
                    APIHandler.fillCharacterAssets(character);
//                    System.out.println(character.getAssets().size());
                    updateAssetList();
                    btFetch.setEnabled(true);
                }
            });
            filler.start();
        }
        
    }
    
    private void updateAssetList(){
        this.remove(treeView);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Assets");
        for(int i=0;i<character.getAssets().size();i++){
            //stations
            Station sta = character.getAssets().get(i);
            DefaultMutableTreeNode stationNode = new DefaultMutableTreeNode(sta);
            for(int n=0;n<sta.getShips().size();n++){
                Ship parent = sta.getShips().get(n);
                DefaultMutableTreeNode parentItemNode;
                Ship parentShip = (Ship)parent;
                if( parentShip.getContainedItems().isEmpty() &&
                    parentShip.getDroneBay().isEmpty() &&
                    parentShip.getFittedItems().isEmpty() &&
                    parentShip.getCargoHold().isEmpty()){
                    parentItemNode = new DefaultMutableTreeNode(parentShip);
                }else{
                    DefaultMutableTreeNode fittedNode = new DefaultMutableTreeNode("fitted");
                    DefaultMutableTreeNode droneBayNode = new DefaultMutableTreeNode("drone bay");
                    DefaultMutableTreeNode cargoNode = new DefaultMutableTreeNode("cargo");
                    DefaultMutableTreeNode otherNode = new DefaultMutableTreeNode("other");
                    for(int k=0;k<parentShip.getFittedItems().size();k++){//fitted items
                        Item child = parentShip.getFittedItems().get(k);
                        fittedNode.add(new DefaultMutableTreeNode(child));
                    }
                    for(int k=0;k<parentShip.getDroneBay().size();k++){//fitted items
                        Item child = parentShip.getDroneBay().get(k);
                        droneBayNode.add(new DefaultMutableTreeNode(child));
                    }
                    for(int k=0;k<parentShip.getCargoHold().size();k++){//fitted items
                        Item child = parentShip.getCargoHold().get(k);
                        cargoNode.add(new DefaultMutableTreeNode(child));
                    }
                    for(int k=0;k<parentShip.getContainedItems().size();k++){//fitted items
                        Item child = parentShip.getContainedItems().get(k);
                        otherNode.add(new DefaultMutableTreeNode(child));
                    }
                    parentItemNode = new DefaultMutableTreeNode(parentShip);
                    parentItemNode.add(fittedNode);
                    parentItemNode.add(droneBayNode);
                    parentItemNode.add(cargoNode);
                    parentItemNode.add(otherNode);
                }
                stationNode.add(parentItemNode);
            }
            for(int n=0;n<sta.getItems().size();n++){
                Item parent = sta.getItems().get(n);
                DefaultMutableTreeNode parentItemNode;
                if(parent.getContainedItems().isEmpty()){
                    parentItemNode = new DefaultMutableTreeNode(parent);
                }else{
                    parentItemNode = new DefaultMutableTreeNode(parent);
                    for(int k=0;k<parent.getContainedItems().size();k++){
                        Item child = parent.getContainedItems().get(k);
                        parentItemNode.add(new DefaultMutableTreeNode(child));
                    }
                }
                stationNode.add(parentItemNode);
            }
            top.add(stationNode);
        }
        tree = new JTree(top);
        TypeTreeCellRenderer renderer = new TypeTreeCellRenderer();
        tree.setCellRenderer(renderer);
        treeView = new JScrollPane(tree);
        treeView.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(treeView,BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
}
