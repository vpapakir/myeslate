package gr.cti.eslate.eslateMenuBar;


import javax.swing.JMenuItem;


public class PathChangedEvent extends java.util.EventObject {
	private static final long serialVersionUID = -5822608728562875232L;
	public static final int PATH_CHANGED = PathChangedEvent.PATH_CHANGED;
    public String[] path;
    private JMenuItem item;

    public PathChangedEvent(Object source, int id) {
        super(source);
    }

    public PathChangedEvent(Object source, int id, String[] givenPath, JMenuItem item) {
        super(source);
        path = givenPath;
        this.item = item;
    }

    public JMenuItem getMenuItem() {
        return item;
    }

    public String[] getPath() {
        return path;
    }

}
