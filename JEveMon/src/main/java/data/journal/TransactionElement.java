/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.journal;

/**
 *
 * @author lecsa
 */
public class TransactionElement {
    public String typeName = "";
    public double price;
    public String clientName = "";
    public String stationName = "";
    public String transactionType = "";
    public int quantity = 0;
    public String transactionFor = "";
    public TransactionElement(String typeName, double price, String clientName, String stationName, String transactionType, int quantity, String transactionFor) {
        this.typeName = typeName;
        this.price = price;
        this.clientName = clientName;
        this.stationName = stationName;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.transactionFor = transactionFor;
    }
    
}
