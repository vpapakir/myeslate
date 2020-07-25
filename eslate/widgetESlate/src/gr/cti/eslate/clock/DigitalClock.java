package gr.cti.eslate.clock;


import java.awt.*;
import java.util.*;
import javax.swing.*;


public class DigitalClock extends JPanel {

	private static final long serialVersionUID = 1L;
	JLabel face;
    private TimeCount currentGMT;
    boolean countBackwards = false;
    JPanel panel;
    Date date;
    

    /**
     * Constructs a new digital appearance of the Clock
     *
     */

    public DigitalClock() {
        face = new JLabel();
        Font boldFont = new Font("SansSerif", Font.BOLD, 20);

        panel = new JPanel(new BorderLayout());
        //      panel.setBorder(BorderFactory.createRaisedBevelBorder());
        face.setFont(boldFont);

        Box row = Box.createHorizontalBox();
        Box col = Box.createVerticalBox();

        col.add(Box.createVerticalGlue());
        row.add(Box.createHorizontalGlue());
        row.add(face);
        row.add(Box.createHorizontalGlue());
        col.add(row);
        col.add(Box.createVerticalGlue());
        panel.add(col, BorderLayout.CENTER);
        currentGMT = new TimeCount(0, 0, 0);
        setDisplay(0, 0, 0);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    /**
     * Sets the digital display reeding
     * @param h The hours
     * @param m The minutes
     * @param s The seconds
     */

    public void setDisplay(int h, int m, int s) {
        face.setText(twoDigits(h) + ":" + twoDigits(m) + ":" + twoDigits(s));
    }

    private static String twoDigits(int x) {
        if (x < 10) {
            return "0" + Integer.toString(x);
        } else {
            return Integer.toString(x);
        }
    }

    /**
     * Sets clock's time
     * @param	time The time
     */

    public void setTime(TimeCount time) {
        setTime(time.hour, time.min, time.sec);
    }

    public void setTime(int hour, int min, int sec) {
        synchronized (currentGMT) {
            currentGMT.hour = hour;
            currentGMT.min = min;
            currentGMT.sec = sec;
            //currentGMT.usec = time.usec;
            if (!countBackwards)
                setDisplay(currentGMT.hour, currentGMT.min, currentGMT.sec);
            else
                setDisplay(12 - currentGMT.hour, 60 - currentGMT.min, 60 - currentGMT.sec);
        }
    }



    /**
     * Sets clock's date
     * @param d The clock date
     * Notice : Only date values are used, not time.
     * For example if d is 31/1/2000 12:43:10 then only 31/1/2000 will be used
     */

    public void setDate(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(date);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, GregorianCalendar.MINUTE, GregorianCalendar.SECOND, currentGMT.hour, currentGMT.min, currentGMT.sec);
        this.date = calendar.getTime();
    }
}
