package gr.cti.eslate.clock;


import java.awt.*;
import java.util.*;
import javax.swing.*;
import gr.cti.eslate.utils.*;
import java.awt.image.*;
import java.text.*;

abstract class ClockHand {

    protected int baseX[], baseY[];
    protected int transX[], transY[];
    protected int numberOfPoints;

    public ClockHand(int originX, int originY, int length, int thickness, int points) {
        baseX = new int[points];
        baseY = new int[points];
        transX = new int[points];
        transY = new int[points];
        initiallizePoints(originX, originY, length, thickness);
        numberOfPoints = points;
    }

    abstract protected void initiallizePoints(int originX,
        int originY,
        int length,
        int thickness);

    abstract public void draw(Color color, double angle, Graphics g);

    protected void transform(double angle) {
        for (int i = 0; i < numberOfPoints; i++) {
            transX[i] = (int) ((baseX[0] - baseX[i]) * Math.cos(angle) -
                        (baseY[0] - baseY[i]) * Math.sin(angle) +
                        baseX[0]);

            transY[i] = (int) ((baseX[0] - baseX[i]) * Math.sin(angle) +
                        (baseY[0] - baseY[i]) * Math.cos(angle) +
                        baseY[0]);
        }
    }
}


class SweepHand extends ClockHand {

    public SweepHand(int originX, int originY, int length, int points) {
        super(originX, originY, length, 0, points);
    }

    protected void initiallizePoints(int originX, int originY, int length, int unused) {
        //unused = unused;  //We don't use the thickness parameter in this class
        //This comes from habit to prevent compiler warning
        //concerning unused arguments.

        baseX[0] = originX;
        baseY[0] = originY;
        baseX[1] = originX;
        baseY[1] = originY - length / 5;
        baseX[2] = originX;
        baseY[2] = originY + length;
    }

    public void draw(Color color, double angle, Graphics g) {
        transform(angle);
        g.setColor(color);
        g.drawLine(transX[1], transY[1], transX[2], transY[2]);
    }

    public void setParameters(int originX, int originY, int length, int points) {
        initiallizePoints(originX, originY, length, points);
    }

}


class HmHand extends ClockHand {

    public HmHand(int originX, int originY, int length, int thickness, int points) {
        super(originX, originY, length, thickness, points);
    }

    protected void initiallizePoints(int originX,
        int originY,
        int length,
        int thickness) {
        baseX[0] = originX;
        baseY[0] = originY;

        baseX[1] = baseX[0] - thickness / 2;
        baseY[1] = baseY[0] + thickness / 2;

        baseX[2] = baseX[1];
        baseY[2] = baseY[0] + length - thickness;

        baseX[3] = baseX[0];
        baseY[3] = baseY[0] + length;

        baseX[4] = baseX[0] + thickness / 2;
        baseY[4] = baseY[2];

        baseX[5] = baseX[4];
        baseY[5] = baseY[1];
    }

    public void draw(Color color, double angle, Graphics g) {
        transform(angle);
        g.setColor(color);
        g.fillPolygon(transX, transY, numberOfPoints);
    }

    public void setParameters(int originX, int originY, int length, int points) {
        initiallizePoints(originX, originY, length, points);
    }

    public Polygon getPolygon() {
        return new Polygon(transX, transY, numberOfPoints);
    }

}


public class AnalogClock extends JPanel {

	private static final long serialVersionUID = -7107014L;
	//some DEFINE'd constants
    static final int BACKGROUND = 0;              //Background image index
    static final int LOGO = 1;                    //Logo image index
    String text = "";//resources.getString("E-Slate");                      //Default text on clock face
    static final double MINSEC = 0.104719755;     //Radians per minute or second
    static final double HOUR = 0.523598776;       //Radians per hour
    private TimeCount currentGMT;
    protected ResourceBundle resources;
    Date date = new Date();
    GregorianCalendar c = new GregorianCalendar();
    String Month = "";
    protected String pattern = "dd-MM-yy";

    boolean countBackwards = false;
    boolean dateVisible = false;
    NewRestorableImageIcon backgroundImageIcon;

