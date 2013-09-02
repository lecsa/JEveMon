/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import UI.MainFrame;
import UI.Msg;
import data.EVECharacter;
import data.SkillInTraining;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utils.ImageHandler;
import utils.Utils;

/**
 *
 * @author lecsa
 */
public class ExtendedCharPanel extends JPanel implements Runnable,MouseListener{
    private EVECharacter character;
    private final static Color RED = new Color(150,50,50);
    private final static Color GREEN = new Color(50,150,50);
    private final static Color LIGHT_RED = new Color(250,200,200);
    private final static Color LIGHT_GREEN = new Color(200,250,200);
    public static boolean isRunning = false;
    private JLabel lbFinishes = new JLabel();
    private JLabel lbTaining = new JLabel();
    private JLabel lbQueue = new JLabel();
    private JLabel lbSP = new JLabel();
    private JLabel lbISK = new JLabel();
    private final int UPDATE_RATE = 1000;//ms
    private JPanel gridPanel;
    private int lowQueueCount = 0;
    private int lowCloneCount = 0;
    private final int NOTIFICATION_IN_SEC = 3600;
    private CharFrame charFrame = null;
    boolean charFrameVisible = false;
    
    
    public ExtendedCharPanel(EVECharacter character){
        this.character = character;
        setLayout(new BorderLayout());
        JLabel img = ImageHandler.getCharacterIMGLabel(character.id);
        img.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(img,BorderLayout.WEST);
        initGrid();
        setBorder(BorderFactory.createLineBorder(Color.gray,2));
        setBackground(Color.WHITE);
        this.addMouseListener(this);
        Thread worker = new Thread(this);
        isRunning = true;
        worker.start();
    }

