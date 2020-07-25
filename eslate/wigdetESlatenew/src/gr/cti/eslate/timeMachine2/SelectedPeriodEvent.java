package gr.cti.eslate.timeMachine2;

import java.util.EventObject;

/**
 * Event triggered from changes at time machine. The event's getSource() method
 * will return the E-Slate handle of the media player.
 * 
 * @author augril
 */
public class SelectedPeriodEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Selected period's start/end year.
	 */
	private int yearStart, yearEnd;

	/**
	 * Constructs an event.
	 * 
	 * @param source
	 *            The source of the event.
	 * @param yearStart
	 *            Selected period's start year.
	 * @param yearEnd
	 *            Selected period's end year.
	 */
	public SelectedPeriodEvent(Object source, int yearStart, int yearEnd) {
		super(source);
		this.yearStart = yearStart;
		this.yearEnd = yearEnd;
	}

	/**
	 * Get selected period's start year.
	 * 
	 * @return Selected period's start year.
	 */
	int getYearStart() {
		return yearStart;
	}

	/**
	 * Get selected period's end year.
	 * 
	 * @return Selected period's end year.
	 */
	int getYearEnd() {
		return yearEnd;
	}
}
