package gr.cti.eslate.graph2;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * Function to draw.
 * @version     1.0.6, 16-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class Function implements Externalizable {
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;
	/** Used for externalization. */
	private static final int STORAGE_VERSION = 1;
	/** Keys used for externalization. */
	private static final String KEY_EXPRESSION = "expression";
//	private static final String KEY_STEP = "step";
	private static final String KEY_TRANSLATION_X = "translationX";
	private static final String KEY_TRANSLATION_Y = "translationY";
	private static final String KEY_ROTATION = "rotation";
	private static final String KEY_SCALE = "scale";
	private static final String KEY_DOMAIN_FROM = "domainFrom";
	private static final String KEY_DOMAIN_TO = "domainTo";
	private static final String KEY_POINTS = "points";
	/** Default drawing step. */
//    private static final double STEP = 0.1;
	/** Function's string epxression. */
	private String expression;
	/** Function's drawing step. */
//	private double step = STEP;
	/** Drawing color. */
	private Color color;
	/** Transformation translation at axis x. */
	private double translationX;
	/** Transformation translation at axis y. */
	private double translationY;
	/** Transformation rotation. */
	private double rotation;
	/** Transformation scale. */
	private double scale = 1.0;
	/** Domain from/to */
	private double domainFrom, domainTo;
	/** Collection of points. */
	private ArrayList<Point2D.Double> points;
	
	/**
	 * Create a new function.
	 */
	public Function() {
	}
	
	/**
	 * @param expression
	 * @param domainFrom
	 * @param domainTo
	 */
	Function(String expression, double domainFrom, double domainTo) {
		this.expression = expression;
		this.domainFrom = domainFrom;
		this.domainTo = domainTo;
	}
	
	/**
	 * @param expression
	 */
	Function(String expression) {
		this.expression = expression;
		
		points = new ArrayList<Point2D.Double>();
	}

	/**
	 * Get expression.
	 * @return Expression.
	 */
	String getExpression() {
		return expression;
	}
	
	/**
	 * Set expression.
	 * @param expression the expression to set
	 */
	void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Get drawing step.
	 * @return Drawing step.
	 */
//	double getStep() {
//		return step;
//	}

	/**
	 * Set drawing step.
	 * @param step Drawing step.
	 */
//	void setStep(double step) {
//		this.step = step;
//	}

	/**
	 * Get drawing color.
	 * @return Drawing color.
	 */
	Color getColor() {
		return color;
	}

	/**
	 * Set drawing color.
	 * @param color Drawing color.
	 */
	void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Get transformation rotation.
	 * @return Transformation rotation.
	 */
	double getRotation() {
		return rotation;
	}

	/**
	 * Set transformation rotation.
	 * @param rotation Set transformation rotation.
	 */
	void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * Get transformation translation x.
	 * @return Transformation translation x.
	 */
	double getTranslationX() {
		return translationX;
	}

	/**
	 * Set transformation translation x.
	 * @param translationX Transformation translation x.
	 */
	void setTranslationX(double translationX) {
		this.translationX = translationX;
	}

	/**
	 * Get transformation translation y.
	 * @return Transformation translation y.
	 */
	double getTranslationY() {
		return translationY;
	}

	/**
	 * Set transformation translation y.
	 * @param translationX Transformation translation y.
	 */
	void setTranslationY(double translationY) {
		this.translationY = translationY;
	}

	/**
	 * Get transformation scale.
	 * @return Transformation scale.
	 */
	double getScale() {
		return scale;
	}

	/**
	 * Set transformation scale.
	 * @param scale Transformation scale.
	 */
	void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Get domain start.
	 * @return the domain start
	 */
	double getDomainFrom() {
		return domainFrom;
	}

	/**
	 * Set domain start.
	 * @param domainFrom the domain start to set
	 */
	void setDomainFrom(double domainFrom) {
		this.domainFrom = domainFrom;
	}

	/**
	 * Get domain end.
	 * @return the domain end
	 */
	double getDomainTo() {
		return domainTo;
	}

	/**
	 * Set domain end.
	 * @param domainTo the domain end to set
	 */
	void setDomainTo(double domainTo) {
		this.domainTo = domainTo;
	}

	/**
	 * @return the points
	 */
	ArrayList<Point2D.Double> getPoints() {
		return points;
	}

	/**
	 * Remove all points.
	 */
	void removePoints() {
		points.clear();
	}
	
	/**
	 * The object implements the writeExternal method to save its contents by
	 * calling the methods of DataOutput for its primitive values or calling the
	 * writeObject method of ObjectOutput for objects, strings, and arrays.
	 * 
	 * @serialData Overriding methods should use this tag to describe the data
	 *             layout of this Externalizable object. List the sequence of
	 *             element types and, if possible, relate the element to a
	 *             public/protected field and/or method of this Externalizable
	 *             class.
	 * 
	 * @param out
	 *            the stream to write the object to
	 * @exception IOException
	 *                Includes any I/O exceptions that may occur
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);

		fieldMap.put(KEY_EXPRESSION, expression);
//		fieldMap.put(KEY_STEP, step);
		fieldMap.put(KEY_TRANSLATION_X, translationX);
		fieldMap.put(KEY_TRANSLATION_Y, translationY);
		fieldMap.put(KEY_ROTATION, rotation);
		fieldMap.put(KEY_SCALE, scale);
		fieldMap.put(KEY_DOMAIN_FROM, domainFrom);
		fieldMap.put(KEY_DOMAIN_TO, domainTo);
		fieldMap.put(KEY_POINTS, points);
		
		out.writeObject(fieldMap);
		out.flush();
	}

	/**
	 * The object implements the readExternal method to restore its contents by
	 * calling the methods of DataInput for primitive types and readObject for
	 * objects, strings and arrays. The readExternal method must read the values
	 * in the same sequence and with the same types as were written by
	 * writeExternal.
	 * 
	 * @param in
	 *            the stream to read data from in order to restore the object
	 * @exception IOException
	 *                if I/O errors occur
	 * @exception ClassNotFoundException
	 *                If the class for an object being restored cannot be found.
	 */
	@SuppressWarnings("unchecked")
  public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		 StorageStructure fieldMap = (StorageStructure) in.readObject();
		 
		 expression = (String) fieldMap.get(KEY_EXPRESSION);
//		 step = ((Double) fieldMap.get(KEY_STEP, STEP)).doubleValue();
		 translationX = ((Double) fieldMap.get(KEY_TRANSLATION_X)).doubleValue();
		 translationY = ((Double) fieldMap.get(KEY_TRANSLATION_Y)).doubleValue();
		 rotation = ((Double) fieldMap.get(KEY_ROTATION)).doubleValue();
		 scale = ((Double) fieldMap.get(KEY_SCALE)).doubleValue();
		 domainFrom = ((Double) fieldMap.get(KEY_DOMAIN_FROM)).doubleValue();
		 domainTo = ((Double) fieldMap.get(KEY_DOMAIN_TO)).doubleValue();
		 points = (ArrayList<Point2D.Double>) fieldMap.get(KEY_POINTS);
	}
}
