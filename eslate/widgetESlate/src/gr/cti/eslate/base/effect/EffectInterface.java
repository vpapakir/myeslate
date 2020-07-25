package gr.cti.eslate.base.effect;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * This interface declares the methods that UI effects must implement.
 *
 * @author      Augustine Grillakis
 * @author      kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface EffectInterface
{
  /**
   * Compute the next effect values.
   * @return    True if effect has finished.
   */
  public boolean step();

  /**
   * Applies the current values of the effect to the supplied Graphics.
   * @param     g       The graphics context on which to realize the effect.
   */
  public void realizeEffect(Graphics g);

  /**
   * Effects which have to do with the bounds of the rectagular area on which
   * they act, should override this method to set their variables. This method
   * is always called once, before the execution of the effect starts.
   * @param     compoBounds     The bounds of the rectangular area upon which
   *                            the effect acts.
   */
  void setBounds(Rectangle compoBounds);

  /**
   * Returns the bounds of the effect, i.e., the bounds of the rectangular
   * area upon which the effect takes place.
   */
  public Rectangle getBounds();

  /**
   * By overriding this method, the effect has the chance to perform any
   * required initializations. This method will be called once before the
   * execution of the effect starts and right after <code>setBounds</code>
   * has been called.
   */
  public void init();

  /**
   ** Returns the code to execute immediately after the effect terminates.
   * @return    The <code>EffectTerminationHook</code> instance with the
   *            requested code.
   */
  public EffectTerminationHook getEffectTerminationHook();
}
