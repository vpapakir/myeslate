package gr.cti.eslate.protocol;

import gr.cti.typeArray.*;

/**
 * This interface declares the methods that components connecting to the
 * sequence component through its "Actor" plug must implement.
 *
 * @author      Augustine Grillakis
 * @version     3.0.0, 19-May-2006
 * @see         gr.cti.eslate.protocol.ActorInteface
 */
public interface ActorInterface {

    /**
     * @return  The values of the variables of the actor.
     */
    public IntBaseArray getVarValues();

    /**
     * Sets the values of the actor's variables.
     * @param   varValues         The values of the actor's variables.
     * @param   animationSession  The animation session to set the values.
     */
    public void setVarValues(IntBaseArray varValues, AnimationSession animationSession);

    /**
     * Get the animated property structure.
     * @return  The animated property structure.
     */
    public AnimatedPropertyStructure getAnimatedPropertyStructure();

    /**
     * Actor is active (on stage).
     * @param   animationSession  The actor's animation session.
     */
    public void onStage(AnimationSession animationSession);

    /**
     * Actor is inactive (off stage).
     * @param   animationSession  The actor's animation session.
     */
    public void offStage(AnimationSession animationSession);

    /**
     * Get actor name.
     * @return The actor's name.
     */
    public String getActorName();

    /**
     * Get plugs number.
     * @return The plug's number.
     */
    public int getPlugCount();

    /**
     * Add a listener for actor name events.
     * @param   listener        The listener to add.
     */
    public void addActorNameListener(ActorNameListener listener);

    /**
     * Remove a listener from actor's name events.
     * @param   listener        The listener to remove.
     */
    public void removeActorNameListener(ActorNameListener listener);

    /**
     * Fires all listeners registered for milestone events.
     * @param   actorName  The actor's name that changed.
     */
    public void fireActorNameListeners(String actorName);
}
