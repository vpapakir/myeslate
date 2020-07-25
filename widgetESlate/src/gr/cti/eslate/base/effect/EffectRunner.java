package gr.cti.eslate.base.effect;

import java.awt.*;
import javax.swing.*;

/**
 * This class implements an effect editor.
 *
 * @author      Augustine Grillakis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class EffectRunner
{
  public static final int NO_EFFECT = 0;
  public static final int COMPOSITE_EFFECT = 1;
  public static final int CLIPPING_EFFECT = 2;
  public static final int INTERSECTION_EFFECT = 3;
  public static final int REVERSE_COMPOSITE_EFFECT = 4;

  /**
   * The component on which the effect is applied.
   */
  private Component component;
  /**
   * The id of the current effect.
   */
  private int effectID = NO_EFFECT;
  /** The Thread which plays the effect. A new thread is created every time
   *  an effect is played. The EffectRunner plays only one thread at a time.
   *  Every time a new thread is created, the previous one exits before the
   *  new one starts.
   */
  private EffectThread effectThread;
  /**
   * Used for synchronization.
   */
  private Object sync = new Object();

  /**
   * The current effect.
   */
  EffectInterface effect = null;

  /**
   * <code>Runnable</code> for invoking the component's
   * <code>repaint()</code> method via
   * <code>SwingUtilities.invokeAndWait()</code>.
   */
  private Runnable doRepaint = new Runnable()
  {
    public void run()
    {
      component.repaint();
    }
  };

  /**
   * Constructs the EffectRunner on a specific component.
   * @param     component       The component the effect materializes on.
   */
  public EffectRunner(Component component)
  {
    this.component = component;
  }

  /**
   * Starts playing the active effect, if one is set.
   */
  public void start()
  {
    //System.out.println("Starting effect " + effect);
    if (effect == null) {
      return;
    }

    EffectThread th;
    synchronized (sync) {
      th = effectThread;
    }
    if (th != null) {
      th.interrupt();
      try {
        th.join();
      } catch (InterruptedException e) {}
    }
    effectThread = new EffectThread(this);
    effectThread.start();
  }

  /**
   * Set active effect using one of the known Effect ids.
   * @param     newEffect       The effect id. One of <code>NO_EFFECT</code>,
   *                            <code>COMPOSITE_EFFECT</code>,
   *                            <code>CLIPPING_EFFECT</code>,
   *                            <code>INTERSECTION_EFFECT</code>,
   *                            <code>REVERSE_COMPOSITE_EFFECT</code>.
   */
  public void setEffectID(int newEffect)
  {
    if (effectThread != null) {
      effectThread.stopThread();
    }
    switch (effectID) {
      case NO_EFFECT:
        effect = null;
        break;
      case COMPOSITE_EFFECT:
        effect =
          new CompositeEffect(100 /*compositeNumberOfSteps*/, false, false);
        break;
      case CLIPPING_EFFECT:
        effect = new ClippingEffect(50 /*clippingNumberOfSteps*/, false, false);
        break;
      case INTERSECTION_EFFECT:
        effect = new IntersectionEffect(
          100 /*intersectionNumberOfSteps*/,
          IntersectionEffect.HEIGHT_INCREASE /*direction*/,
          false, false
        );
        break;
      case REVERSE_COMPOSITE_EFFECT:
        effect = new ReverseCompositeEffect(
          100 /*compositeNumberOfSteps*/, false, false
        );
        break;
      default:
        return ;
    }
    if (effect != null) {
      start();
    }
    this.effectID = newEffect;
  }

  /**
   * Returns the id of the current effect. If the current effect is
   * unknown to the EffectRunner, NO_EFFECT is returned.
   */
  public int getEffectID()
  {
    return effectID;
  }

  /**
   * Sets the active effect.
   * @param     effect  The effect.
   */
  public void setEffect(EffectInterface effect)
  {
    if (effectThread != null) {
      effectThread.stopThread();
    }
    this.effect = effect;
    start();
  }

  /**
   * Returns the effect that is set to the EffectRunner.
   * @return    The active effect.
   */
  public EffectInterface getEffect()
  {
    return effect;
  }

  /**
   * Reports if an effect is currently running.
   */
  public boolean isEffectRunning()
  {
    return (effectThread != null);
  }

  class EffectThread extends Thread
  {
    EffectRunner effectRunner;
    boolean stopFlag = false;
    protected long sleepAmount = 30;

    public EffectThread(EffectRunner effectRunner)
    {
      this.effectRunner = effectRunner;
    }

    public void stopThread()
    {
      stopFlag = true;
    }

    public void run()
    {
      EffectInterface effect = effectRunner.effect;
      effect.setBounds(effectRunner.component.getBounds());
      effect.init();
      do {
        try {
          SwingUtilities.invokeAndWait(doRepaint);
        } catch (Exception e) {
        }
      } while (!stopFlag && !effect.step());
      synchronized (sync) {
        effectRunner.effectThread = null;
      }
      if (!stopFlag) {
        EffectTerminationHook effectTerminationHook = effect.getEffectTerminationHook();
        if (effectTerminationHook != null) {
          effectTerminationHook.effectTerminated();
        }
        try {
          SwingUtilities.invokeAndWait(doRepaint);
        } catch (Exception e) {
        }
      }
    }
  }
}
