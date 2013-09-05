package event;

/**
 * Basic event.
 * @author lecsa
 */
public abstract class AbstractEvent {
    private Object source;
    
    public AbstractEvent(Object source){
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
    
}
