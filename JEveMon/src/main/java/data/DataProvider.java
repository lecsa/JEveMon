/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.account.Account;
import java.util.ArrayList;

/**
 * Main data source.
 * @author lecsa
 */
public class DataProvider {
    ArrayList<Account> accounts = new ArrayList();

    public DataProvider() {
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    
    public Account getAccountByCharacterID(int characterID){
        Account retval = null;
            for(int i=0;i<accounts.size() && retval == null;i++){
                for(int n=0;n<accounts.get(i).getCharacters().size() && retval == null;n++){
                    if(accounts.get(i).getCharacters().get(n).getId() == characterID){
                        retval = accounts.get(i);
                    }
                }
            }
        return retval;
    }
}
