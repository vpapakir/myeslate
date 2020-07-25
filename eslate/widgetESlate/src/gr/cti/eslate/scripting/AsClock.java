package gr.cti.eslate.scripting;

import gr.cti.eslate.clock.*;
import java.util.Date;

/**
 * This interface describes the functionality of the Clock component
 * that is available to the Logo scripting mechanism.
 *
 * @author	Kriton Kyrimis
 * @version	1.5.17, 20-Oct-1999
 * @see	gr.cti.eslate.clock.Clock
 */

public interface AsClock {

    /**
     * Set the current time to a given value.
     * @param	time	The new time value.
     */
    public void setTime(TimeCount time);

    public void setTime(Date d);

    /**
     * Return the current time.
     * @return	The requested time.
     */
    public Date getTime();
}
