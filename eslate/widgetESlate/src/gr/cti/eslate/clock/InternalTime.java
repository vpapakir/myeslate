package gr.cti.eslate.clock;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class InternalTime {

		private static final long serialVersionUID = 1461075182495838198L;
		
		protected int hours,minutes,sec;
		long time;
		Calendar c = GregorianCalendar.getInstance();
		
		public InternalTime(double localOffset) {
	        long tzOffset = (c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)) / (60 * 1000);

	        localOffset *= 3600000.0;
	        time = time + tzOffset + (long) localOffset;
	    }

	    public InternalTime(int hours, int minutes, int sec) {
	        this.hours = hours;
	        this.minutes = minutes;
	        this.sec = sec;
	    }

	    public InternalTime() {
	    }

	    public double get_hours() {
	        return (double) hours + (double) minutes / 60.0;
	    }
	}