    //User options, see getParameterInfo(), below.
    JLabel face;
    int width = 700;
    int height = 700;
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    Color bgColor = new Color(192, 192, 192);
    Color faceColor = new Color(255, 255, 255);
    Color sweepColor = new Color(255, 0, 0);
    Color minuteColor = new Color (0, 0, 255);
    Color hourColor = new Color (0, 0, 255);
    Color textColor = new Color (0, 0, 0);
    Color caseColor = new Color (0, 0, 255);
    Color trimColor = new Color (192, 192, 192);
    boolean inHourHand = false;
    boolean inMinuteHand = false;
    boolean inSecondsHand = false;
    String logoString = null;
    boolean showNumbers = true;
    boolean showMinNums = false;
    Dimension d;
    int a = 0;
    int b = 0;

    Image images[] = new Image[2]; //Array to hold optional images

    boolean isPainted = false;

    //Center point of clock
    int x1, y1;

    //Array of points for triangular icon at 12:00
    int xPoints[] = new int[3], yPoints[] = new int[3];

    //Class to hold time, with method to return (double)(hours + minutes/60)
    InternalTime cur_time;

    //The clock's seconds, minutes, and hours hands.
    SweepHand sweep;
    HmHand  minuteHand,
        hourHand;

    //The last parameters used to draw the hands.
    double lastHour;
    int lastMinute, lastSecond;

    //The font used for text and date.
    Font font;

    //Offscreen image and device context, for buffered output.
    BufferedImage offScrImage;
    Graphics offScrGC;

    // Use to test background image, if any.
    MediaTracker tracker;

    int minDimension;   // Ensure a round clock if applet is not square.
    int originX;        // Upper left corner of a square enclosing the clock
    int originY;        // with respect to applet area.

    double tzDifference = 0;

    boolean localOnly = false;

    /*
     void showURLerror(Exception e){
     String errorMsg = "text URL error: "+e;
     showStatus(errorMsg);
     System.err.println(errorMsg);
     }
     */
    // This lets us create clocks of various sizes, but with the same
    // proportions.
    private int size(int percent) {
        return (int) ((double) percent / 100.0 * (double) minDimension);
    }

    
    /**
     * Constructs a new digital appearance of the Clock
     *
     */

    public AnalogClock() {
        tracker = new MediaTracker(this);
        setPreferredSize(new Dimension(200, 200));
        logoString = text;
        resources = ResourceBundle.getBundle("gr.cti.eslate.clock.ClockBundleMessages", Locale.getDefault());
        Month = resources.getString("Month" + c.get(GregorianCalendar.MONTH));
        currentGMT = new TimeCount(0, 0, 0);
        //    szImagesURL[BACKGROUND]  = getParameter("BGIMAGEURL");
        //    szImagesURL[LOGO] = getParameter("LOGOIMAGEURL");
        /*    for(int i=0; i<2; i++){
         if(szImagesURL[i] != null){
         try{
         imagesURL[i]=new URL(getCodeBase(),szImagesURL[i]);
         } catch (MalformedURLException e){
         showURLerror(e);
         imagesURL[i]=null;
         images[i]=null;
         }
         if(imagesURL[i] != null){
         showStatus("Javex loading image: " + imagesURL[i].toString());
         images[i]=getImage(imagesURL[i]);
         if(images[i] != null)
         tracker.addImage(images[i],i);
         showStatus("");
         }
         try{
         tracker.waitForAll(i);
         }catch (InterruptedException e){}
         }else images[i]=null;

         }      */

        cur_time = new InternalTime(0, 0, 0);
        lastHour = -1.0;
        lastMinute = -1;
        lastSecond = -1;

        //setSize(d);
        x1 = this.getWidth() / 2;
        y1 = this.getHeight() / 2;
        minDimension = Math.min(this.getWidth(), this.getHeight());
        originX = (this.getWidth() - minDimension) / 2;
        originY = (this.getHeight() - minDimension) / 2;

        xPoints[1] = x1 - size(3);
        xPoints[2] = x1 + size(3);
        xPoints[0] = x1;
        yPoints[1] = y1 - size(38);
        yPoints[2] = y1 - size(38);
        yPoints[0] = y1 - size(27);

        sweep = new SweepHand(x1, y1, size(40), 3);
        minuteHand = new HmHand(x1, y1, size(40), size(6), 6);
        hourHand = new HmHand(x1, y1, size(25), size(8), 6);

        font = new Font("TXT", Font.PLAIN, size(10));
    }

