package gr.cti.eslate.base.sharedObject;

/**
 * This interface must be implemented by components that have
 * input plugs, so that they may receive events signifying a change in the
 * shared object.
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface SharedObjectListener extends java.util.EventListener
{
  public void handleSharedObjectEvent(SharedObjectEvent soe);
}
