package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import virtuoso.logo.*; //LOGO//

/**
 * Logo machine shared object.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class MachineSO extends SharedObject
{
  MyMachine machine;

  public MachineSO(ESlateHandle app)    //18May1999: ESlateII
  {
    super(app);
    this.machine = null;
  }


  public void setMachine(MyMachine machine)
  {
    if (areDifferent(this.machine, machine)) {
      this.machine = machine;
      //18May1999: ESlateII
      //22May1999: using new SO constructor
      fireSharedObjectChanged(new SharedObjectEvent(this));
    }
  }


  public MyMachine getMachine()
  {
    return this.machine;
  }

}

