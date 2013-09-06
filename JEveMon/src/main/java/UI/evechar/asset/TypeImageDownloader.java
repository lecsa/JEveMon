/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.evechar.asset;

import java.util.concurrent.Callable;
import javax.swing.ImageIcon;
import utils.ImageHandler;

/**
 *
 * @author lecsa
 */
public class TypeImageDownloader implements Callable<Void>{
    private static final int LIMIT_OF_CALLS = 8;//total number of download threads
    private static int currentNumberOfCalls = 0;//current number of download threads
    private static final int MAX_ATTEMPTS = 200;//max attempts to download
    private int attempts = 0;//attempt counter
    private ItemTreeElement itemrow;//itemrow object to update
    
    public TypeImageDownloader(ItemTreeElement itemrow){
        this.itemrow = itemrow;
    }
    @Override
    public Void call() throws Exception {
        ImageIcon theIcon = null;
        while( theIcon == null && attempts < MAX_ATTEMPTS ){
            if( LIMIT_OF_CALLS > currentNumberOfCalls ){
                currentNumberOfCalls++;
                theIcon = ImageHandler.getTypeIMG(itemrow.getItem().getId());
                if( theIcon!=null ){
                    itemrow.updateIcon(theIcon);
                }
                currentNumberOfCalls--;
            }else{
                attempts++;
                Thread.sleep(100+(int)(Math.random()*100));
            }
        }
        return null;
    }
    
}
