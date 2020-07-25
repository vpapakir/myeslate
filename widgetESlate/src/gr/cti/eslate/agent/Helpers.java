package gr.cti.eslate.agent;

import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

public class Helpers {
    /**
     * Normalizes the given angle (in degrees) in the interval [0,360).
     */
    public static double normAngle(double angle) {
        if (!valid(angle)) {
            if (angle<0)
                for (;!valid(angle);)
                    angle+=360;
            else
                for (;!valid(angle);)
                    angle-=360;
        }
        return angle;
    }
    /**
     * Normalizes the given angle (in degrees) in the interval (-180,180].
     */
    public static double normAngleMinusPiPi(double angle) {
        if (!validMinusPiPi(angle)) {
            if (angle<0)
                for (;!validMinusPiPi(angle);)
                    angle+=360;
            else
                for (;!validMinusPiPi(angle);)
                    angle-=360;
        }
        return angle;
    }
    /**
     * Converts an angle from agent-tilt to trigonometrical angle in [0,2�).
     */
    public static double trigAngle(double angle) {
        angle+=90;
        return normAngle(angle)*Math.PI/180;
    }
    /**
     * Converts a trigonometrical angle to agent-tilt angle in [0,360).
     */
    public static double tiltAngle(double angle) {
        angle=(angle-Math.PI/2)*180/Math.PI;
        return normAngle(angle);
    }
    /**
     * @return  The azimuth from x1,y1 to x2,y2 while in cartesian space.
     */
    public static double cartesianAzimuth(double x1,double y1,double x2,double y2) {
        double dx=x2-x1;
        double dy=y2-y1;
        double a;
        if (Math.abs(dx)>1E-12) {
            a=Math.atan(dy/dx);
            if (dx<0)
                a+=Math.PI/2;
            else
                a+=3*Math.PI/2;
            return a;
        } else {
            if (dy>=0)
                return 0;
            else
                return Math.PI;
        }
    }

    private static boolean valid(double angle) {
        if (angle>=0 && angle<360)
            return true;
        return false;
    }
    private static boolean validMinusPiPi(double angle) {
        if (angle>-180 && angle<=180)
            return true;
        return false;
    }
    public static Icon[] producePhases(JComponent comp,Icon baseIcon,int phases) {
        NewRestorableImageIcon[] icons=new NewRestorableImageIcon[phases];
        //Produce all the rotated images
        double angleStep=360d/phases;
        double startAngle=-(angleStep/2);
        if (phases==1) {
            BufferedImage bi=new BufferedImage(baseIcon.getIconWidth(),baseIcon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
            baseIcon.paintIcon(comp,bi.getGraphics(),0,0);
            icons[0]=new NewRestorableImageIcon(bi);
        } else {
            for (int i=0;i<phases;i++) {
                BufferedImage bi; int w,h,tLx,tLy,bRx,bRy,tRx,tRy,bLx,bLy; double rotation; double cos,sin;
                w=baseIcon.getIconWidth();
                h=baseIcon.getIconHeight();
                //The rotated image needs a greater rectangle. Calculate it.
                rotation=(360-startAngle-angleStep/2)*Math.PI/180;
                cos=Math.cos(rotation);
                sin=Math.sin(rotation);
                tLx=(int) (w/2*(1-cos)+h/2*sin);
                tLy=(int) (h/2*(1-cos)-w/2*sin);
                bRx=(int) (cos*w-sin*h+w/2*(1-cos)+h/2*sin);
                bRy=(int) (sin*w+cos*h+h/2*(1-cos)-w/2*sin);
                tRx=(int) (cos*w+w/2*(1-cos)+h/2*sin);
                tRy=(int) (sin*w+h/2*(1-cos)-w/2*sin);
                bLx=(int) (-sin*h+w/2*(1-cos)+h/2*sin);
                bLy=(int) (cos*h+h/2*(1-cos)-w/2*sin);
                //Add 2 to the values for truncation problems
                w=Math.max(Math.max(Math.max(tLx,tRx),bLx),bRx)-Math.min(Math.min(Math.min(tLx,tRx),bLx),bRx);
                h=Math.max(Math.max(Math.max(tLy,tRy),bLy),bRy)-Math.min(Math.min(Math.min(tLy,tRy),bLy),bRy);

                //Now paint the rotated image
                bi=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2=(Graphics2D) bi.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
                g2.setTransform(AffineTransform.getRotateInstance(rotation,w/2,h/2));
                baseIcon.paintIcon(comp,g2,(w-baseIcon.getIconWidth())/2,(h-baseIcon.getIconHeight())/2);
                icons[i]=new NewRestorableImageIcon(bi);
                startAngle+=angleStep;
            }
        }
        return icons;
    }

    protected static Cursor normalCursor=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    protected static Cursor waitCursor=Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
}
