package gr.cti.eslate.utils.sound;

import java.util.EventListener;


public interface ESlateSoundListener extends EventListener {
    public void soundStopped(ESlateSoundEvent e);
}