    private void drawHands(Graphics g, int minDimension, int x1, int y1) {

        //System.out.println("x1: "+x1+", size(3): "+size(3)+", y1: "+y1+", size(38): "+size(38));
        xPoints[1] = x1 - size(3);
        xPoints[2] = x1 + size(3);
        xPoints[0] = x1;
        yPoints[1] = y1 - size(38);
        yPoints[2] = y1 - size(38);
        yPoints[0] = y1 - size(27);

        sweep.setParameters(x1, y1, size(40), 3);
        minuteHand.setParameters(x1, y1, size(40), size(6));
        hourHand.setParameters(x1, y1, size(25), size(8));

        float size = minDimension / 10;
        Font newfont = font.deriveFont(size);
        Font newfont2 = font.deriveFont(size / 2);
        Font newfont3 = font.deriveFont(size / 3);

        double angle;
        int i, j;
        int x, y;

        g.setColor(textColor);

        // Here hour points get drawn
        g.setFont(newfont);
        FontMetrics fm = g.getFontMetrics();

        if (showNumbers) {
            g.drawString("12", xPoints[1] - size(3), yPoints[1] + 3 * fm.getHeight() / 4);
            g.drawString("6", x1 - size(3), originY + minDimension - size(22) + 3 * fm.getHeight() / 4);
            g.drawString("9", originX + size(12), y1 - size(2) + fm.getHeight() / 2);
            g.drawString("3", originX + minDimension - size(18), y1 - size(2) + fm.getHeight() / 2);
            for (i = 1; i < 12; i += 3)
                for (j = i; j < i + 2; j++) {
                    x = (int) (x1 + Math.sin(HOUR * j) * size(35));
                    y = (int) (y1 - Math.cos(HOUR * j) * size(35));
                    if (j >= 4 & j <= 8)
                        g.drawString((new Integer(j)).toString(), x - size(3), y - size(3) + fm.getHeight() / 2);
                    else {
                        if (j == 10)
                            g.drawString((new Integer(j)).toString(), x - size(4), y - size(3) + 3 * fm.getHeight() / 4);
                        else if (j == 11)
                            g.drawString((new Integer(j)).toString(), x - size(5), y - size(3) + 3 * fm.getHeight() / 4);
                        else
                            g.drawString((new Integer(j)).toString(), x - size(3), y - size(3) + 3 * fm.getHeight() / 4);
                    }
                }
        } else {
            g.fillRect(originX + size(12), y1 - size(2), size(10), size(4));
            g.fillRect(originX + minDimension - size(22), y1 - size(2), size(10), size(4)); /*Three o clock rect*/
            g.fillRect(x1 - size(2), originY + minDimension - size(22), size(4), size(10));
            g.fillPolygon(xPoints, yPoints, 3);
            for (i = 1; i < 12; i += 3)
                for (j = i; j < i + 2; j++) {
                    x = (int) (x1 + Math.sin(HOUR * j) * size(35));
                    y = (int) (y1 - Math.cos(HOUR * j) * size(35));
                    g.fillOval(x - size(3), y - size(3), size(6), size(6));
                }
        }

        //Paint our logo...
        if (x1 - fm.stringWidth(logoString) / 2 > 0 && y1 - size(12) > 0)
            g.drawString(logoString, x1 - fm.stringWidth(logoString) / 2, y1 - size(12));
        if (dateVisible) {
            g.setFont(newfont2);
            fm = g.getFontMetrics();
            if (pattern.equals("full")) {
                pattern = "EEEEEEEEE, d '" + Month + "' yyyy";
                g.setFont(newfont3);
                dateFormat.applyPattern(pattern);
                g.drawString(dateFormat.format(date), x1 - fm.stringWidth(dateFormat.format(date)) / 2, y1 + size(16));
            } else {
                dateFormat.applyPattern(pattern);
                g.drawString(dateFormat.format(date), x1 - fm.stringWidth(dateFormat.format(date)) / 2, y1 + size(16));
            }
        }
        g.setFont(newfont2);
        fm = g.getFontMetrics();
        if (cur_time.hours <= 23 && cur_time.hours >= 12) {
            if (x1 - fm.stringWidth(resources.getString("PM")) / 2 > 0)
                g.drawString(resources.getString("PM"), x1 - fm.stringWidth(resources.getString("PM")) / 2, y1 + size(24));
        } else {
            if (x1 - fm.stringWidth(resources.getString("AM")) / 2 > 0)
                g.drawString(resources.getString("AM"), x1 - fm.stringWidth(resources.getString("AM")) / 2, y1 + size(24));
        }
        //Get the day of the month...
        //        String day=Integer.toString(cur_time.getDate(),10);

        //Paint it...
        //        g.drawString(   day,
        //                        originX + minDimension-size(14)-fm.stringWidth(day),
        //                        y1+size(5));

        //and put a box around it.
        //        g.drawRect( originX + minDimension-size(14)-fm.stringWidth(day)-size(2),
        //                    y1-size(5)-size(2),
        //                    fm.stringWidth(day)+size(4),
        //                    size(10)+size(4));

        /*        if(cur_time.getHours()>7 && cur_time.getHours()<20){
         x = originX + (minDimension-dayIcon.getImage().getWidth(this))/2;
         y = y1 + (minDimension/2 - size(22) -dayIcon.getImage().getHeight(this))/2;
         if(x > 0 && y > 0)
         offScrGC.drawImage(dayIcon.getImage(), x1 + size(14)+ size(2),
         y1-size(5),size(10),size(10), this);
         }else{
         x = originX + (minDimension-nightIcon.getImage().getWidth(this))/2;
         y = y1 + (minDimension/2 - size(22) -nightIcon.getImage().getHeight(this))/2;
         if(x > 0 && y > 0)
         offScrGC.drawImage(nightIcon.getImage(), x1+size(14)+size(2),
         y1-size(5),size(10),size(10), this);
         }
         */
        //        if(cur_time.getMinutes() != lastMinute){
        //           minuteHand.draw(minuteColor,MINSEC*lastMinute,g);
        //           minutesAlreadyDrawn = true;
        //            if(cur_time.get_hours() != lastHour)
        //                 hourHand.draw(hourColor,HOUR*lastHour,g);
        //        }

        if (!countBackwards) {
            lastHour = cur_time.get_hours();
            hourHand.draw(hourColor, HOUR * lastHour, g);

            lastMinute = cur_time.minutes;
            minuteHand.draw(minuteColor, MINSEC * lastMinute, g);

            g.setColor(minuteColor);
            g.fillOval(x1 - size(4), y1 - size(4), size(8), size(8));
            g.setColor(sweepColor);
            g.fillOval(x1 - size(3), y1 - size(3), size(6), size(6));

            lastSecond = cur_time.sec;
            angle = MINSEC * lastSecond;
            sweep.draw(sweepColor, angle, g);
        }/*else{
         lastHour=12 - cur_time.get_hours();
         hourHand.draw(hourColor,HOUR*lastHour,g);


         lastMinute=60 - cur_time.getMinutes();
         minuteHand.draw(minuteColor,MINSEC*lastMinute,g);

         g.setColor(minuteColor);
         g.fillOval(x1-size(4),y1-size(4),size(8),size(8));
         g.setColor(sweepColor);
         g.fillOval(x1-size(3),y1-size(3),size(6),size(6));

         lastSecond=60 - cur_time.getSeconds();
         angle=MINSEC*lastSecond;
         sweep.draw(sweepColor, angle,g);
         }  */

        g.setColor(trimColor);
        g.fillOval(x1 - size(1), y1 - size(1), size(2), size(2));
    }

