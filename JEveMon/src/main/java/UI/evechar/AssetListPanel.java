/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import API.APIHandler;
import data.character.EVECharacter;
import data.type.Item;
import data.location.Station;
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
//                    System.out.println(character.assets.size());
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
        for(int i=0;i<character.assets.size();i++){
            //stations
            Station sta = character.assets.get(i);
            DefaultMutableTreeNode stationNode = new DefaultMutableTreeNode(sta);
            for(int n=0;n<sta.items.size();n++){
                Item parent = sta.items.get(n);
                DefaultMutableTreeNode parentItemNode;
                if(parent.containedItems.isEmpty()){
                    parentItemNode = new DefaultMutableTreeNode(parent);
                }else{
                    parentItemNode = new DefaultMutableTreeNode(parent);
                    for(int k=0;k<parent.containedItems.size();k++){
                        Item child = parent.containedItems.get(k);
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
