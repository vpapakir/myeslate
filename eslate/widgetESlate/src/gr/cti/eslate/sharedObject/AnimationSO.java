package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.animation.*;

/**
 * A shared object wrapping a animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 23-Apr-2002
 */
public class AnimationSO extends SharedObject {
    private Animation animation;

    /**
     * The constructor.
     * @param   handle    The associated ESlateHandle instance.
     * @param   animation  The animation of the shared object.
     */
    public AnimationSO(ESlateHandle handle, Animation animation) {
      super(handle);
      this.animation = animation;
    }

    /**
     * @return  The animation of the shared object.
     */
    public Animation getAnimation() {
      return animation;
    }

    /**
     * Sets the animation.
     * @param   animation    The animation.
     */
    public void setAnimation(Animation animation) {
        this.animation = animation;
        // Create an event
        SharedObjectEvent e = new SharedObjectEvent(this);
        fireSharedObjectChanged(e);	// Notify the listeners
    }
}
