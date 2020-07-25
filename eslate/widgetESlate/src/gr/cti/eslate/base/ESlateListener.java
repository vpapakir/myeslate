package gr.cti.eslate.base;

import java.util.*;

/**
 * The listener interface for receiving events from E-Slate.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ESlateListener extends EventListener
{
  /**
   * Invoked when a component is successfully renamed.
   */
  public void componentNameChanged(ComponentNameChangedEvent e);

  /**
   * Invoked immediately before an E-Slate handle is disposed.
   */
  public void disposingHandle(HandleDisposalEvent e);

  /**
   * Invoked immediately after an E-Slate handle is disposed.
   */
  public void handleDisposed(HandleDisposalEvent e);

  /**
   * Invoked when a component's parent has changed.
   */
  public void parentChanged(ParentChangedEvent e);
}
