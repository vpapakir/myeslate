package gr.cti.eslate.base.effect;

/**
 * This interface describes the code to execute immediately after an effect
 * terminates, e.g., remove an image after having faded it out.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public interface EffectTerminationHook
{
  /**
   * Invoked when an effect terminates.
   */
  public void effectTerminated();
}
