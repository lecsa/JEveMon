/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.Map;

/**
 *
 * @author lecsa
 */
public class JournalElement {
    public String date = "";
    public int refTypeID = 0;
    public String refTypeName = "";
    public long refID = 0;
    public String owner1Name = "";
    public String owner2Name = "";
    public long iskAmount = 0;
    public long balance = 0;
    public String reason = "";
    public TransactionElement transaction = new TransactionElement("n/a", 0, "n/a", "n/a", "", 0);
    public static Map<Integer,String> refTypes = null;
    
    public JournalElement(String date, long refID, int refTypeID, String owner1Name, String owner2Name, long iskAmount, long balance, String reason) {
        this.iskAmount = iskAmount;
        this.balance = balance;
        this.refID = refID;
        this.refTypeID = refTypeID;
        this.date = date;
        this.owner1Name = owner1Name;
        this.owner2Name = owner2Name;
        this.reason = reason;
        if( refTypes != null ){
            refTypeName = refTypes.get(new Integer(refTypeID));
        }
    }
    
    
    
}
