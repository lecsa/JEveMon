package event.done;

import java.util.ArrayList;

/**
 * Data update finished event notifier.
 * @author lecsa
 */
public class DataUpdateFinishedNotifier {
    ArrayList<DataUpdateFinishedListener> listeners = new ArrayList<>();
    public DataUpdateFinishedNotifier () {
      
    }
    public DataUpdateFinishedNotifier (DataUpdateFinishedListener cl) {
       addListener(cl);
    }
    
    public void addListener(DataUpdateFinishedListener cl){
        listeners.add(cl);
    }
    
    public void notifyListeners(DataUpdateFinishedEvent e){
        for(DataUpdateFinishedListener cl : listeners){
            cl.dataUpdateFinishedEvent(e);
        }
    }
 }
