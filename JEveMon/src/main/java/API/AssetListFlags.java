/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

/**
 *
 * @author lecsa
 */
public class AssetListFlags {
    
    public enum Flag_t{
        FLAG_CARGO (5),
        FLAG_LOW_SLOT_1 (11),
        FLAG_LOW_SLOT_2 (12),
        FLAG_LOW_SLOT_3 (13),
        FLAG_LOW_SLOT_4 (14),
        FLAG_LOW_SLOT_5 (15),
        FLAG_LOW_SLOT_6 (16),
        FLAG_LOW_SLOT_7 (17),
        FLAG_LOW_SLOT_8 (18),
        FLAG_MED_SLOT_1 (19),
        FLAG_MED_SLOT_2 (19),
        FLAG_MED_SLOT_3 (20),
        FLAG_MED_SLOT_4 (21),
        FLAG_MED_SLOT_5 (22),
        FLAG_MED_SLOT_6 (23),
        FLAG_MED_SLOT_7 (24),
        FLAG_MED_SLOT_8 (25),
        FLAG_HIGH_SLOT_1 (27),
        FLAG_HIGH_SLOT_2 (28),
        FLAG_HIGH_SLOT_3 (29),
        FLAG_HIGH_SLOT_4 (30),
        FLAG_HIGH_SLOT_5 (31),
        FLAG_HIGH_SLOT_6 (32),
        FLAG_HIGH_SLOT_7 (33),
        FLAG_HIGH_SLOT_8 (34),
        FLAG_DRONE_BAY(87)
        ;
        
        private int value;
        
        Flag_t(int val){
            value = val;
        }
        public int getValue(){
        return value;
        }
        
    }
}
