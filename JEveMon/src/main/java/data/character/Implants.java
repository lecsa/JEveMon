package data.character;

import data.character.Attributes.Attribute;

/**
 * Implant handler class
 * @author bayi
 */
public class Implants {
    
    /**
     * Array for storing implant values
     */
    private int[] implantValues = {0,0,0,0,0};
    
    /**
     * Get value for selected Attribute Implant
     * @param attribute Selected Attribute
     * @retval int Value for selected Attribute Implant
     */
    public int getImplant( Attribute attribute ) {
        return implantValues[ attribute.ordinal() ];
    }
    
    /**
     * Set value for selected Attribute Implant
     * @param attribute Selected Attribute
     * @param value Value for selected Attribute Implant
     */
    public void setImplant( Attribute attribute , int value ) {
        implantValues[ attribute.ordinal() ] = value;
    }
    
    /**
     * Sets all the attribute implant value data once
     * @param value An Attributes indexed array of ints @see Attribute
     */
    public void setImplant( int[] value ) {
        for ( Attribute attributeId : Attribute.values() ) {
            setImplant( attributeId , value[ attributeId.ordinal() ]);
        }
    }
}
