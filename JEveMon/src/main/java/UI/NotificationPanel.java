/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author lecsa
 */
public class NotificationPanel extends JPanel implements ActionListener{
    
    private JTextArea textArea = new JTextArea();
    private JPanel pnFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JButton btClear = new JButton("Clear notifications");
    
    public NotificationPanel(){
        setLayout(new BorderLayout());
        btClear.addActionListener(this);
        pnFlow.add(btClear);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        JScrollPane jsp = new JScrollPane(textArea);
        jsp.setPreferredSize(new Dimension(1300, 80));
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.getVerticalScrollBar().setUnitIncrement(16);
        add(jsp,BorderLayout.CENTER);
        add(pnFlow,BorderLayout.NORTH);
        textArea.setText("Notifications:\n");
    }
    public void addLine(String line){
        textArea.setText(textArea.getText()+line+"\n");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        textArea.setText("Notifications:\n");
    }
    
    
}
