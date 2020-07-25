package gr.cti.eslate.utils.sound;


public class ESlateSoundEvent extends java.util.EventObject
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    public ESlateSoundEvent(ESlateSound source) {
        super(source);
    }
}