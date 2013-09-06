/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar.asset;

import API.APIHandler;
import data.character.EVECharacter;
import data.type.Item;
import data.location.Station;
import data.type.Ship;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import settings.Settings;

/**
 *
 * @author lecsa
 */
public class AssetListPanel extends JPanel implements ActionListener{
    private JTree tree;
    private JButton btFetch = new JButton("Get asset data");
    private EVECharacter character;
    private JScrollPane treeView;
    private ConcurrentLinkedQueue<ItemTreeElement> itemRowsToUpdate = new ConcurrentLinkedQueue();
    
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
                DefaultMutableTreeNode parentItemNode;
                Ship parentShip = sta.getShips().get(n);
                ItemTreeElement parentShipRow = new ItemTreeElement(parentShip, new JLabel());
                itemRowsToUpdate.offer(parentShipRow);
                if( parentShip.getContainedItems().isEmpty() &&
                    parentShip.getDroneBay().isEmpty() &&
                    parentShip.getFittedItems().isEmpty() &&
                    parentShip.getCargoHold().isEmpty()){
                    parentItemNode = new DefaultMutableTreeNode(parentShipRow);
                }else{
                    DefaultMutableTreeNode fittedNode = new DefaultMutableTreeNode("fitted");
                    DefaultMutableTreeNode droneBayNode = new DefaultMutableTreeNode("drone bay");
                    DefaultMutableTreeNode cargoNode = new DefaultMutableTreeNode("cargo");
                    DefaultMutableTreeNode otherNode = new DefaultMutableTreeNode("other");
                    for(int k=0;k<parentShip.getFittedItems().size();k++){//fitted items
                        Item child = parentShip.getFittedItems().get(k);
                        ItemTreeElement childRow = new ItemTreeElement(child, new JLabel());
                        itemRowsToUpdate.offer(childRow);
                        fittedNode.add(new DefaultMutableTreeNode(childRow));
                    }
                    for(int k=0;k<parentShip.getDroneBay().size();k++){//fitted items
                        Item child = parentShip.getDroneBay().get(k);
                        ItemTreeElement childRow = new ItemTreeElement(child, new JLabel());
                        itemRowsToUpdate.offer(childRow);
                        droneBayNode.add(new DefaultMutableTreeNode(childRow));
                    }
                    for(int k=0;k<parentShip.getCargoHold().size();k++){//fitted items
                        Item child = parentShip.getCargoHold().get(k);
                        ItemTreeElement childRow = new ItemTreeElement(child, new JLabel());
                        itemRowsToUpdate.offer(childRow);
                        cargoNode.add(new DefaultMutableTreeNode(childRow));
                    }
                    for(int k=0;k<parentShip.getContainedItems().size();k++){//fitted items
                        Item child = parentShip.getContainedItems().get(k);
                        ItemTreeElement childRow = new ItemTreeElement(child, new JLabel());
                        itemRowsToUpdate.offer(childRow);         
                        otherNode.add(new DefaultMutableTreeNode(childRow));
                    }
                    parentItemNode = new DefaultMutableTreeNode(parentShipRow);
                    parentItemNode.add(fittedNode);
                    parentItemNode.add(droneBayNode);
                    parentItemNode.add(cargoNode);
                    parentItemNode.add(otherNode);
                }
                stationNode.add(parentItemNode);
            }
            for(int n=0;n<sta.getItems().size();n++){
                Item parent = sta.getItems().get(n);
                ItemTreeElement parentRow = new ItemTreeElement(parent,new JLabel());
                itemRowsToUpdate.offer(parentRow);         
                DefaultMutableTreeNode parentItemNode;
                if(parent.getContainedItems().isEmpty()){
                    parentItemNode = new DefaultMutableTreeNode(parentRow);
                }else{
                    parentItemNode = new DefaultMutableTreeNode(parentRow);
                    for(int k=0;k<parent.getContainedItems().size();k++){
                        Item child = parent.getContainedItems().get(k);
                        ItemTreeElement childRow = new ItemTreeElement(child, new JLabel());
                        itemRowsToUpdate.offer(childRow);         
                        parentItemNode.add(new DefaultMutableTreeNode(childRow));
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
        //download images..
        ItemTreeElement currentRow = null;
        while((currentRow=itemRowsToUpdate.poll())!=null){
            if( currentRow != null ){
                try{
                    ExecutorService exe = Executors.newFixedThreadPool(1);
                    Callable<Void> command = new TypeImageDownloader(currentRow);
                    Future<Void> done = exe.submit(command);
                }catch(Exception ex){
                    if( Settings.isDebug ) System.out.println("Error while downloading type image (typeID="+currentRow.getItem().getId()+"): "+ex.getMessage());
                }
            }
        }
    }
    
}
