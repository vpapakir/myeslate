package gr.cti.eslate.base;

/**
 * Event triggered immediately before and after an E-Slate handle is disposed.
 * The getSource() method will return the disposed E-Slate handle.
 * <P>
 * In the first case, an ESlateListener is given the opportunity to veto the
 * disposal and to inform the container that their state has changed.
 * <UL>
 * <LI>
 * If <code>cancellationRespected</code> is true, then the disposingHandle
 * method of an ESlateListener is allowed to veto the disposal.
 * </LI>
 * <LI>
 * This vetoing is done by setting <code>vetoDisposal</code> to true.
 * </LI>
 * <LI>
 * By setting <code>stateChanged</code> to true, the disposingHandle method of
 * an ESlateListener can indicate to the container that the component's state
 * has changed, so that the container may display an appropriate notification
 * to the user.
 * </LI>
 * <LI>
 * To perform different actions depending on whether the entire microworld is
 * being destroyed or just this particular component, ESLateListeners can
 * check the value of <code>microworldState</code>
 * </LI>
 * </UL>
 * <P>
 * The E-Slate container guarantees that:
 * <UL>
 * <LI>
 * Before disposing a single component (as opposed to disposing the component
 * as part of closing the entire microworld), a HandleDisposalEvent having
 * <code>cancellationRespected = true</code> is generated, and the
 * disposingHandle method of all registered ESlateListeners is called. If at
 * least one listener sets <code>vetoDisposal</code> to true, the disposal is
 * cancelled.
 * </LI>
 * <LI>
 * When closing an entire microworld, the disposingHandle method of all
 * ESlateListeners registered to all handles is called before disposing of any
 * handles. The value of <code>cancellationRespected</code> for these events
 * will be false. If any of these methods sets <code>stateChanged</code> to
 * true, then the E-Slate container will ask the user whether they want to
 * store the microworld before closing it, and save the microworld if the user
 * chooses to do so. Then, the handles are disposed, without any further
 * notification.
 * </LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class HandleDisposalEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Indicates that the container will honor the request of the
   * disposingHandle method of an ESlateListener to cancel the handle's
   * disposal.
   */
  public boolean cancellationRespected;
  /**
   * The disposingHandle method of an ESlateListener can set this variable to
   * true to indicate that the component's state has changed.
   */
  public boolean stateChanged;
  /**
   * The disposingHandle method of an ESlateListener can set this variable to
   * true to request that the disposal of the handle is cancelled. This
   * variable is ignored if <code>cancellationRespected</code> is false.
   */
  public boolean vetoDisposal;
  /**
   * The current state of the microworld. This should be one of
   * ESlateMicroworld.RUNNING, ESlateMicroworld.CLOSING, or
   * ESlateMicroworld.DEAD. Other possible
   * values, but which which should not appear in this context, are
   * ESlateMicroworld.LOADING, and ESlateMicroworld.SAVING.
   */
  public int microworldState;

  /**
   * Constructs an E-Slate handle disposal  event.
   * @param     handle  The disposed E-Slate handle.
   */
  public HandleDisposalEvent(ESlateHandle handle)
  {
    this(handle, false);
  }

  /**
   * Constructs an E-Slate handle disposal  event.
   * @param     handle  The disposed E-Slate handle.
   * @param     cancellationRespected   Indicates that the container will
   *                    honor the request of the disposingHandle method of
   *                    an ESlateListener to cancel the handle's disposal.
   */
  public HandleDisposalEvent(ESlateHandle handle,
                             boolean cancellationRespected)
  {
    super(handle);
    ESlateMicroworld mw = handle.getESlateMicroworld();
    if (mw != null) {
      microworldState = mw.getState();
    }else{
      microworldState = ESlateMicroworld.DEAD;
    }
    this.cancellationRespected = cancellationRespected;
    stateChanged = false;
    vetoDisposal = false;
  }

}
