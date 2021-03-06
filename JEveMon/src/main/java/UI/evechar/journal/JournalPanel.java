/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar.journal;

import API.APIHandler;
import data.character.EVECharacter;
import data.journal.JournalElement;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lecsa
 */
public class JournalPanel extends JPanel implements ActionListener{
    private JButton btFetch = new JButton("Get journal data");
    private EVECharacter character;
    private JTable table = new JTable();
    private JScrollPane view = new JScrollPane();
    
    public JournalPanel(EVECharacter c){
        this.character = c;
        setLayout(new BorderLayout());
        JPanel flow = new JPanel(new FlowLayout());
        flow.add(btFetch);
        btFetch.addActionListener(this);
        add(flow,BorderLayout.NORTH);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        view = new JScrollPane(table);
        view.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(view,BorderLayout.CENTER);
        updateJournalTable();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btFetch)){
            Thread filler = new Thread(new Runnable() {

                @Override
                public void run() {
                    btFetch.setEnabled(false);
                    APIHandler.fillCharacterWalletJournal(character);
                    updateJournalTable();
                    btFetch.setEnabled(true);
                }
            });
            filler.start();
        }
    }
    
    private void updateJournalTable(){
        Object[] columnNames = new Object[]
        {
        "Date",                 //0
        "Type",//refTypeName    //1
        "Owner",                //2
        "ISK amount",           //3
        "Balance",              //4
                                
        "Item",//typename       //5
        "Prize",                //6
        "Quantity",             //7
        "Client",               //8
        "Station",              //9
        "Buy/sell",//transactiontype    //10
        "For",                  //11
        "Reason"                //12
        };
        //build data
        Object[][] data = new Object[character.getWalletJournal().size()][columnNames.length];
        for(int i=0;i<character.getWalletJournal().size();i++){
            JournalElement e = character.getWalletJournal().get(i);
            DecimalFormat f = new DecimalFormat("###,###,###,###");
            String s,c = "</font></html>";
            if( e.iskAmount < 0 ){
                s = "<html><font color=#991111>";
            }else{
                s = "<html><font color=#119911>";
            }
            data[i][0] = (Object)e.date;
            data[i][1] = (Object)e.refTypeName;
            data[i][2] = (Object)e.owner1Name;
            data[i][3] = (Object)s+f.format(e.iskAmount)+c;
            data[i][4] = (Object)f.format(e.balance);
            data[i][5] = (Object)e.transaction.typeName;
            data[i][6] = (Object)f.format(e.transaction.price);
            data[i][7] = (Object)Integer.toString(e.transaction.quantity);
            data[i][8] = (Object)e.transaction.clientName;
            data[i][9] = (Object)e.transaction.stationName;
            data[i][10]= (Object)e.transaction.transactionType;
            data[i][11]= (Object)e.transaction.transactionFor;
            data[i][12]= (Object)e.reason;
        }
        updateTable(data, columnNames);
    }
    
    private void updateTable(final Object[][] data, final Object[] columnNames){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DefaultTableModel model = new DefaultTableModel(data, columnNames){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
//                table.setDefaultRenderer(Object.class, new CellRenderer());
                table.setRowHeight(25);
                table.setModel(model);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                TableColumnAdjuster tca = new TableColumnAdjuster(table);
                tca.adjustColumns();
            }
        });
    }
}
