package gr.cti.eslate.base.effect;

import java.awt.*;

/**
 * This class implements an alpha composite effect (fade in).
 *
 * @author      Augustine Grillakis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class CompositeEffect implements EffectInterface
{
  /**
   * One of RenderingHints.VALUE_ANTIALIAS_OFF,
   * RenderingHints.VALUE_ANTIALIAS_ON.
   */
  private Object antiAlias = RenderingHints.VALUE_ANTIALIAS_OFF;
  /**
   * One of RenderingHints.VALUE_RENDER_SPEED,
   * RenderingHints.VALUE_RENDER_QUALITY.
   */
  private Object renderingQuality = RenderingHints.VALUE_RENDER_SPEED;
  /**
   * The steps in which the effect will take place.
   */
  private int numberOfSteps;

  /**
   * The current value of the composite's alpha.
   */
  float compositeValue;
  /**
   * The step at which the value of the compoiste's alpha increases.
   */
  float compositeStep;
  /**
   *The bounds of the rectangular area the effect acts upon.
   */
  Rectangle compoBounds = null;
  /**
   * The code to execute immediately after the effect terminates.
   */
  private EffectTerminationHook etHook;

  /**
   * Create an alpha composite effect.
   * @param     numberOfSteps           The number of steps the effect realizes.
   * @param     antiAliasOn             Whether antialising will be used.
   * @param     renderingQualityOn      True for maximum rendering quality.
   *                                    terminates.
   */
  public CompositeEffect(
    int numberOfSteps, boolean antiAliasOn, boolean renderingQualityOn)
  {
    this(numberOfSteps, antiAliasOn, renderingQualityOn, null);
  }

  /**
   * Create an alpha composite effect.
   * @param     numberOfSteps           The number of steps the effect realizes.
   * @param     antiAliasOn             Whether antialising will be used.
   * @param     renderingQualityOn      True for maximum rendering quality.
   * @param     hook                    Code to execute when the effect
   *                                    terminates.
   */
  public CompositeEffect(
    int numberOfSteps, boolean antiAliasOn, boolean renderingQualityOn,
    EffectTerminationHook hook)
  {
    if (numberOfSteps <= 0) {
      throw new IllegalArgumentException(
        "The number of steps must be at least 1"
      );
    }
    etHook = hook;
    this.numberOfSteps = numberOfSteps;
    antiAlias = (antiAliasOn) ? RenderingHints.VALUE_ANTIALIAS_ON
                              : RenderingHints.VALUE_ANTIALIAS_OFF;
    renderingQuality =
      (renderingQualityOn) ? RenderingHints.VALUE_RENDER_QUALITY
                           : RenderingHints.VALUE_RENDER_SPEED;
  }

  public void init()
  {
    compositeValue = 0.0f;
    compositeStep = 1.0f / numberOfSteps;
  }

  public void setBounds(Rectangle compoBounds)
  {
    this.compoBounds = compoBounds;
  }

  public Rectangle getBounds()
  {
    return compoBounds;
  }

  /**
   * Compute next effect values.
   * @return    True if effect has finished.
   */
  public boolean step()
  {
    compositeValue += compositeStep;
    if (compositeValue > 1.0f) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Realize effect.
   * @param     g       The graphics context on which to realize the effect.
   */
  public void realizeEffect(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias);
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, renderingQuality);
    g2.setComposite(
      AlphaComposite.getInstance(AlphaComposite.SRC_OVER, compositeValue)
    );
  }

  public void setAntiAliased(boolean aa)
  {
    antiAlias = aa ? RenderingHints.VALUE_ANTIALIAS_ON
                   : RenderingHints.VALUE_ANTIALIAS_OFF;
  }

  public boolean isAntiAliased()
  {
    return (antiAlias == RenderingHints.VALUE_ANTIALIAS_ON);
  }


  public void setRenderingQualityOn(boolean rq)
  {
    renderingQuality = rq ? RenderingHints.VALUE_RENDER_QUALITY
                          : RenderingHints.VALUE_RENDER_SPEED;
  }

  public boolean isRenderingQualityOn()
  {
    return (renderingQuality == RenderingHints.VALUE_RENDER_QUALITY);
  }

  public void setNumberOfSteps(int steps)
  {
    if (steps > 0) {
      numberOfSteps = steps;
    }
  }

  public int getNumberOfSteps()
  {
    return numberOfSteps;
  }

  /**
   ** Returns the code to execute immediately after the effect terminates.
   * @return    The <code>EffectTerminationHook</code> instance with the
   *            requested code.
   */
  public EffectTerminationHook getEffectTerminationHook()
  {
    return etHook;
  }

}
