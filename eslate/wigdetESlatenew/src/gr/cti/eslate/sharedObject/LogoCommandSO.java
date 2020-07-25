package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;

/**
 * Logo command shared object.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class LogoCommandSO extends SharedObject
{
  String command; //the LOGO command (maybe use StringList?)
  int execAtTopLevelClock;

  public static final int NEW_COMMAND = 1;

  public LogoCommandSO(ESlateHandle app)        //18May1999: ESlateII
  {
    super(app);
    this.command = null;
    this.execAtTopLevelClock = 0;
  }

  public void setCommand(String command, int topLevelClock)
  {
    if (areDifferent(this.command, command)) {
      this.command = command;
      this.execAtTopLevelClock = topLevelClock;
      //18May1999: ESlateII
      //22May1999: using new SO constructor
      fireSharedObjectChanged(new SharedObjectEvent(this, NEW_COMMAND));
    }
  }

  public String getCommand()
  {
    return this.command;
  }

  public int getTopLevelClock()
  {
    return this.execAtTopLevelClock;
  }

}
