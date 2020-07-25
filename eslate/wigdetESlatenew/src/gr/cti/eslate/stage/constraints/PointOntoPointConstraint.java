//2Dec1999 - localized

package gr.cti.eslate.stage.constraints;

import gr.cti.eslate.stage.constraints.base.AbstractMasterPointSlavePointConstraint;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;

public class PointOntoPointConstraint
 extends AbstractMasterPointSlavePointConstraint
{ //8Aug1999: changed ancestor

 static final long serialVersionUID = 4895797436571210424L; //8Jun2000: serial-version, so that new vers load OK

 public static String getTitle(){return MessagesBundle.localize("PointOntoPoint");} //29Jul1999

 protected void force(PropertyChangeEvent e){ //override ancestor's "force" method with a much faster implementation (the distance=0 case)
 //System.out.println("***");
 //Thread.dumpStack();
  getSlavePoint().setLocation2D(getMasterPoint().getLocation2D());
 }

 public boolean isValid(Point2D master, Point2D slave){ //8Aug1999:changed param order
  return slave.equals(master);
 };

}
