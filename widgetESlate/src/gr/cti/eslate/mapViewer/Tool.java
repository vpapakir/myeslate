package gr.cti.eslate.mapViewer;


/**
 * An interface defining the additions to the AbstractButton
 * in the tools of the MapViewer
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 10-Apr-2000
 */
interface Tool {
    public abstract void setHelpText(String ht);
    public abstract String getHelpText();
    public abstract boolean isHelpTextChanged();
    /**
     * One of ALWAYS, NEVER, ONMOUSE.
     */
    public abstract void setBorderPolicy(int bp);
    public abstract int getBorderPolicy();
    public abstract void setOrientation(int o);

    public static final int ALWAYS=0;
    public static final int NEVER=1;
    public static final int ONMOUSE=2;
    public static final int HORIZONTAL=0;
    public static final int VERTICAL=1;
}
