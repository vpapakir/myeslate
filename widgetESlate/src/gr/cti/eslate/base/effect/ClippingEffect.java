package gr.cti.eslate.base.effect;

import java.awt.*;
import java.awt.geom.*;

/**
 * This class implements a clipping effect.
 *
 * @author      Augustine Grillakis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ClippingEffect implements EffectInterface
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
   * The step of the increase of the readious of the ellipse. This is
   * calculated based on the size of the component and the number of steps.
   */
  private double clippingStep;

  /**
   * The X coordinate of the ellipse.
   */
  double x;
  /**
   * The Y coordinate of the ellipse.
   */
  double y;
  /**
   * The width of the ellipse;
   */
  double ew;
  /**
   * The height of the ellipse;
   */
  double eh;
  /**
   * The width of the component.
   */
  int w;
  /**
   * The height of the component.
   */
  int h;
  /**
   * The square of the width of the component.
   */
  private double w2;
  /**
   * The square of the height of the component.
   */
  private double h2;
  /**
   * The bounds of the rectangular area upon which the effect acts.
   */
  Rectangle compoBounds = null;
  /**
   * The code to execute immediately after the effect terminates.
   */
  private EffectTerminationHook etHook;

  /**
   * Create a clipping effect.
   * @param     numberOfSteps           The number of steps the effect realizes.
   * @param     antiAliasOn             Whether antialising will be used.
   * @param     renderingQualityOn      True for maximum rendering quality.
   */
  public ClippingEffect(
    int numberOfSteps, boolean antiAliasOn, boolean renderingQualityOn)
  {
    this(numberOfSteps, antiAliasOn, renderingQualityOn, null);
  }

  /**
   * Create a clipping effect.
   * @param     numberOfSteps           The number of steps the effect realizes.
   * @param     antiAliasOn             Whether antialising will be used.
   * @param     renderingQualityOn      True for maximum rendering quality.
   * @param     hook                    Code to execute when the effect
   *                                    terminates.
   */
  public ClippingEffect(
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
  }

  public void setBounds(Rectangle compoBounds)
  {
    this.compoBounds = compoBounds;
    Dimension d = compoBounds.getSize();
    w = d.width;
    h = d.height;
    w2 = (double)(w * w);
    h2 = (double)(h * h);
    x = w / 2;
    y = h / 2;
    ew = 0;
    eh = 0;

    if (w >= h) {
      clippingStep = w / numberOfSteps;
    }else{
      clippingStep = h / numberOfSteps;
    }
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
    x -= clippingStep;
    y -= clippingStep;
    ew += clippingStep * 2;
    eh += clippingStep * 2;
    // When the upper right corner of the component's bounding box is inside
    // the ellipse, then we are done.
    boolean inside = (w2/(ew*ew) + h2/(eh*eh)) < 1.0;
    if (inside) {
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
    //GeneralPath p1 = new GeneralPath();
    //p1.append(new Ellipse2D.Double(x, y, ew, eh), false);
    //g2.clip(p1);
    g2.clip(new Ellipse2D.Double(x, y, ew, eh));
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