    @Override
    public void run() {
        while(isRunning){
            try {
                updateLabels();
                Thread.sleep(UPDATE_RATE);
            } catch (InterruptedException ex) {
                System.out.println("IEX: "+ex.getMessage());
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }
    void changeFrameVisibility(boolean visible){
        if( visible ){
            charFrame = new CharFrame(this);
            charFrameVisible = true;
            setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
        }else{
            charFrame.dispose();
            charFrameVisible = false;
            setBorder(BorderFactory.createLineBorder(Color.gray,2));
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if( this.charFrameVisible == false ){
            changeFrameVisibility(true);
        }else{
            changeFrameVisibility(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    public EVECharacter getCharacter(){
        return character;
    }
    
    public void updateLabels(){
        lbSP.setText(Utils.formatInt(character.skillpoints)+" / "+Utils.formatInt(character.cloneSkillpoints));
        if( character.skillpoints >= character.cloneSkillpoints ){
            if( lowCloneCount == 0 ){
                MainFrame.noti.addLine("Clone upgrade needed: "+character.name);
            }
            lowCloneCount++;
            if(lowCloneCount == NOTIFICATION_IN_SEC){
                lowCloneCount = 0;
            }
        }
        SkillInTraining currSkill = character.getSkillInTraining();
        if( currSkill == null ){
            character.isTraining = false;
        }
        if( character.isTraining ){
            
            lbTaining.setText(currSkill.type.name+" lvl "+currSkill.skillevel);
            lbTaining.setForeground(GREEN);
            Date now = Utils.getNowUTC();
            Date fin = Utils.getUTC(currSkill.endTime);
            int s = (int)((fin.getTime()-now.getTime())/1000);
            if( (s*1000) <= UPDATE_RATE ){
                MainFrame.noti.addLine("Skill training completed: "+character.name+" "+currSkill.type.name+" lvl "+currSkill.skillevel);
            }
            lbFinishes.setText(Utils.formatTime(s));
            if( !character.trainingQueue.isEmpty() ){
                fin = Utils.getUTC(character.trainingQueue.get(character.trainingQueue.size()-1).endTime);
                s = (int)((fin.getTime()-now.getTime())/1000);
                lbQueue.setText(Utils.formatTime(s));
                if(Utils.formatTime(s).startsWith("0")){
                    lbQueue.setForeground(RED);
                    setBackground(LIGHT_RED);
                    gridPanel.setBackground(LIGHT_RED);
                    if( lowQueueCount == 0 ){
                        MainFrame.noti.addLine("Skill queue low: "+character.name);
                    }
                    lowQueueCount++;
                    if(lowQueueCount == NOTIFICATION_IN_SEC){
                        lowQueueCount = 0;
                    }
                }else{
                    lbQueue.setForeground(GREEN);
                    setBackground(LIGHT_GREEN);
                    gridPanel.setBackground(LIGHT_GREEN);
                }
                
                String tooltip = "<html>";
                for(int i=0;i<character.trainingQueue.size();i++){
                    fin = Utils.getUTC(character.trainingQueue.get(i).endTime);
                    if(fin.after(now)){
                        tooltip+=character.trainingQueue.get(i).type.name+" "+character.trainingQueue.get(i).skillevel+"<br />"; 
                    }
                }
                    tooltip+="</html>";
                lbQueue.setToolTipText(tooltip);
            }
        }else{
            setBackground(Color.WHITE);
            gridPanel.setBackground(Color.WHITE);
            lbTaining.setText("Inactive");
            lbTaining.setForeground(RED);
            lbFinishes.setText("-");
            lbQueue.setText("-");
        }
        lbISK.setText(Utils.formatLong((long)character.balance));
        this.repaint();
    }
    private void initGrid(){
        gridPanel = new JPanel(new GridLayout(4, 4,1,1));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 10));
        gridPanel.add(new JLabel("Name:"));
        gridPanel.add(new JLabel(character.name));
        gridPanel.add(new JLabel("Corp:"));
        gridPanel.add(new JLabel(character.corpName));
        gridPanel.add(new JLabel("DoB:"));
        gridPanel.add(new JLabel(character.dayOfBirth));
        gridPanel.add(new JLabel("Skills / clone:"));
        gridPanel.add((lbSP = new JLabel(Utils.formatInt(character.skillpoints)+" / "+Utils.formatInt(character.cloneSkillpoints))));
        if( character.cloneSkillpoints <= character.skillpoints ){
            Msg.warningMsg(character.name+" needs clone upgrade!");
        }
        SkillInTraining currSkill = character.getSkillInTraining();
        if( currSkill == null ){
            character.isTraining = false;
        }
        if( character.isTraining ){
            gridPanel.add(new JLabel("Training:"));
            lbTaining = new JLabel(currSkill.type.name+" lvl "+currSkill.skillevel);
            lbTaining.setForeground(GREEN);
            gridPanel.add(lbTaining);
            gridPanel.add(new JLabel("Finishes:"));
            Date now = Utils.getNowUTC();
            Date fin = Utils.getUTC(currSkill.endTime);
            int s = (int)((fin.getTime()-now.getTime())/1000);
            gridPanel.add((lbFinishes = new JLabel(Utils.formatTime(s))));
            
            gridPanel.add(new JLabel("Queue:"));
            if( !character.trainingQueue.isEmpty() ){
                fin = Utils.getUTC(character.trainingQueue.get(character.trainingQueue.size()-1).endTime);
                s = (int)((fin.getTime()-now.getTime())/1000);
                lbQueue = new JLabel(Utils.formatTime(s));
                if(Utils.formatTime(s).startsWith("0")){
                    lbQueue.setForeground(RED);
                    Msg.infoMsg("Skill queue low: "+character.name);
                }else{
                    lbQueue.setForeground(GREEN);
                }
                gridPanel.add(lbQueue);
                String tooltip = "<html>";
                for(int i=0;i<character.trainingQueue.size();i++){
                    fin = Utils.getUTC(character.trainingQueue.get(i).endTime);
                    if(fin.after(now)){
                        tooltip+=character.trainingQueue.get(i).type.name+" "+character.trainingQueue.get(i).skillevel+"<br />";
                    }
                }
                    tooltip+="</html>";
                lbQueue.setToolTipText(tooltip);
            }else{
                lbQueue = new JLabel("Queue is empty.");
                gridPanel.add(lbQueue);
            }
        }else{
            gridPanel.add(new JLabel("Training:"));
            lbTaining = new JLabel("Inactive");
            lbTaining.setForeground(RED);
            gridPanel.add(lbTaining);
            gridPanel.add(new JLabel("Finishes:"));
            gridPanel.add((lbFinishes = new JLabel("-")));
            gridPanel.add(new JLabel("Queue:"));
            lbQueue = new JLabel("-");
            gridPanel.add(lbQueue);
        }
        gridPanel.add(new JLabel("Balance:"));
        gridPanel.add(lbISK);
        add(gridPanel,BorderLayout.CENTER);
    }
    
}
