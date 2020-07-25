//Description: Scripting interface for an object representing a Scene
//Author: G.Birblis <birbilis@kagi.com>
//Version: 2Apr2001

package gr.cti.eslate.stage.models;

import gr.cti.eslate.stage.objects.*;
import java.awt.*;

public interface AsScene
 extends HasColor,
         HasImage
{

 public Image getPhoto(); //10May2000

 public SceneObject newSceneObject(String type) throws Exception;
 public void removeSceneObject(String name);
 public SceneObject cloneSceneObject(String name);

 public void refresh(); //1Jun1999
 public void refresh(Rectangle r); //1Jun1999
 public void setRefreshEnabled(boolean enabled); //1Jun1999
 public void clearScene();

 //public void setCoordinatesVisible(boolean visible);
 public void setControlPointsVisible(boolean visible);

 public void sendToBack(String o); //26Jul1999 //2Apr2001: this must be a String! e.g. Scene class (which implements AsScene) also has a sendToBack(Object) and we don't want to call that one!
 public void bringToFront(String o); //26Jul1999 //2Apr2001: this must be a String! e.g. Scene class (which implements AsScene) also has a bringToFront(Object) and we don't want to call that one!

 public void translate(double offsx,double offsy); //2Nov1999
 //public Rectangle2D getViewBounds();
 //public void setViewBounds(Rectangle2D bounds);
 public void setGridSize(double size);
 public double getGridSize();
 public void setGridVisible(boolean visible);
 public boolean isGridVisible();
 public void setMarksOverShapes(boolean over);
 public boolean isMarksOverShapes();
}