    /**
     * Paints the analog clock
     *
     */

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        boolean imageExists = false;

        minDimension = Math.min(this.getWidth(), this.getHeight());
        if (minDimension < 15)
            return;
        originX = (this.getWidth() - minDimension) / 2;
        originY = (this.getHeight() - minDimension) / 2;
        x1 = minDimension / 2;
        y1 = minDimension / 2;
        int i, x0, y0, x2, y2;

        if (offScrImage == null || a != this.getWidth() || b != this.getHeight()) {
            offScrImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            a = this.getWidth();
            b = this.getHeight();
        }

        offScrGC = offScrImage.getGraphics();
        Graphics2D offScrGC2 = (Graphics2D) offScrGC;

        //        Graphics2D offScrGC2=(Graphics2D)offScrImage.getGraphics();
        offScrGC2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImageIcon == null) {
            if (!isOpaque()) {
                offScrGC2.setBackground(new Color(255, 0, 0, 0));
                offScrGC2.clearRect(0, 0, this.getWidth(), this.getHeight());
            } else {
                offScrGC2.setColor(bgColor);
                offScrGC2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
        } else {
            //            System.out.println("Drawing image");
            Image backgroundImage = backgroundImageIcon.getImage();

            offScrGC2.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
            imageExists = true;
        }
        if (!imageExists) {
            //        System.out.println("not image Exists");

            offScrGC2.setColor(caseColor);
            //Shrink one pixel so we don't clip anything off...
            //        offScrGC2.setStroke(new BasicStroke(0.05f*minDimension));
            offScrGC2.fillOval(originX + 1,
                originY + 1,
                minDimension - 2,
                minDimension - 2);
            //        offScrGC2.setStroke(new BasicStroke(0.005f*minDimension));
            offScrGC2.setColor(faceColor);

            offScrGC2.fillOval(originX + size(5),
                originY + size(5),
                minDimension - size(10),
                minDimension - size(10));

            offScrGC2.setColor(trimColor);
            offScrGC2.drawOval(originX + 1,
                originY + 1,
                minDimension - 2,
                minDimension - 2);

            offScrGC2.drawOval(originX + size(5),
                originY + size(5),
                minDimension - size(10),
                minDimension - size(10));

            offScrGC2.setColor(textColor);

            float size = minDimension / 40;
            Font secfont = font.deriveFont(size);

            offScrGC2.setFont(secfont);
            //Draw minute numbers!
            if (showMinNums) {

                /*for(i=1;i<61;i++){
                 x0=0;
                 if(i>=1 &&i <= 29)
                 x0=(int)(originX + x1+size(44)*Math.cos(MINSEC*i-Math.PI/2) - fm.getHeight()/2 - size(1));
                 else if(i<=58 &&i >= 31)
                 x0=(int)(originX + x1+size(44)*Math.cos(MINSEC*i-Math.PI/2) - fm.getHeight()/2 + size(1));
                 else
                 x0=(int)(originX + x1+size(44)*Math.cos(MINSEC*i-Math.PI/2) - fm.getHeight()/2);
                 if (i<15 || i>45)
                 y0=(int)(originY + y1+size(44)*Math.sin(MINSEC*i-Math.PI/2) + fm.getHeight()/2);
                 else
                 y0=(int)(originY + y1+size(44)*Math.sin(MINSEC*i-Math.PI/2));
                 if (i==15 || i==45)
                 y0=(int)(originY + y1+size(44)*Math.sin(MINSEC*i-Math.PI/2) + fm.getHeight()/4);
                 if (i<10)
                 offScrGC2.drawString(" "+i,x0,y0);
                 else
                 offScrGC2.drawString(""+i,x0,y0);
                 } */

                for (i = 0; i < 60; i++) {
                    x0 = (int) (originX + x1 + size(42) * Math.sin(MINSEC * i));
                    y0 = (int) (originY + y1 + size(42) * Math.cos(MINSEC * i));
                    x2 = (int) (originX + x1 + size(44) * Math.sin(MINSEC * i));
                    y2 = (int) (originY + y1 + size(44) * Math.cos(MINSEC * i));
                    offScrGC2.drawLine(x0, y0, x2, y2);
                }
                String toBeShown = "";

                for (i = 1; i < 61; i++) {
                    if (i < 10)
                        toBeShown = " " + i;
                    else
                        toBeShown = "" + i;
                    if (i >= 1 && i <= 30)
                        x0 = (int) (originX + x1 + size(40) * Math.cos(MINSEC * i - Math.PI / 2) - size(2));
                    else
                        x0 = (int) (originX + x1 + size(40) * Math.cos(MINSEC * i - Math.PI / 2) - size(1));
                    if (i >= 45 && i <= 15)
                        y0 = (int) (originY + y1 + size(40) * Math.sin(MINSEC * i - Math.PI / 2) + size(1));
                    else
                        y0 = (int) (originY + y1 + size(40) * Math.sin(MINSEC * i - Math.PI / 2) + size(1));
                    offScrGC2.drawString(toBeShown, x0, y0);
                }
            } else {
                //Draw graduations, a longer index every fifth mark...
                for (i = 0; i < 60; i++) {
                    if (i == 0 || (i >= 5 && i % 5 == 0)) {
                        x0 = (int) (originX + x1 + size(40) * Math.sin(MINSEC * i));
                        y0 = (int) (originY + y1 + size(40) * Math.cos(MINSEC * i));
                    } else {
                        x0 = (int) (originX + x1 + size(42) * Math.sin(MINSEC * i));
                        y0 = (int) (originY + y1 + size(42) * Math.cos(MINSEC * i));
                    }
                    x2 = (int) (originX + x1 + size(44) * Math.sin(MINSEC * i));
                    y2 = (int) (originY + y1 + size(44) * Math.cos(MINSEC * i));
                    offScrGC2.drawLine(x0, y0, x2, y2);
                }
            }
        }

        drawHands(offScrGC2, minDimension, originX + x1, originY + y1);
        g2.drawImage(offScrImage, 0, 0, this);

        offScrGC2.dispose();
        offScrGC2 = null;

        isPainted = true;
    }

    /*    public synchronized void update(Graphics g){
     offScrImage = (BufferedImage) createImage(width,height);
     System.out.println("update offScrImage : "+ offScrImage);
     offScrGC = offScrImage.getGraphics();
     if(!isPainted)
     paint(g);
     else{
     drawHands(offScrGC,minDimension,x1,y1);
     g.drawImage(offScrImage,0,0,this);
     }
     }
     */

    /**
     * Sets clock's time
     * @param	time The time
     */

    public void setTime(TimeCount time) {
        setTime(time.hour, time.min, time.sec);
    }

    public void setTime(int hour, int min, int sec ) {
        synchronized (currentGMT) {
            cur_time.hours=hour;
            cur_time.minutes = min;
            cur_time.sec = sec;
            repaint();
            revalidate();
        }
    }

    /**
     * Returns mouse click distance from sweephand
     */

    protected double dist(Point p, int x0, int y0, int x1, int y1) {
        double t;

        if (Math.abs(x0 - x1) <= 1.5)
            t = Math.abs(p.x - x0);
        else if (Math.abs(y0 - y1) <= 1.5)
            t = Math.abs(p.y - y0);
        else {
            t = Double.MAX_VALUE;
            int a = y0 - y1;
            int b = x1 - x0;
            int c = x0 * y1 - x1 * y0;
            int ar = a * p.x + b * p.y + c;
            double pa = Math.sqrt(a * a + b * b);

            //            Double d= new Double(ar);
            t = ar / pa;
        }

        return t;
    }

    private TimeCount copyTime(InternalTime time) {
        TimeCount timeCount = new TimeCount(0, 0, 0);

        timeCount.hour = time.hours;
        timeCount.min = time.minutes;
        timeCount.sec = time.sec;
        return timeCount;
    }

    /**
     * Sets clock's date
     * @param	date The date
     */

    public void setDate(Date date) {
        this.date = date;
        GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(date);
        Month = resources.getString("Month" + calendar.get(Calendar.MONTH));
        //     System.out.println("setDate in analog and month: " + Month);
    }
}
