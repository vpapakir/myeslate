package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.logo.ProcedureCall;

/**
 * Logo call shared object.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class LogoCallSO extends SharedObject
{
  ProcedureCall call = null;
  public Integer executeAtForcedClock = null;

  public static final int NEW_CALL = 1;

  public LogoCallSO(ESlateHandle app)   //18May1999: ESlateII
  {
    super(app);
  }

  public synchronized void set_call(ProcedureCall call) //Synchronized!!!
  {
    if (areDifferent(this.call, call)) {
      this.call = call;
      synchronized(this) { //12-7-98: !!!
        if (executeAtForcedClock != null) {
          // Get the clock value before setting executeAtForcedClock to null.
          int clock = executeAtForcedClock.intValue();
          //unlock clock here (don't do in LOGO, cause exec was in a thread)   
          executeAtForcedClock=null;
          // Use explicit clock value if execution is forced at that time check
          // call for null: was firing exception when Logo-Slider double
          // connection.
          if (call != null) {
            this.call.time = clock;
          }
          //System.out.println("LogoCallSO: executing at forced clock="+clock);
        }
      }

      //18May1999: ESlateII
      //22May1999: using new SO constructor
      fireSharedObjectChanged(new SharedObjectEvent(this,NEW_CALL));
    }
  }

  public ProcedureCall get_call()
  {
    return this.call;
  }

}
