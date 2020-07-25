//Version: 29Jun1999

package gr.cti.eslate.stage.models;

import java.awt.geom.Point2D;

public interface HasLocation2D
{
 public Point2D getLocation2D(); //29Jun1999: suffixed with "2D", cause there is a method of java.awt.Component called "getLocation"
 public void setLocation2D(Point2D value); //29Jun1999: suffixed with "2D", cause there is a method of java.awt.Component called "setLocation"
}

