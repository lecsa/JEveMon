/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.account.Account;
import java.util.ArrayList;

/**
 *
 * @author lecsa
 */
public class DataProvider {
    ArrayList<Account> accounts = new ArrayList();

    public DataProvider() {
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    
}
