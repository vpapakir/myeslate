package gr.cti.eslate.sharedObject;

import java.util.*;

import gr.cti.eslate.base.sharedObject.*;

/**
 * Time machine shared object.
 *
 * @author      Nicolas Drossos
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class TimeMachineSO extends SharedObject {
  private Date from,to, visibleStart, visibleEnd;
  private boolean isPeriodChanging = false;
  private boolean propagatePeriod = true;

  public TimeMachineSO(gr.cti.eslate.base.ESlateHandle baseESlate) {
    super(baseESlate);
    to=from=new Date();
  }

  @SuppressWarnings(value={"deprecation"})
  public void setTimePeriod(Date newfrom, Date newto, Vector path) {
    //System.out.println("setTimePeriod(\"" + newfrom + "\", \"" + newto +"\")");
    if (areDifferent(from, newfrom) || areDifferent(to, newto)) {
      from=newfrom;
      to=newto;
      // Create an event
      SharedObjectEvent soe = new SharedObjectEvent(this, path);

      fireSharedObjectChanged(soe);     // Notify the listeners
    }
  }

  public void setTimePeriod(Date newfrom, Date newto) {
    //System.out.println("setTimePeriod(\"" + newfrom + "\", \"" + newto +"\")");
    if (areDifferent(from, newfrom) || areDifferent(to, newto)) {
      from=newfrom;
      to=newto;

      // Create an event
      SharedObjectEvent soe = new SharedObjectEvent(this);

      fireSharedObjectChanged(soe);     // Notify the listeners
    }
  }


  public void setTimePeriod(Date newfrom, Date newto, boolean isChanging) {
    if (areDifferent(from, newfrom) || areDifferent(to, newto) || isChanging != isPeriodChanging) {
        isPeriodChanging = isChanging;
        from=newfrom;
        to=newto;

        // Create an event
        SharedObjectEvent soe = new SharedObjectEvent(this);

        fireSharedObjectChanged(soe);     // Notify the listeners
    }
  }

  public Date getFrom() {
    return from;
  }

  public Date getTo() {
    return to;
  }

  public void setSelectedPeriodFrom(Date start){
      if (areDifferent(from, start)) {
          from = start;
          SharedObjectEvent soe = new SharedObjectEvent(this);
          fireSharedObjectChanged(soe); // Notify the listeners
      }
  }

  public void setSelectedPeriodTo(Date end){
      if (areDifferent(to, end)) {
          to = end;
          SharedObjectEvent soe = new SharedObjectEvent(this);
          fireSharedObjectChanged(soe); // Notify the listeners
      }
  }

  public void setVisiblePeriodFrom(Date visibleStart){
      if (areDifferent(this.visibleStart, visibleStart)) {
          this.visibleStart = visibleStart;
          SharedObjectEvent soe = new SharedObjectEvent(this);
          fireSharedObjectChanged(soe); // Notify the listeners
      }
  }

  public Date getVisiblePeriodFrom(){
      return visibleStart;
  }

  public void setVisiblePeriodTo(Date visibleEnd){
      if (areDifferent(this.visibleEnd, visibleEnd)) {
          this.visibleEnd = visibleEnd;
          SharedObjectEvent soe = new SharedObjectEvent(this);
          fireSharedObjectChanged(soe); // Notify the listeners
      }
  }

  public Date getVisiblePeriodTo(){
      return visibleEnd;
  }

  public void setPropagatePeriod(boolean b){
      propagatePeriod = b;
  }

  public boolean isPropagatePeriod(){
      return propagatePeriod;
  }

  public void setPeriodChanging(boolean b){
      isPeriodChanging = b;
  }

  public boolean isPeriodChanging(){
      return isPeriodChanging;
  }
}
