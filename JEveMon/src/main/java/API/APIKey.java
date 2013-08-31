/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import java.io.Serializable;

/**
 *
 * @author lecsa
 */
public class APIKey implements Serializable{
    private String name = "";
    private String keyID = "";
    private String vCode = "";

    public APIKey(String name, String keyID, String vCode) {
        this.keyID = keyID;
        this.name = name;
        this.vCode = vCode;
    }

    public String getName() {
        return name;
    }

    public String getKeyID() {
        return keyID;
    }

    public String getvCode() {
        return vCode;
    }
    
    public String getURL(String aspx){
        return getURL(Defaults.SERVER, Defaults.DIR, aspx);
    }
    public String getURL(String dir, String aspx){
        return getURL(Defaults.SERVER,dir,aspx);
    }
    public String getURL(String server, String dir, String aspx){
        return "https://"+server+"/"+dir+"/"+aspx+"?keyID="+getKeyID()+"&vCode="+getvCode();
    }
}
