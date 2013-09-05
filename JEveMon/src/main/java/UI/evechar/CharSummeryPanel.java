/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar;

import data.character.EVECharacter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author lecsa
 */
public class CharSummeryPanel extends JPanel implements ActionListener{
    private ExtendedCharPanel[] panels;
    private ArrayList<ExtendedCharPanel> visiblePanels = new ArrayList();
    private JPanel grid = new JPanel();
    private JCheckBox cbInactive = new JCheckBox("Inactive");
    private JCheckBox cbActive = new JCheckBox("Active");
    private JScrollPane jsp = null;

    public CharSummeryPanel(ArrayList<EVECharacter> characters){
        setLayout(new BorderLayout());
        panels = new ExtendedCharPanel[characters.size()];
        JPanel spaceFillerPanel = new JPanel();
        for(int i=0;i<characters.size();i++){
            panels[i] = new ExtendedCharPanel(characters.get(i));
            if(characters.get(i).isTraining){
                visiblePanels.add(panels[i]);
            }

        }
        JPanel pnTop = new JPanel(new FlowLayout());
        cbActive.addActionListener(this);
        cbInactive.addActionListener(this);
        pnTop.add(cbInactive);
        pnTop.add(cbActive);
        cbActive.setSelected(true);
        add(pnTop,BorderLayout.NORTH);
        grid = new JPanel(new GridBagLayout());
        jsp = new JScrollPane(grid);
        add(jsp,BorderLayout.CENTER);
        updateGrid();
    }

    private void updateVisibility(){
        visiblePanels = new ArrayList();
        for(int i=0;i<panels.length;i++){
            if( panels[i].getCharacter().isTraining && cbActive.isSelected() ){
                visiblePanels.add(panels[i]);
            }else if( !panels[i].getCharacter().isTraining && cbInactive.isSelected() ){
                visiblePanels.add(panels[i]);
            }
        }
    }
    private void updateGrid(){
        Thread updater = new Thread(new Runnable() {

            @Override
            public void run() {
                updateVisibility();
                remove(jsp);
                grid = new JPanel(new GridBagLayout());
                grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTH;

                for (int i = 0; i < visiblePanels.size(); i++) {
                    gbc.gridy = gbc.gridy + 1;
                    grid.add(visiblePanels.get(i), gbc);
                }
                gbc.weighty=1;
                grid.add(new JPanel(), gbc);
                jsp = new JScrollPane(grid);
                jsp.getVerticalScrollBar().setUnitIncrement(16);
                add(jsp,BorderLayout.CENTER);
                jsp.repaint();
                revalidate();
                repaint();
            }
        });
        updater.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGrid();
    }

}
