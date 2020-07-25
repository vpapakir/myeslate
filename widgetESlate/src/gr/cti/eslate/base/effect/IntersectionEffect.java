package gr.cti.eslate.base.effect;

import java.awt.*;
import java.awt.geom.*;

/**
 * This class implements an intersection effect.
 *
 * @author      Augustine Grillakis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class IntersectionEffect implements EffectInterface
{
  public static final int HEIGHT_INCREASE = 0;
  public static final int HEIGHT_DECREASE = 1;
  public static final int WIDTH_DECREASE = 2;
  public static final int WIDTH_INCREASE = 3;

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
  private int numberOfSteps = 100;

  /**
   * The X coordinate of the intersection rectangle.
   */
  private int xx;
  /**
   * The Y coordinate of the intersection rectangle.
   */
  private int yy;
  /**
   * The width the intersection rectangle.
   */
  private int ww;
  /**
   * The height the intersection rectangle.
   */
  private int hh;
  /**
   * The step at which the intersection rectangle grows or shrinks.
   */
  double intersectionStep;
  /**
   * The intersection type. One of HEIGHTDECREASE, HEIGHTDECREASE,
   * WIDTHDECREASE, WIDTHINCREASE.
   */
  private int direction = HEIGHT_DECREASE;

  /**
   * The width of the component.
   */
  private int w;
  /**
   * The height of the component.
   */
  private int h;
  /**
   * The bounds of the rectangular area upon which the effect acts.
   */
  Rectangle compoBounds = null;
  /**
   * The code to execute immediately after the effect terminates.
   */
  private EffectTerminationHook etHook;

  /**
   * Create an intersection effect.
   * @param     numberOfSteps           The number of steps the effect realizes.
   * @param     direction               The direction of the intersection.
   * @param     antiAliasOn             Whether antialising will be used.
   * @param     renderingQualityOn      True for maximum rendering quality.
   */
  public IntersectionEffect(
    int numberOfSteps, int direction, boolean antiAliasOn,
    boolean renderingQualityOn)
  {
    this(numberOfSteps, direction, antiAliasOn, renderingQualityOn, null);
  }

  /**
   * Create an intersection effect.
   * @param     numberOfSteps           The number of steps the effect realizes.
   * @param     direction               The direction of the intersection.
   * @param     antiAliasOn             Whether antialising will be used.
   * @param     renderingQualityOn      True for maximum rendering quality.
   * @param     hook                    Code to execute when the effect
   *                                    terminates.
   */
  public IntersectionEffect(
    int numberOfSteps, int direction, boolean antiAliasOn,
    boolean renderingQualityOn, EffectTerminationHook hook)
  {
    if (numberOfSteps <= 0) {
      throw new IllegalArgumentException(
        "The number of steps must be at least 1"
      );
    }
    if (direction != HEIGHT_INCREASE && direction != HEIGHT_DECREASE &&
        direction != WIDTH_DECREASE && direction != WIDTH_INCREASE) {
      throw new IllegalArgumentException("Invalid direction argument");
    }

    etHook = hook;
    this.numberOfSteps = numberOfSteps;
    this.direction = direction;
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
    w = compoBounds.width;
    h = compoBounds.height;

    switch (direction) {
      case HEIGHT_DECREASE:
        xx = 0;
        yy = 0;
        ww = w - 1;
        hh = h;
        break;
      case HEIGHT_INCREASE:
        xx = 0;
        yy = h / 2;
        ww = w - 1;
        hh = 0;
        break;
      case WIDTH_DECREASE:
        xx = 0;
        yy = 0;
        ww = w - 1;
        hh = h - 1;
        break;
      case WIDTH_INCREASE:
        xx = w / 2;
        yy = 0;
        ww = 0;
        hh = h;
        break;
    }
    if (w >= h) {
      intersectionStep = w / numberOfSteps;
    }else{
      intersectionStep = h / numberOfSteps;
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
    switch (direction) {
      case HEIGHT_DECREASE:
        yy += intersectionStep;
        hh -= intersectionStep * 2;
        if (hh < 0) {
          return true;
        }
        break;
      case HEIGHT_INCREASE:
        yy -= intersectionStep;
        hh += intersectionStep * 2;
        if (hh > h) {
          return true;
        }
        break;
      case WIDTH_DECREASE:
        xx += intersectionStep;
        ww -= intersectionStep * 2;
        if (ww < 0) {
          return true;
        }
        break;
      case WIDTH_INCREASE:
        xx -= intersectionStep;
        ww += intersectionStep * 2;
        if (ww > w) {
          return true;
        }
    }
    return false;
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
    if ((direction == HEIGHT_INCREASE) || (direction == WIDTH_INCREASE)) {
      Rectangle rect = new Rectangle(xx, yy, ww, hh);
      g2.clip(rect);
    }else{
      GeneralPath p1 = new GeneralPath();
      if (direction == HEIGHT_DECREASE) {
        p1.append(new Rectangle(0, 0, ww, yy), false);
        p1.append(new Rectangle(0, yy + hh, ww, h - yy - hh), false);
      }else{
        if (direction == WIDTH_DECREASE) {
          p1.append(new Rectangle(0, 0, xx, hh), false);
          p1.append(new Rectangle(xx + ww, 0, w - xx - ww, hh), false);
        }
      }
      g2.clip(p1);
    }
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

  public void setDirection(int direction)
  {
    if (direction != HEIGHT_INCREASE && direction != HEIGHT_DECREASE &&
        direction != WIDTH_DECREASE && direction != WIDTH_INCREASE) {
      return ;
    }
    this.direction = direction;
  }

  public int getDirection()
  {
    return direction;
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
