/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import API.APIHandler;
import data.EVECharacter;
import data.Skill;
import data.SkillGroup;
import db.DBHandler;
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import utils.Utils;

/**
 *
 * @author lecsa
 */
public class SkillsPanel extends JPanel{
    
    private EVECharacter character;
    private ArrayList<SkillGroup> groups = new ArrayList();
    private JTree tree;
    
    public SkillsPanel(EVECharacter character){
        this.character = character;
        setLayout(new BorderLayout());
        DBHandler db = new DBHandler();
        for(int i=0;i<character.skills.size();i++){
            Skill currentSkill = character.skills.get(i);
            if( isGroupExists(currentSkill.type.groupID) ){
                getGroup(currentSkill.type.groupID).addSkill(currentSkill);
            }else{
                this.groups.add(new SkillGroup(currentSkill.type.groupID,db.getGroupNameByID(currentSkill.type.groupID)));
                getGroup(currentSkill.type.groupID).addSkill(currentSkill);
            }
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Skills");
        int countlvl5=0;
        for(int i=0;i<groups.size();i++){
            DefaultMutableTreeNode currentGroup = new DefaultMutableTreeNode(groups.get(i).name+" - "+Utils.formatLong(groups.get(i).groupSP)+" SP - "+groups.get(i).lvl5+" skills on lvl5");
            for(int n=0;n<groups.get(i).getSkills().size();n++){
                Skill currentSkill = groups.get(i).getSkills().get(n);
                if( currentSkill.skillevel == 4 ){
                    DefaultMutableTreeNode currSkillNode = new DefaultMutableTreeNode("<html><font color=#117777>"+currentSkill+" - lvl "+currentSkill.skillevel+"</font></html>");
                    currentGroup.add(currSkillNode);
                }else if( currentSkill.skillevel == 5 ){
                    DefaultMutableTreeNode currSkillNode = new DefaultMutableTreeNode("<html><font color=#117711>"+currentSkill+" - lvl "+currentSkill.skillevel+"</font></html>");
                    currentGroup.add(currSkillNode);
                }else{
                    DefaultMutableTreeNode currSkillNode = new DefaultMutableTreeNode("<html><font color=#771111>"+currentSkill+" - lvl "+currentSkill.skillevel+"</font></html>");
                    currentGroup.add(currSkillNode);
                }
            }
            countlvl5+=groups.get(i).lvl5;
            top.add(currentGroup);
        }
        top.setUserObject("Skills - "+Utils.formatLong(character.skillpoints)+" - "+countlvl5+" skills on lvl5");
        tree = new JTree(top);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        ImageIcon leaf = APIHandler.getTypeIMG(character.skills.get(0).type.id);
        ImageIcon root = null;
        try{
            root = new ImageIcon(ImageIO.read(new File("img/0_32.png")));
        }catch(Exception ex){
            System.out.println("EX: "+ex.getMessage());
        }
        if( leaf != null ){
            renderer.setLeafIcon(leaf);
            if( root != null ){
                renderer.setClosedIcon(root);
                renderer.setOpenIcon(root);
            }else{
                renderer.setClosedIcon(leaf);
                renderer.setOpenIcon(leaf);
            }
            
        }
        tree.setCellRenderer(renderer);
        JScrollPane treeView = new JScrollPane(tree);
        treeView.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(treeView,BorderLayout.CENTER);
    }
    
    private boolean isGroupExists(int groupID){
    boolean exists = false;
        for(int i=0;i<groups.size() && !exists;i++){
            if(groups.get(i).id == groupID){
                exists = true;
            }
        }
    return exists;
    }
    
    private SkillGroup getGroup(int groupID){
    SkillGroup grp = null;
        for(int i=0;i<groups.size() && grp == null;i++){
            if(groups.get(i).id == groupID){
                grp = groups.get(i);
            }
        }
    return grp;
    }
}
