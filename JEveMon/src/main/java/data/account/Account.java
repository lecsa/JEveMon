/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.account;

import data.character.EVECharacter;
import java.util.ArrayList;

/**
 *
 * @author lecsa
 */
public class Account {
    private String expires;
    private ArrayList<EVECharacter> characters = new ArrayList<>();

    public Account(String expires) {
        this.expires = expires;
    }
    
    public void addCharacter(EVECharacter c){
        characters.add(c);
    }
    public void setCharacters(ArrayList<EVECharacter> chars){
        this.characters = chars;
    }
    public String getExpires() {
        return expires;
    }

    public ArrayList<EVECharacter> getCharacters() {
        return characters;
    }
    
}
