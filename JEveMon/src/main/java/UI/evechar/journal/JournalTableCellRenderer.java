/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar.journal;

import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author lecsa
 */
public class JournalTableCellRenderer  extends JLabel implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, final int row, int column) {

        JLabel lb = (JLabel)this;

        if( hasFocus ){
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            lb=null;
        }else{
            setBackground(table.getBackground());
            setBorder(null);
        }
        if( isSelected ){    
            setBackground(table.getSelectionBackground());
            setBorder(null);
        }else{
            setBackground(table.getBackground());
            setBorder(null);
        }
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setFont(new Font("SansSerif", Font.PLAIN, 13));
        this.setOpaque(true);
        setText((String) value);
        
        return this;
    }
}