package gr.cti.eslate.agent;

import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.Icon;

class AgentFace {
    AgentFace(Agent agent,int numberOfFaces) {
        this.agent=agent;
        faces=new Interval[numberOfFaces];
    }

    /**
     * @param angle In degrees.
     */
    Icon get(double angle) {
        for (int i=0;i<faces.length;i++) {
            //Handle the anomaly around 0
            if (faces[i].from<=faces[i].to) {
                if (faces[i].from<=angle && faces[i].to>angle)
                    return faces[i].ic;
            } else {
                if ((angle>=faces[i].from && angle<=360) || (angle<=faces[i].to && angle>=0))
                    return faces[i].ic;
            }
        }
        return null;
    }
    /**
     * Adds an interval and the associated, after normalizing the angles in [0,360).
     * @param f "From" in degrees.
     * @param t "To" in degrees.
     */
    void add(double f,double t,Icon ic) {
        faces[requestIndex()]=new Interval(Helpers.normAngle(f),Helpers.normAngle(t),ic);
    }
    /**
     * Sets an existing interval and the associated, after normalizing the angles in [0,360).
     * @param f "From" in degrees.
     * @param t "To" in degrees.
     */
    void set(double f,double t,Icon ic) {
        double from=Helpers.normAngle(f);
        double to=Helpers.normAngle(t);
        boolean found=false;
        for (int i=0;i<faces.length;i++)
            if (faces[i]!=null && faces[i].from==from && faces[i].to==to) {
                faces[i].ic=ic;
                found=true;
            }
        if (!found)
            add(f,t,ic);
    }
    /**
     * @return The index of a null position in the array.
     */
    private int requestIndex() {
        //Find a null position
        for (int i=0;i<faces.length;i++)
            if (faces[i]==null)
                return i;
        throw new RuntimeException("AGENT#200005091725: Cannot add an icon face to the Agent. No space available.");
    }
    /**
     * Clears the array.
     */
    protected void clearAll() {
        for (int i=0;i<faces.length;i++)
            faces[i]=null;
    }
    /**
     * Changes the size of the array.
     */
    protected void setCapacity(int c) {
        if (c<0)
            return;
        Interval[] tmp=faces;
        faces=new Interval[c];
        System.arraycopy(tmp,0,faces,0,Math.min(tmp.length,c));
    }
    /**
     * Returns the capacity of the structure.
     */
    protected int getCapacity() {
        return faces.length;
    }

    class Interval implements Serializable {
        double from;
        double to;
        Icon ic;
        private static final long serialVersionUID=4576350953430974924L;//19991113L;

        Interval() {
            from=Double.MAX_VALUE;
            to=-from;
            ic=null;
        }

        Interval(double from,double to,Icon ic) {
            this.from=from;
            this.to=to;
            this.ic=ic;
        }

        private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
            from=((Double) in.readObject()).doubleValue();
            to=((Double) in.readObject()).doubleValue();
            ic=(NewRestorableImageIcon) in.readObject();
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(new Double(from));
            out.writeObject(new Double(to));
            if (ic instanceof NewRestorableImageIcon)
                out.writeObject(ic);
            else {
                //If the icon is not restorable, create a restorable one.
                BufferedImage bi=new BufferedImage(ic.getIconWidth(),ic.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
                ic.paintIcon(agent,bi.getGraphics(),0,0);
                out.writeObject(new NewRestorableImageIcon(bi));
            }
        }
    }

    Interval[] faces;
    private Agent agent;
}