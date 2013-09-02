package data.character;
/**
 * Class that handles the main Attributes of a character
 * @author bayi
 */
public class Attributes extends Implants {
    
    /**
     * The main character Attributes
     */
    public enum Attribute {
        INTELLIGENCE, MEMORY, PERCEPTION, WILLPOWER, CHARISMA
    }
    
    /**
     * Array for storing raw Attribute values ( Remap only )
     */
    private int[] attributesRawValues = {0,0,0,0,0};
    
    /**
     * Gets the selected attribute
     * @param attribute The selected attribute
     * @retval int The value of the requested Attribute
     */
    public int getRaw( Attribute attribute ) {
        return attributesRawValues[attribute.ordinal()];
    }
    
    /**
     * Gets Total ( Remap + Implants ) Attribute value for selected Attribute
     * @param attribute Selected Attribute
     * @retval int Total Attribute value
     */
    public int get( Attribute attribute ) {
        return attributesRawValues[ attribute.ordinal() ] + getImplant(attribute);
    }
    
    /**
     * Sets the selected attribute to a value
     * @param attribute The selected attribute
     * @param value The value to set the selected attribute
     */
    public void setRaw( Attribute attribute , int value ) {
        //Debug Check ?
        //System.out.println("Attribute: " + attribute.toString() + " (" + attribute.ordinal() + ") Set to: " + value);
        attributesRawValues[ attribute.ordinal() ] = value;
    }
    
    /**
     * Sets all the attribute data once
     * @param value An Attribute indexed array of ints @see Attribute
     */
    public void setRaw( int[] value ) {
        for ( Attribute attributeId : Attribute.values() ) {
            setRaw( attributeId , value[ attributeId.ordinal() ]);
        }
    }
    
    /**
     * Gets the Skill Point / Hour for the given Primary and Secondary Attributes
     * @param primary Primary Attribute
     * @param secondary Secondary Attribute
     * @retval int Skill Point / Hour
     */
    public int getSPH( Attribute primary , Attribute secondary ) {
        return ( getRaw(primary) + ( getRaw(secondary) / 2 ) ) * 60;
    }
}
