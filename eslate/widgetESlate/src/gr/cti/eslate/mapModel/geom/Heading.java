package gr.cti.eslate.mapModel.geom;

import gr.cti.eslate.protocol.IPolyLine;

/**
 * Used to transfer motion data.
 *
 * @author  Giorgos Vasiliou
 * @version 1.0, 27 ��� 2002
 * @since   2.3.1
 */
public abstract class Heading {
    public boolean invalid;
    public static class Line extends Heading {
        public IPolyLine line;
        public boolean ascending;
        private int segment;
	    public void setSegment(int seg) {
		    segment=seg;
	    }
	    public int getSegment() {
		    return segment;
	    }
	    /**
	     * Creates and returns a copy of this object.
	     */
	    public Object clone() {
		    Heading.Line hl=new Line();
            hl.line=line;
		    hl.ascending=ascending;
		    hl.segment=segment;
		    return hl;
	    }
    }

	/**
	 * Creates and returns a copy of this object.
	 */
	public abstract Object clone();
}
