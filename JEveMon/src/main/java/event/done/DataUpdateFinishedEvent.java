package event.done;

import event.AbstractEvent;

/**
 * Data update finished event.
 * @author lecsa
 */
public class DataUpdateFinishedEvent extends AbstractEvent{
    
    public DataUpdateFinishedEvent(Object source) {
        super(source);
    }
    
}
