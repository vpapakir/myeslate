package gr.cti.eslate.mapModel;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

class MapBackgroundModel implements ListModel {
	MapBackgroundModel() {
		images=new ArrayList();
		listeners=new ArrayList();
	}
	/**
	 * Adds a non-time-aware background. The first image inserted is by default the default background.
	 */
	protected void addBackground(Image back) {
		addBackground(new MapBackground(back));
	}
	/**
	 * Adds a non-time-aware background. The first image inserted is by default the default background.
	 */
	protected void addBackground(MapBackground back) {
		images.add(back);
		//Inform listeners
		ListDataEvent e=new ListDataEvent(this,ListDataEvent.INTERVAL_ADDED,images.size()-1,images.size()-1);
		for (int i=0;i<listeners.size();i++)
			((ListDataListener) listeners.get(i)).intervalAdded(e);
	}
	/**
	 * The index of a background in the model.
	 */
	protected int indexOfBackground(MapBackground back) {
		return images.indexOf(back);
	}
	/**
	 * Removes a background.
	 * @param back  The background to remove.
	 * @return  The index of the background removed.
	 */
	protected int removeBackground(MapBackground back) {
		int i=images.indexOf(back);
		if (i==getDefaultBackgroundIndex())
			setDefaultBackgroundIndex((i!=0)?0:images.size()-2);
		images.remove(back);
		return i;
	}

	protected void setDefaultBackground(MapBackground defaultBackground) {
		defaultBack=defaultBackground;
	}


	protected MapBackground getDefaultBackground() {
		return defaultBack;
	}

	int getDefaultBackgroundIndex() {
		if (defaultBack!=null)
			return images.indexOf(defaultBack);
		else
			return -1;
	}

	void setDefaultBackgroundIndex(int i) {
		try {
			defaultBack=(MapBackground) images.get(i);
		} catch(Throwable e) {
			//To keep consistent and compatible with the old versions
			defaultBack=null;
		}
	}

	/**
	 * @from    If <em>null</em> returns the default background.
	 * @to      If <em>null</em> returns the default background.
	 * @return The background image according to the given date interval. If no such image exists, the default.
	 */
	protected MapBackground getBackground(Date from,Date to) {
		if (from!=null && to!=null)
			for (int i=0;i<images.size();i++) {
				try {
					MapBackground mb=(MapBackground) images.get(i);
					if ((mb.getDateTo().after(from) || mb.getDateTo().equals(from)) && (mb.getDateFrom().before(to) || mb.getDateFrom().equals(to)))
						return mb;
				} catch(Throwable t) {/*No date info?*/}
			}
		return getDefaultBackground();
	}
	/**
	 * javax.swing.ListModel method
	 */
	public int getSize() {
		return images.size();
	}
	/**
	 * javax.swing.ListModel method
	 */
	public Object getElementAt(int index) {
		return images.get(index);
	}
	/**
	 * javax.swing.ListModel method
	 */
	public void addListDataListener(ListDataListener l) {
		if (l!=null)
			listeners.add(l);
	}
	/**
	 * javax.swing.ListModel method
	 */
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
	/**
	 * Clears listeners.
	 */
	void clearListeners() {
		listeners.clear();
	}
	/**
	 * Index of element.
	 */
	public int indexOf(Object o) {
		return images.indexOf(o);
	}

	private ArrayList images;
	private ArrayList listeners;
	private MapBackground defaultBack;
}
