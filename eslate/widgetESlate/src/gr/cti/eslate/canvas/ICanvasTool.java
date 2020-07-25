package gr.cti.eslate.canvas;

import java.awt.event.*;

import gr.cti.utils.Graphix;

public interface ICanvasTool extends MouseListener, MouseMotionListener {
 public void setComponent(CanvasPage c);
  //could use Component instead of JComponent
  //using this cause in debug I need paintImmediately in fill
 public void setGraphix(Graphix gx);
 public void reset();
 public String getToolName(); 
// public void setParamNames(String[] names); 
// public String[] getParamNames();
// public void setParamValues(double[] values);
// public double[] getParamValues();
}