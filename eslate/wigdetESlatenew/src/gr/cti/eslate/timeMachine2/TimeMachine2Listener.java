package gr.cti.eslate.timeMachine2;

import java.util.EventListener;

/**
 * The listener interface for receiving events about changes at time machine.
 * 
 * @author augril
 */
public interface TimeMachine2Listener extends EventListener {
	/**
	 * Invoked when scale changes.
	 * 
	 * @param e
	 *            The media player event.
	 */
	public void scaleChanged(ScaleEvent e);

	/**
	 * Invoked when selected period changes.
	 * 
	 * @param e
	 *            The media player event.
	 */
	public void selectedPeriodChanged(SelectedPeriodEvent e);
}
